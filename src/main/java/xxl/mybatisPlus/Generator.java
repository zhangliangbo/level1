package xxl.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import xxl.jdbc.JdbcSource;

/**
 * mybatis plus 生成器
 *
 * @author zhangliangbo
 * @since 2020/12/23
 **/


@Slf4j
public class Generator {
    public static boolean generate(String author, String outputDir, String entitySuffix,
                                   String url, String driver, String username, String password,
                                   String parent, String entity, String mapper, String xml, String service, String serviceImpl, String controller,
                                   String tablePrefix, String... table) {
        try {
            AutoGenerator mpg = new AutoGenerator();

            GlobalConfig gc = new GlobalConfig();
            gc.setAuthor(author);
            gc.setOutputDir(outputDir);
            gc.setFileOverride(true);
            gc.setOpen(false);
            gc.setBaseResultMap(true);
            gc.setIdType(IdType.AUTO);
            gc.setEntityName("%s" + entitySuffix);
            mpg.setGlobalConfig(gc);

            DataSourceConfig dsc = new DataSourceConfig();
            dsc.setDbType(DbType.MYSQL);
            dsc.setUrl(url);
            dsc.setDriverName(driver);
            dsc.setUsername(username);
            dsc.setPassword(password);
            mpg.setDataSource(dsc);

            PackageConfig pc = new PackageConfig();
            pc.setParent(parent)
                    .setEntity(entity)
                    .setMapper(mapper)
                    .setXml(xml)
                    .setService(service)
                    .setServiceImpl(serviceImpl)
                    .setController(controller);
            mpg.setPackageInfo(pc);

            StrategyConfig strategy = new StrategyConfig();
            strategy.setTablePrefix(tablePrefix);
            strategy.setNaming(NamingStrategy.underline_to_camel);
            strategy.setColumnNaming(NamingStrategy.underline_to_camel);
            strategy.setEntityLombokModel(true);
            strategy.setRestControllerStyle(true);
            strategy.setInclude(table);
            mpg.setStrategy(strategy);
            mpg.execute();
            return true;
        } catch (Exception e) {
            log.info("generator error {}", e.getMessage());
            return false;
        }
    }

}
