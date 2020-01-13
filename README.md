
# qd-mall
* 奇点商城--->旨在项目搭建以及电商系统架构设计学习和研究，以及相关技术栈的整合学习


## 目前实现依赖功能：
````
qd-mall -- 父项目，公共依赖
|  |─qd-mall-business -- 业务模块一级工程
|  |  |─user-center -- 用户中心
|  |——qd-mall-codegenerator--mybatis-plus代码生成
|  |─qd-mall-commons -- 通用工具(配置)一级工程
|  |  |─qd-mall-base-config -- 封装基础项通用配置
|  |  |─qd-mall-log-config -- 封装日志统一配置
|  |  |─qd-mall-db-config -- 封装数据库通用配置
|  |  |─qd-mall-swagger-config -- 封装swagger通用配置
````

#### (一) mybatis-plus代码生成
* 模块位置：qd-mall-codegenerator
* 参考官方文档撰写：
    * 注意这里的模板引擎有两种 vm 和 ftl；我这里在ftl的基础上做的升级改动。针对我目前项目的结构。
    * 代码生成工具：CodeGenerator类
    * 使用模板引擎：resources/ftl 包下的。其余两个包下为MP底层的原有copy的。
* 使用方法：
    * （1） 启动模块里的main方法
    * （2） 控制台输入示例： (写入指定的具体功能模块和表名)，代码会自动在指定位置生成
        ````
        /qd-mall-business/user-center/user-center-business
        /qd-mall-business/user-center/user-center-client
        sys_user
        ````
* 目前实现程度：可以生成增删改查等基本功能代码。而且代码里的相关统一出入参规范属于个人定义，参考时请记得修改。

* 测试：测试前，确保相关yml、pom依赖等配置完善，相关入参出参规范，在commons模块--base-config中定义
    * localhost:9002/sys-user/add
    ````
      入参：
      {
        "header":{},
        "body":{
            "username":"sunbin",
            "password":"123456",
            "nickname":"binbin",
            "mobile":"18956358459",
            "sex":true,
            "type":"A"
        }
      }  
      出参：
      {
          "header": {
              "code": "200",
              "message": "SUCCESS!"
          },
          "body": 1
      }
     ````

#### （二） swagger 接口文档规范使用：

* 启本地动模块项目后，访问默认地址（每个模块端口修调整）：http://localhost:9002/swagger-ui.html#/

#### （三） 日志配置

* springboot默认使用自带的logback
    * 相关配置：https://www.cnblogs.com/bigdataZJ/p/springboot-log.html
    
        ````
        yml配置:
        # 日志生成文件
        logging.path=/Users/jackie/workspace/rome/ 
        logging.file=springbootdemo.log
        # 日志级别配置（特殊类做处理）
        logging.level.root=INFO
        logging.level.com.jackie.springbootdemo.config=WARN
        # 定制日志格式
        logging.pattern.console=%d{yyyy/MM/dd-HH:mm:ss} [%thread] %-5level %logger- %msg%n 
        logging.pattern.file=%d{yyyy/MM/dd-HH:mm} [%thread] %-5level %logger- %msg%n
        ````
    * 使用：
        * 类中定义：private static final Logger LOG = LoggerFactory.getLogger(LogConfig.class);
        * @Slf4j lombok的日志注解
        * 日志级别总共有TARCE < DEBUG < INFO < WARN < ERROR < FATAL  (springboot 默认info)
* 相关说明：
    * SpringBoot能自动适配所有的日志，而且底层使用slf4j+logback（接口+实现类）的方式记录日志，引入其他框架的时候，只需要把这个框架依赖的日志框架排除掉即可
    * 如：Spring框架用的是commons-logging；在依赖是可以剔除（不清楚的话就 就在顶层pom邮件maven show dependencies查看）
        ````
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>commons-logging</groupId>
                    <artifactId>commons-logging</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        ````
* 统一日志处理：
    * 统一日志简述：https://www.jianshu.com/p/33135f3418f0
        * 对于slf4j 和 log4j、logback、jdk-logging等的关系：
        
        日志门面（日志的抽象层--接口） |	日志实现（日志组件-输出）
        ---|---
        SLF4j 、commons logging(JCL apache旗下)、jboss-logging（非大众服务） |  Log4j 、JUL（java.util.logging）、 Log4j2 、Logback（与slf4j同一作者）
        
        * 关系详细描述以及基本集成  ：https://www.cnblogs.com/manayi/p/9570411.html
        
    * 如果不使用springboot自带的日志框架：替换pom依赖，给类路径下放上每个日志框架自己的配置文件即可；SpringBoot就不使用他默认配置的了，
        * logback的相关独立配置
            * logback.xml：直接就被日志框架识别了,不许额外配置；
            * logback-spring.xml：日志框架就不直接加载日志的配置项，由SpringBoot解析日志配置，可以使用SpringBoot的高级Profile功能（指定不同的环境dev prod test 应用名等）
        * 注意如果使用其他日志框架依赖，记得剔除springboot自带的日志依赖,再导入log4j.xml
        ````
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-web</artifactId>
                  <exclusions>
                 <!-- 去掉默认配置 -->
                      <exclusion>
                          <groupId>org.springframework.boot</groupId>
                          <artifactId>spring-boot-starter-logging</artifactId>
                      </exclusion>
                  </exclusions>
              </dependency>
              <!-- 引入log4j2依赖 -->
              <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-starter-log4j2</artifactId>
              </dependency>
        ````
    * 常见日志系统和方案： 
    
    Logging System	| Customization
    ---|---
    Logback	| logback-spring.xml, logback-spring.groovy, logback.xml or logback.groovy
    Log4j2	| log4j2-spring.xml or log4j2.xml
    JDK | (Java Util Logging)	logging.properties

* 该项目的运用：
    * 使用loback-spring.xml 配置文件，将其封装在公共配置模块，qd-mall-log-config 其余业务项目只需要依赖即可。（使用SpringBoot的高级Profile功能）
    ````
    <dependency>
        <groupId>com.qidian.mall</groupId>
        <artifactId>qd-mall-log-config</artifactId>
    </dependency>
    ````
 
 * lombok 的日志注解的使用：
    * @Slf4j	使用该注解后，可以直接使用log，而无需生成logger对象，是logback使用的
    * @Log4j	当项目是log4j日志框架时使用   
    
#### （四） 引入nacos 作为服务注册中心和配置中心，管理微服务项目

* 启动目录：qd-mall-register/nacos/bin (windows启动startup.cmd linux启动startup.sh)
* nacos后台地址：localhost:8848/nacos
* 一、服务注册步骤：
    * 1、安装nacos启动服务端
    * 2、客户端服务项目添加依赖配置：（切记版本对应，否则注册失败，可参考nacos-start github学习记录和官方文档）
    ````
    # （1）顶层pom （版本对应关系要对 boot2.0.4 ： nacos-cloud 0.2.1 ： cloud Finchley.SR3）
     <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-dependencies</artifactId>
        <version>${spring-cloud-dependencies.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-alibaba-dependencies</artifactId>
        <version>${spring-cloud-alibaba-dependencies.version}</version>
        <type>pom</type>
        <scope>import</scope>
    </dependency>
      <!--各个微服务项目：nacos服务注册-->
      <dependency>
          <groupId>org.springframework.cloud</groupId>
          <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
      </dependency>
  
    # （2）配置yml文件：
    spring:
      application:
        name: qd-mall-usercenter
      # nacos 服务注册地址
      cloud:
        nacos:
          discovery:
            server-addr: 127.0.0.1:8848
  
    # （3）启动类添加注解：服务注册发现
    @EnableDiscoveryClient
  
    # （4）重启项目，成功日志
    2020-01-13 11:20:41.407  INFO 20288 --- [  restartedMain] o.s.c.a.n.registry.NacosServiceRegistry  : nacos registry, qd-mall-usercenter 10.40.28.31:9002 register finished
    ````
* 二、配置中心的应用：
* 加载多个配置文件（共享配置）的方法 ： https://www.cnblogs.com/didispace/p/10358157.html
* 使用步骤：
    ````
    # （1）添加依赖：
    <!--nacos 外部配置中心-->
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-alibaba-nacos-config</artifactId>
    </dependency>
    # （2）新建bootstrap.yml 配置文件，将相关nacos配置迁移至此（这个文件优先级最高）
    # （3）将国际化配置和配置文件加密规则配置放置在nacos后台的配置中心 创建dataId:common.yml的配置数据。
    # （4）重启项目成功日志：
    2020-01-13 14:22:16.566  INFO 4584 --- [-localhost_8848] o.s.c.a.n.c.NacosPropertySourceBuilder   : Loading nacos data, dataId: 'common.yml', group: 'DEFAULT_GROUP'
    2020-01-13 14:22:16.575  INFO 4584 --- [-localhost_8848] b.c.PropertySourceBootstrapConfiguration : Located property source: CompositePropertySource {name='NACOS', propertySources=[NacosPropertySource {name='qd-mall-usercenter.yml'}, NacosPropertySource {name='common.yml'}]}
    ````
* 测试：启动测试类的方法，看看在nacos后台配置的加密规则是否生效（可把配置删除进行对比测试），完善配置是可以正常运行并打印语句。

#### LAST: 问题整理：

* springboot整合mybatis-plus 过程中启动项目报错，mapper注入失败，主要是配置问题：
    * 启动类加@MapperScan 扫描注解
    * 有没有依赖mybatis-plus-boot-starter （因为被封装在db-config 公共以来中，user-center直接依赖db-config就行）
    * yml配置文件
    
* 代码生成的时间类型问题：
    ````
    Error attempting to get column 'update_time' from result set.  Cause: java.sql.SQLFeatureNotSupportedException
    ; null; nested exception is java.sql.SQLFeatureNotSupportedException] with root cause
    
    因为mybatis生成的时间类型 默认为java的LocalDateTime 类型 mybatis 不支持此api，所以报错。
    处理方法：修改日期类型Date ，代码生成工具类修改。gc.setDateType(DateType.ONLY_DATE);
    ````
* nacos日志一直打印：
    ````
    2020-01-13 15:12:26.608  INFO 22792 --- [ing.beat.sender] com.alibaba.nacos.client.naming          : [BEAT] [] [] send beat to server: {"cluster":"DEFAULT","dom":"qd-mall-usercenter","ip":"10.40.28.31","metadata":{},"port":9002,"weight":1.0}
    2020-01-13 15:12:31.608  INFO 22792 --- [ing.beat.sender] com.alibaba.nacos.client.naming          : [BEAT] [] [] send beat to server: {"cluster":"DEFAULT","dom":"qd-mall-usercenter","ip":"10.40.28.31","metadata":{},"port":9002,"weight":1.0}
    2020-01-13 15:12:36.609  INFO 22792 --- [ing.beat.sender] com.alibaba.nacos.client.naming          : [BEAT] [] [] send beat to server: {"cluster":"DEFAULT","dom":"qd-mall-usercenter","ip":"10.40.28.31","metadata":{},"port":9002,"weight":1.0}
    2020-01-13 15:12:41.608  INFO 22792 --- [ing.beat.sender] com.alibaba.nacos.client.naming          : [BEAT] [] [] send beat to server: {"cluster":"DEFAULT","dom":"qd-mall-usercenter","ip":"10.40.28.31","metadata":{},"port":9002,"weight":1.0}
    
    解决方法：yml中配置 logging.level.com.alibaba.nacos.client.naming 的日志级别为warn
    ````