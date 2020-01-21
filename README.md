
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


#### spring security + oauth2 + jwt 实现统一鉴权认证中心

* 获取token的几个controller  请求头里 写client_id:app  client_secret:app 请求body {请求入参}
* 我们支持一下几种获取token登录的方式：
    * 授权码模式：
    * 密码模式：
    * 客户端凭证模式：
    * 简化模式：
    * 自定义获取token 的方式：（上面四种是底层存在的，我们下边在controller包里自定义实现了几种方式）
        * 用户名密码
        * 手机号密码
        * openId
    
* 几种授权类型：（我们都支持）
    * authorization_code：授权码类型。（最安全，适用于第三方app和应用）
    * implicit：隐式授权类型。
    * password：资源所有者（即用户）密码类型。-----采用（因为是把密码交给了浏览器，不安全所以只适用于自己开发的应用）
    * client_credentials：客户端凭据（客户端ID以及Key）类型。（客户端client请求，只要确认client信息准确，就把token发给client，适用于没有用户参与完全信任的服务器端服务）
        * -----所以这种模式下Authentication 为null 所以你不能使用token转换器追加用户信息，即我们现在的 CustomJwtAccessTokenConverter 这个类可以取消，即token不追加用户信息。
    
* 我们支持几种token的存储方式：（可以通过配置文件来配置对应TokenStoreConfig配置类，默认使用JWT）
    * 基于数据库
    * 基于redis
    * 基于jwt
    * 基于内存
    
* 客户端详情获取：
    * 我们基于数据库查询并且做了一层缓存优化（jdbc+redis） 表名：oauth_client_details
    * 几个重要属性：
    * clientId：（必须的）用来标识客户的Id。
    * secret：（需要值得信任的客户端）客户端安全码，如果有的话。
    * scope：用来限制客户端的访问范围，如果为空（默认）的话，那么客户端拥有全部的访问范围。
    * authorizedGrantTypes：此客户端可以使用的授权类型，默认为空。
    * authorities：此客户端可以使用的权限（基于Spring Security authorities）。
    
* spring security的验证流程（AuthenticationManager：https://www.jianshu.com/p/32fa221e03b7）：

* token 的颁发生成管理(AuthorizationServerTokenServices接口：失效时间读书客户端详情数据库的配置，每个客户端目前都是5H)：

* token的获取测试：（grant_type 不同模式认证传参不同）
    * （1） **自定义模式**：
        * 调用controller的3个方法，密码模式获取token （用户名密码、手机号密码、openId，注意请求头带client_id 和 client_secret两个参数）
        
    * （2） **授权码模式**：
        * 因为需要session的支持，首先需要WebSecurityConfiguration配置类中做修改：在主过滤器中配置 httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED); 这个支持所有的oauth2认证，如果不是授权码模式可以不用session，即被注释的部分恢复
        * 获取授权码GET请求： http://localhost:8088/oauth/authorize?response_type=code&client_id=webApp&redirect_uri=http://www.baidu.com
        * 如果没有登录会先跳到登录自定义界面：http://localhost:8088/login.html
        * 登录以后，用户信息在session中了，就会获取到授权码（redirect_uri后面的code 就是授权码，这里我们自定义授权码的存储生成策略RedisAuthorizationCodeServices：利用redis存储并设置了12位长度）：https://www.baidu.com/?code=H4yKSbukQ2Fn
        * 拿到授权码去postman post请求http://localhost:8088/oauth/token?code=H4yKSbukQ2Fn&client_id=webApp&redirect_uri=http://www.baidu.com&grant_type=authorization_code&scope=app&client_secret=webApp 获取token 
            * 注意几个必选参数： client_id client_secret scope grant_type=authorization_code redirect_uri  这些信息在数据库表oauth_client_details中都有存储要对应，否则会报错 违法的客户端之类的
            
    * （3）**客户端凭证模式** ： 
        *  post请求url：http://localhost:8088/oauth/token?client_id=webApp&grant_type=client_credentials&scope=app&client_secret=webApp
        * 此模式下 需要将 我们自定义的CustomJwtAccessTokenConverter token生成转换类 大部分代码注释掉，因为此模式下不涉及用户身份信息，所以Authentication 返回null。我们那个类就是对用户身份信息追加至token的，如果null 就会报空指针，
        * 所以此模式不能追加用户信息至token 还用也没有refresh_token
        ````
          protected OAuth2Authentication getOAuth2Authentication(ClientDetails client, TokenRequest tokenRequest) {
                OAuth2Request storedOAuth2Request = this.requestFactory.createOAuth2Request(client, tokenRequest);
                return new OAuth2Authentication(storedOAuth2Request, (Authentication)null);
            }
        ````
    

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
* 在使用授权码模式进行认证授权流程时候，同一个类出现了类型不匹配，我们自定义的CustomUserDetails 这个用户信息类，我们在CustomJwtAccessTokenConverter 这了自定义的token生成转换器，追加用户信息的时候报错
    * 原因： 因为在根节点pom中使用了spring-boot-devtools 的热部署（但是我设置了option 为true，也没用），这个依赖使用的类加载器和默认的不同，所以会出现这个问题。
    * 参考连接：https://www.cnblogs.com/UncleWang001/p/10063172.html
    * 调试：debug 选中类 ADD TO WATCH--->edit  .class .classloader() 可查看类加载器
    * 启动热部署的情况下，
      类的加载器是 org.springframework.boot.devtools.restart.classloader.RestartClassLoader
      原先的类加载器（默认的系统类加载器）是 sun.misc.Launcher$AppClassLoader
      因为是不同的类加载器，所以会报错。