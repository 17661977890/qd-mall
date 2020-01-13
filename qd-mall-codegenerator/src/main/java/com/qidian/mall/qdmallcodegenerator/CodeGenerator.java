package com.qidian.mall.qdmallcodegenerator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 演示例子，执行 main 方法控制台输入模块表名回车自动生成对应项目目录中
 *
 * 注意这里的模板引擎有两种 vm 和 ftl 我这里在ftl的基础上做的升级改动。针对我目前项目的结构
 */
public class CodeGenerator {

    /**
     * <p>
     * 读取控制台内容(控制台输入示例)
     *  /qd-mall-business/user-center/user-center-business
     *  /qd-mall-business/user-center/user-center-client
     *  usercenter----去掉模块名 这个不需要输入
     *  sys_user
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            if (StringUtils.isNotEmpty(ipt)) {
                return ipt;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        /**
         * 全局配置
         *
         */
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir")+scanner("项目业务子模块");
        String projectClientPath = System.getProperty("user.dir")+scanner("项目独立接口子模块");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("binsun");
        gc.setOpen(false);
        // 主键策略
        gc.setIdType(IdType.AUTO);
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(false);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(true);
        // 实体属性 Swagger2 注解
        gc.setSwagger2(true);
        // 设置时间类型为java.util.Date---原因默认的LocalDateTime mybatis不支持
        gc.setDateType(DateType.ONLY_DATE);
        mpg.setGlobalConfig(gc);


        /**
         * 数据源配置
         */
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/qd-mall?useUnicode=true&useSSL=false&characterEncoding=utf8");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        mpg.setDataSource(dsc);

        /**
         * 包配置(代码生成路径)
         */
        PackageConfig pc = new PackageConfig();
        //去掉这个，因为不便于生成dto vo的包名路径，只怪自己不想研究底层解析语法，这里图省事
//        pc.setModuleName(scanner("包模块名"));
        pc.setParent("com.qidian.mall");
        mpg.setPackageInfo(pc);

        /**
         * 注入配置，通过该配置，可注入自定义参数等操作以实现个性化操作
         */
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };

        // 如果模板引擎是freemarker
        // --这里我们用自定义的模板引擎路径: ./templates/mapper.xml.ftl
        // --官方默认的位置: /templates/mapper.xml.ftl
        String templatePath = "/ftl/mapper.xml.ftl";
        String templatePath2 = "/ftl/entity.java.ftl";
        String templatePath3 = "/ftl/entityVO.java.ftl";
        String templatePath4 = "/ftl/entityDTO.java.ftl";
        // 如果模板引擎是velocity
        // String templatePath = "/templates/mapper.xml.vm";

        // 实体类和mapper.xml自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        focList.add(new FileOutConfig(templatePath2) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义实体类输出文件名（独立接口部分）
                return projectClientPath + "/src/main/java/" + pc.getParent()
                        + "/entity/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(templatePath3) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义相应出参vo对象输出文件名（独立接口部分）
                return projectClientPath + "/src/main/java/" + pc.getParent()
                        + "/response/" + tableInfo.getEntityName()+"VO" + StringPool.DOT_JAVA;
            }
        });
        focList.add(new FileOutConfig(templatePath4) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义请求入参DTO输出文件名（独立接口部分）
                return projectClientPath + "/src/main/java/" + pc.getParent()
                        + "/request/" + tableInfo.getEntityName()+"DTO" + StringPool.DOT_JAVA;
            }
        });
        /*
        cfg.setFileCreate(new IFileCreate() {
            @Override
            public boolean isCreate(ConfigBuilder configBuilder, FileType fileType, String filePath) {
                // 判断自定义文件夹是否需要创建
                checkDir("调用默认方法创建的目录");
                return false;
            }
        });
        */
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        /**
         * 自定义模板配置，可自定义代码生成的模板，实现个性化操作-----这里我们使用MP 自带的freemarker
         */
        TemplateConfig templateConfig = new TemplateConfig();
        //指定自定义模板路径，注意不要带上.ftl/.vm, 会根据使用的模板引擎自动识别
        templateConfig.setService("ftl/service.java");
        templateConfig.setController("ftl/controller.java");
        templateConfig.setServiceImpl("ftl/serviceImpl.java");
        templateConfig.setMapper("ftl/mapper.java");
        // 关闭默认 xml 和 entity生成，调整生成 至 根目录
        templateConfig.setEntity(null);
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        /**
         * 策略配置
         */
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
//        strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
//        strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 写于父类中的公共字段
//        strategy.setSuperEntityColumns("id");
        strategy.setInclude(scanner("表名，多个英文逗号分割").split(","));
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        //使用默认模板引擎，velocity，下面这行注释就可以了
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());
        mpg.execute();
    }

}