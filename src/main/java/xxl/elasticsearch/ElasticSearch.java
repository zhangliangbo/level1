package xxl.elasticsearch;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetMappingsRequest;
import org.elasticsearch.client.indices.GetMappingsResponse;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhangliangbo
 * @since 2021/8/14
 **/


@Slf4j
public class ElasticSearch {

    private static String httpHost;
    private static String username;
    private static String password;

    private static RestHighLevelClient restHighLevelClient;

    public static void use(String host, String un, String pw) {
        httpHost = host;
        username = un;
        password = pw;
    }

    private synchronized static RestHighLevelClient getClient() {
        if (Objects.isNull(restHighLevelClient)) {
            if (StringUtils.isEmpty(httpHost)) {
                throw new IllegalArgumentException("请使用ElasticSearch.use(...)设置数据源");
            }

            RestClientBuilder restClientBuilder = RestClient.builder(HttpHost.create(httpHost));
            //用户名密码
            restClientBuilder.setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
                @Override
                public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
                    BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
                    basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
                    return httpClientBuilder.setDefaultCredentialsProvider(basicCredentialsProvider);
                }
            });
            //失败回调
            restClientBuilder.setFailureListener(new RestClient.FailureListener() {
                @Override
                public void onFailure(Node node) {
                    log.info("Elastic Node {} 失败", node.toString());
                    if (Objects.nonNull(restHighLevelClient)) {
                        try {
                            restHighLevelClient.close();
                        } catch (IOException e) {
                            log.info("Elastic Node {} 关闭失败", node, e);
                        }
                        restHighLevelClient = null;
                    }
                }
            });
            //超时配置
            restClientBuilder.setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                @Override
                public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                    return requestConfigBuilder
                            .setConnectionRequestTimeout(90000)
                            .setConnectTimeout(60000)
                            .setSocketTimeout(60000);
                }
            });
            restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        }
        return restHighLevelClient;
    }

    public static boolean bulkIndex(String index, List<Map<String, Object>> dataset) {
        return bulk(index, dataset, map -> String.valueOf(map.get("id")), 1);
    }

    public static boolean bulkUpdate(String index, List<Map<String, Object>> dataset) {
        return bulk(index, dataset, map -> String.valueOf(map.get("id")), 0);
    }

    public static boolean bulkDelete(String index, List<String> ids) {
        List<Map<String, Object>> dataset = ids.stream().map(t -> {
            Map<String, Object> map = new HashMap<>(1);
            map.put("id", t);
            return map;
        }).collect(Collectors.toList());
        return bulk(index, dataset, map -> String.valueOf(map.get("id")), -1);
    }

    public static boolean bulk(String index, List<Map<String, Object>> dataset, Function<Map<String, Object>, String> func, int mode) {
        BulkRequest bulkRequest = Requests.bulkRequest();
        dataset.forEach(map -> {
            switch (mode) {
                case 1:
                default:
                    IndexRequest indexRequest = Requests.indexRequest(index).id(func.apply(map)).source(map, XContentType.JSON);
                    bulkRequest.add(indexRequest);
                    break;
                case 0:
                    UpdateRequest updateRequest = new UpdateRequest(index, func.apply(map)).doc(map, XContentType.JSON);
                    bulkRequest.add(updateRequest);
                    break;
                case -1:
                    DeleteRequest deleteRequest = Requests.deleteRequest(index).id(func.apply(map));
                    bulkRequest.add(deleteRequest);
                    break;
            }
        });
        try {
            BulkResponse bulkResponse = getClient().bulk(bulkRequest, RequestOptions.DEFAULT);
            return bulkResponse.status().equals(RestStatus.OK);
        } catch (IOException e) {
            log.info("bulk报错", e);
            return false;
        }
    }

    public static List<Map<String, Object>> matchAll(String index) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchAllQuery()));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> multiMatch(String index, Object value, String... field) {
        Set<String> nested = Stream.of(field).filter(t -> t.contains(".")).collect(Collectors.toSet());
        QueryBuilder queryBuilder;
        if (nested.isEmpty()) {
            queryBuilder = QueryBuilders.multiMatchQuery(value, field);
        } else {
            BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
            for (String n : nested) {
                List<String> split = Stream.of(n.split("\\.")).collect(Collectors.toList());
                String path = String.join(".", split.subList(0, split.size() - 1));
                String name = String.join(".", split.subList(split.size() - 2, split.size()));
                boolQueryBuilder.should(QueryBuilders.nestedQuery(path, QueryBuilders.matchQuery(name, value), ScoreMode.None));
            }
            boolQueryBuilder.should(QueryBuilders.multiMatchQuery(value, Stream.of(field).filter(t -> !t.contains(".")).distinct().toArray(String[]::new)));
            queryBuilder = boolQueryBuilder;
        }
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(queryBuilder));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> match(String index, String field, Object value) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchQuery(field, value)));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> matchPhrase(String index, String field, Object value) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchPhraseQuery(field, value)));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> term(String index, String field, Object value) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.termQuery(field, value)));
        return search(searchRequest);
    }

    public static Map<String, Object> mapping(String index) {
        try {
            GetMappingsResponse getMappingsResponse = getClient().indices().getMapping(new GetMappingsRequest().indices(index), RequestOptions.DEFAULT);
            Map<String, MappingMetadata> mappings = getMappingsResponse.mappings();
            Map<String, Object> res = new HashMap<>(mappings.size());
            mappings.forEach((s, mappingMetadata) -> res.put(s, mappingMetadata.getSourceAsMap()));
            return res;
        } catch (IOException e) {
            log.info("获取mapping报错", e);
            return null;
        }
    }

    private static List<Map<String, Object>> search(SearchRequest searchRequest) {
        try {
            SearchResponse searchResponse = getClient().search(searchRequest, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            return Stream.of(hits.getHits()).map(SearchHit::getSourceAsMap).collect(Collectors.toList());
        } catch (IOException e) {
            log.info("search报错", e);
            return null;
        }
    }

    public static Map<String, Object> get(String index, String id) {
        try {
            GetResponse getResponse = getClient().get(new GetRequest(index, id), RequestOptions.DEFAULT);
            return getResponse.getSourceAsMap();
        } catch (IOException e) {
            log.info("get报错", e);
            return null;
        }
    }

    public static boolean update(String index, String id, Map<String, Object> doc) {
        try {
            UpdateResponse updateResponse = getClient().update(new UpdateRequest(index, id).doc(doc, XContentType.JSON), RequestOptions.DEFAULT);
            return updateResponse.status().equals(RestStatus.OK);
        } catch (IOException e) {
            log.info("update报错", e);
            return false;
        }
    }

    public static boolean delete(String index, String id) {
        try {
            DeleteResponse deleteResponse = getClient().delete(new DeleteRequest(index, id), RequestOptions.DEFAULT);
            return deleteResponse.status().equals(RestStatus.OK);
        } catch (IOException e) {
            log.info("delete报错", e);
            return false;
        }
    }

    public static boolean index(String index, String id, Map<String, Object> doc) {
        try {
            IndexResponse indexResponse = getClient().index(Requests.indexRequest(index).id(id).source(doc, XContentType.JSON), RequestOptions.DEFAULT);
            return indexResponse.status().equals(RestStatus.OK);
        } catch (IOException e) {
            log.info("index报错", e);
            return false;
        }
    }

    public static boolean deleteIndex(String index) {
        try {
            AcknowledgedResponse acknowledgedResponse = getClient().indices().delete(Requests.deleteIndexRequest(index), RequestOptions.DEFAULT);
            return acknowledgedResponse.isAcknowledged();
        } catch (IOException e) {
            log.info("deleteIndex报错", e);
            return false;
        }
    }

    public static List<Map<String, Object>> matchAllBy(String index, String field) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.matchAllQuery().queryName(field)));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> nestedMatch(String index, String path, String field, Object value) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.nestedQuery(path, QueryBuilders.matchQuery(field, value), ScoreMode.None)));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> nestedMatchPhrase(String index, String path, String field, Object value) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.nestedQuery(path, QueryBuilders.matchPhraseQuery(field, value), ScoreMode.None)));
        return search(searchRequest);
    }

    public static List<Map<String, Object>> nestedTerm(String index, String path, String field, Object value) {
        SearchRequest searchRequest = Requests.searchRequest(index)
                .source(SearchSourceBuilder.searchSource().query(QueryBuilders.nestedQuery(path, QueryBuilders.termQuery(field, value), ScoreMode.None)));
        return search(searchRequest);
    }

}
