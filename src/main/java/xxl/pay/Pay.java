package xxl.pay;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import xxl.codec.digest.DigestUtils;
import xxl.mathematica.io.ExportString;
import xxl.mathematica.string.StringRiffle;
import xxl.mathematica.time.DateString;
import xxl.saobei.SaobeiPay;
import xxl.wx.pay.WXPay;
import xxl.wx.pay.WXPayUtil;

import java.util.*;

/**
 * 支付
 */
public class Pay {

    private static Map<String, AlipayClient> aliMap = new HashMap<>();
    private static Map<String, WXPay> payMap = new HashMap<>();
    private static Map<String, SaobeiPay> saobeiMap = new HashMap<>();

    /**
     * 获取下单二维码
     *
     * @param appId          应用ID
     * @param outTradeNo     商家交易单号
     * @param totalAmount    总金额，以分为单位
     * @param goodsName      商品明细
     * @param goodsDesc      商品描述
     * @param storeId        商家门店编号
     * @param timeoutExpress 交易超时时间
     * @return
     */
    public static Map<String, String> aliBarcode(String appId, String outTradeNo, long totalAmount, String goodsName, String goodsDesc, String storeId, String timeoutExpress, String notifyUrl) {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(notifyUrl);
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("total_amount", String.format(Locale.CHINA, "%.2f", totalAmount / 100f));
        map.put("subject", goodsName);
        map.put("body", goodsDesc);
        map.put("store_id", storeId);
        map.put("timeout_express", timeoutExpress);
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            AlipayTradePrecreateResponse response = getAli(appId).execute(request);
            return ExportString.exportStringMap(response);
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 取消订单
     *
     * @param appId
     * @param tradeNo
     * @param outTradeNo
     * @return
     */
    public static Map<String, String> aliOrderCancel(String appId, String tradeNo, String outTradeNo) {
        AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
        Map<String, String> map = new HashMap<>();
        if (tradeNo != null) {
            map.put("trade_no", tradeNo);
        } else {
            map.put("out_trade_no", outTradeNo);
        }
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            AlipayTradeCancelResponse response = getAli(appId).execute(request);
            return ExportString.exportStringMap(response);
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 根据阿里订单号或者商家订单号查询订单
     *
     * @param appId
     * @param tradeNo    阿里订单号
     * @param outTradeNo 商家订单号
     * @return
     */
    public static Map<String, String> aliOrderQuery(String appId, String tradeNo, String outTradeNo) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String, String> map = new HashMap<>();
        if (tradeNo != null) {
            map.put("trade_no", tradeNo);
        } else {
            map.put("out_trade_no", outTradeNo);
        }
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            AlipayTradeQueryResponse response = getAli(appId).execute(request);
            return ExportString.exportStringMap(response);
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 根据阿里订单号或者商家订单号退款
     *
     * @param appId
     * @param tradeNo
     * @param outTradeNo
     * @return
     */
    public static Map<String, String> aliOrderRefund(String appId, String tradeNo, String outTradeNo, long refundMoney, String refundReason) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        Map<String, String> map = new HashMap<>();
        if (tradeNo != null) {
            map.put("trade_no", tradeNo);
        } else {
            map.put("out_trade_no", outTradeNo);
        }
        map.put("refund_amount", String.format(Locale.CHINA, "%.2f", refundMoney / 100f));
        map.put("refund_reason", refundReason);
        request.setBizContent(ExportString.exportStringJson(map));
        try {
            AlipayTradeRefundResponse response = getAli(appId).execute(request);
            return ExportString.exportStringMap(response);
        } catch (AlipayApiException e) {
            return null;
        }
    }

    /**
     * 验证阿里订单回调参数
     *
     * @return
     */
    public static boolean aliRsaCheckV1(Map<String, String> params, String aliPublicKey) {
        try {
            return AlipaySignature.rsaCheckV1(params, aliPublicKey, "UTF-8", "RSA2");
        } catch (AlipayApiException e) {
            return false;
        }
    }

    /**
     * 注册阿里平台信息
     *
     * @param serverUrl  服务器地址
     * @param appId      程序ID
     * @param privateKey 私钥（建议使用工具生成）
     * @param publicKey  阿里公钥
     */
    public static void registerAli(String serverUrl, String appId, String privateKey, String publicKey) {
        AlipayClient client = aliMap.get(appId);
        if (client == null) {
            aliMap.put(appId, new DefaultAlipayClient(serverUrl, appId, privateKey, "json", "UTF-8", publicKey, "RSA2"));
        }
    }

    /**
     * 注册微信平台信息
     *
     * @param mchId    商户平台账号
     * @param certFile 证书文件
     * @param appId    公众平台账号
     * @param key      商户平台校验值
     */
    public static void registerWx(String mchId, String certFile, String appId, String key) {
        WXPay wxPay = payMap.get(mchId);
        if (wxPay == null) {
            payMap.put(mchId, new WXPay(new SimpleWxConfig(appId, certFile, key, mchId), null, false));
        }
    }

    /**
     * 注册扫呗平台信息
     *
     * @param url        服务器地址
     * @param mchId      商户ID
     * @param terminalId 终端ID
     * @param token      终端token
     */
    public static void registerSaobei(String url, String mchId, String terminalId, String token) {
        SaobeiPay saobeiPay = saobeiMap.get(terminalId);
        if (saobeiPay == null) {
            saobeiMap.put(terminalId, new SaobeiPay(url, mchId, terminalId, token));
        }
    }

    /**
     * 微信统一下单
     *
     * @param mchId
     * @param outTradeNo
     * @param money
     * @param goodsName
     * @param goodsDesc
     * @param deviceInfo
     * @param ip
     * @param notifyUrl
     * @param type
     * @param productId  trade_type=NATIVE时，此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
     * @param openId     trade_type=JSAPI时（即JSAPI支付），此参数必传，此参数为微信用户在商户对应appid下的唯一标识。
     * @param attach     附加数据
     * @return
     */
    private static Map<String, String> wxOrder(String mchId, String outTradeNo, long money, String goodsName, String goodsDesc, String deviceInfo, String ip, String notifyUrl, String type, String productId, String openId, String attach) {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        map.put("total_fee", String.valueOf(money));
        map.put("body", goodsName);
        map.put("attach", goodsDesc);
        map.put("device_info", deviceInfo);
        map.put("spbill_create_ip", ip);
        map.put("trade_type", type);
        if (notifyUrl != null) {
            map.put("notify_url", notifyUrl);
        }
        if (productId != null) {
            map.put("product_id", productId);
        }
        if (openId != null) {
            map.put("openid", openId);
        }
        if (attach != null) {
            map.put("attach", attach);
        }
        try {
            return getWxPay(mchId).unifiedOrder(map);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 微信JS下订单
     *
     * @param mchId
     * @param outTradeNo
     * @param money
     * @param goodsName
     * @param goodsDesc
     * @param deviceInfo
     * @param ip
     * @param notifyUrl
     * @return
     */
    public static Map<String, String> wxJsOrder(String mchId, String outTradeNo, long money, String goodsName, String goodsDesc, String deviceInfo, String ip, String notifyUrl, String openId, String attach) {
        return wxOrder(mchId, outTradeNo, money, goodsName, goodsDesc, deviceInfo, ip, notifyUrl, "JSAPI", null, openId, attach);
    }

    /**
     * 微信APP下订单
     *
     * @param mchId
     * @param outTradeNo
     * @param money
     * @param goodsName
     * @param goodsDesc
     * @param deviceInfo
     * @param ip
     * @param notifyUrl
     * @return
     */
    public static Map<String, String> wxAppOrder(String mchId, String outTradeNo, long money, String goodsName, String goodsDesc, String deviceInfo, String ip, String notifyUrl, String attach) {
        return wxOrder(mchId, outTradeNo, money, goodsName, goodsDesc, deviceInfo, ip, notifyUrl, "APP", null, null, attach);
    }

    /**
     * 微信支付二维码
     *
     * @param mchId
     * @param outTradeNo
     * @param money
     * @param goodsName
     * @param goodsDesc
     * @param deviceInfo
     * @param ip
     * @param notifyUrl
     * @return
     */
    public static Map<String, String> wxBarcode(String mchId, String outTradeNo, long money, String goodsName, String goodsDesc, String deviceInfo, String ip, String notifyUrl, String productId, String attach) {
        return wxOrder(mchId, outTradeNo, money, goodsName, goodsDesc, deviceInfo, ip, notifyUrl, "NATIVE", productId, null, attach);
    }

    /**
     * 微信下载对账单
     *
     * @param mchId
     * @param billDate 日期，格式：yyyMMdd
     * @param billType 类型，可选值：ALL/SUCCESS/REFUND/RECHARGE_REFUND
     * @return
     */
    public static Map<String, String> wxDownloadBill(String mchId, String billDate, String billType) {
        Map<String, String> map = new HashMap<>();
        map.put("bill_date", billDate);
        map.put("bill_type", billType);
        try {
            return getWxPay(mchId).downloadBill(map);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 关闭微信订单
     *
     * @param mchId
     * @param outTradeNo 商户订单号
     * @return
     */
    public static Map<String, String> wxOrderCancel(String mchId, String outTradeNo) {
        Map<String, String> map = new HashMap<>();
        map.put("out_trade_no", outTradeNo);
        try {
            return getWxPay(mchId).closeOrder(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询微信订单
     *
     * @param mchId      微信商户号
     * @param tradeNo    微信订单号
     * @param outTradeNo 商户订单号
     * @return
     */
    public static Map<String, String> wxOrderQuery(String mchId, String tradeNo, String outTradeNo) {
        try {
            Map<String, String> map = new HashMap<>();
            if (tradeNo != null) {
                map.put("transaction_id", tradeNo);
            } else {
                map.put("out_trade_no", outTradeNo);
            }
            return getWxPay(mchId).orderQuery(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信退款
     *
     * @param mchId       商户号
     * @param tradeNo     微信订单号
     * @param outTradeNo  商户订单号
     * @param outRefundNo 退款单号
     * @param totalFee    订单总金额
     * @param refundFee   退款总金额
     * @return
     */
    public static Map<String, String> wxOrderRefund(String mchId, String tradeNo, String outTradeNo, String outRefundNo, long totalFee, long refundFee, String notifyUrl, String reason) {
        try {
            Map<String, String> map = new HashMap<>();
            if (tradeNo != null) {
                map.put("transaction_id", tradeNo);
            } else {
                map.put("out_trade_no", outTradeNo);
            }
            map.put("out_refund_no", outRefundNo);
            map.put("total_fee", String.valueOf(totalFee));
            map.put("refund_fee", String.valueOf(refundFee));
            if (notifyUrl != null) {
                map.put("notify_url", notifyUrl);
            }
            if (reason != null) {
                map.put("refund_desc", reason);
            }
            return getWxPay(mchId).refund(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 查询退款订单
     *
     * @param mchId
     * @param refundNo    微信退款单号
     * @param outRefundNo 商家退款单号
     * @param tradeNo     微信订单号
     * @param outTradeNo  商家订单号
     * @return
     */
    public static Map<String, String> wxOrderRefundQuery(String mchId, String refundNo, String outRefundNo, String tradeNo, String outTradeNo) {
        try {
            Map<String, String> map = new HashMap<>();
            if (refundNo != null) {
                map.put("refund_id", refundNo);
            } else if (outRefundNo != null) {
                map.put("out_refund_no", outRefundNo);
            } else if (tradeNo != null) {
                map.put("transaction_id", tradeNo);
            } else {
                map.put("out_trade_no", outTradeNo);
            }
            return getWxPay(mchId).refundQuery(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信短链接
     *
     * @param mchId
     * @param longUrl
     * @return
     */
    public static Map<String, String> wxShortUrl(String mchId, String longUrl) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("long_url", longUrl);
            return getWxPay(mchId).shortUrl(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信回调 回复失败
     *
     * @param reason
     * @return
     */
    public static String wxFail(String reason) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "FAIL");
        map.put("return_msg", reason);
        try {
            return WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信回调 回复成功
     *
     * @return
     */
    public static String wxSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "SUCCESS");
        map.put("return_msg", "OK");
        try {
            return WXPayUtil.mapToXml(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信map转xml
     *
     * @param map
     * @return
     * @throws Exception
     */
    public static String wxMapToXml(Map<String, String> map) throws Exception {
        return WXPayUtil.mapToXml(map);
    }


    /**
     * 微信xml转map
     *
     * @param xml
     * @return
     * @throws Exception
     */
    public static Map<String, String> wxXmlToMap(String xml) throws Exception {
        return WXPayUtil.xmlToMap(xml);
    }

    /**
     * 扫呗签名参数
     *
     * @return
     */
    public static String sbKeySign(Map<String, String> map, boolean dictSortQ, String token) {
        if (dictSortQ) {
            map = new TreeMap<>(map);
        }
        List<String> params = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            params.add(entry.getKey() + "=" + entry.getValue());
        }
        String riffle = StringRiffle.stringRiffle(params, "&");
        String withToken = riffle + "&access_token=" + token;
        return DigestUtils.md5Hex(withToken);
    }

    /**
     * 扫呗支付二维码
     *
     * @param terminalId
     * @param outTradeNo
     * @param money
     * @param goodsName
     * @param deviceInfo
     * @param notifyUrl
     * @return
     */
    public static Map<String, String> sbBarcode(String terminalId, String outTradeNo, long money, String goodsName, String deviceInfo, String notifyUrl) {
        SaobeiPay saobeiPay = getSaobei(terminalId);
        Map<String, String> map = new HashMap<>();
        //所有参数都加密
        map.put("pay_ver", "110");
        map.put("pay_type", "000");
        map.put("service_id", "016");
        map.put("merchant_no", saobeiPay.getMchId());
        map.put("terminal_id", saobeiPay.getTerminalId());
        map.put("terminal_trace", outTradeNo);
        map.put("terminal_time", DateString.dateString("yyyyMMddHHmmss"));
        map.put("total_fee", String.valueOf(money));
        //非必填
        map.put("order_body", goodsName);
        if (notifyUrl != null) {
            map.put("notify_url", notifyUrl);
        }
        map.put("attach", deviceInfo);
        map.put("repeated_trace", "1");
        map.put("key_sign", sbKeySign(map, true, saobeiPay.getToken()));
        try {
            return saobeiPay.aggregateCode(map);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }

    /**
     * 微信回调 回复失败
     *
     * @param reason
     * @return
     */
    public static String sbFail(String reason) {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "02");
        map.put("return_msg", reason);
        try {
            return ExportString.exportStringJson(map);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 微信回调 回复成功
     *
     * @return
     */
    public static String sbSuccess() {
        Map<String, String> map = new HashMap<>();
        map.put("return_code", "01");
        map.put("return_msg", "OK");
        try {
            return ExportString.exportStringJson(map);
        } catch (Exception e) {
            return null;
        }
    }

    private static AlipayClient getAli(String appId) {
        AlipayClient ali = aliMap.get(appId);
        if (ali == null) {
            throw new IllegalArgumentException("请先使用registerAli注册支付宝平台信息");
        }
        return ali;
    }

    private static WXPay getWxPay(String mchId) {
        WXPay wxPay = payMap.get(mchId);
        if (wxPay == null) {
            throw new IllegalArgumentException("请先使用registerWx注册微信平台信息");
        }
        return wxPay;
    }

    private static SaobeiPay getSaobei(String terminalId) {
        SaobeiPay sb = saobeiMap.get(terminalId);
        if (sb == null) {
            throw new IllegalArgumentException("请先使用registerSaobei注册支付宝平台信息");
        }
        return sb;
    }
}
