
# qd-mall
* 奇点商城--->旨在项目搭建以及电商系统架构设计学习和研究，以及相关技术栈的整合学习
* 通过maven-nexus私服和gitlab私有代码托管平台的搭建，该项目jar包已部署到私服，代码已经迁移至gitlab
    * my-gitlab： 
        * 自家虚拟机搭建：192.168.0.105 两个管理员账号：sunbin 和 admin （密码有道云记载）  地址：http://192.168.0.105:8082/sunbin/my-qd-mall （内网地址 哈哈 所以你们不连我家wifi是不能访问的。。。）
        * 我自己的阿里云服务器中也基于linux原生搭建了gitlab，同时也利用了docker-compose搭建  账号都是默认 admin 密码也是有道云记载
        * 注意事项：搭建过程中 服务器需要至少2G内存，否则很慢
    * my-nexus：
        * 在阿里云服务器用docker-compose搭建  地址：http://47.103.18.65:8081/ （外网地址）
       
## 业务架构图：
![image](https://github.com/17661977890/qd-mall/blob/master/image/WechatIMG61.png)

## 目前实现依赖功能：
````
qd-mall -- 父项目，公共依赖
|  |-image -- 图片文件
|  |─qd-mall-business -- 业务模块一级工程
|  |  |─user-center -- 用户中心
|  |  |  |-user-business -- 用户业务模块
|  |  |  |-quartz -- 定时调度框架的整合（旧版 springboot 1.x）
|  |  |  |-async  -- 多线程异步任务使用案例
|  |—qd-mall-codegenerator--mybatis-plus代码生成
|  |─qd-mall-commons -- 通用工具(配置)一级工程
|  |  |-qd-mall-auth-config -- 认证授权相关公共配置类配置
|  |  |─qd-mall-base-config -- 封装基础项通用配置（异步async 定时线程池 cros跨域 统一异常处理 统一出参入参规范 redisTemplate序列化配置和工具类 httpclient配置 国际化 相关工具类）
|  |  |─qd-mall-log-config -- 封装日志统一配置
|  |  |─qd-mall-db-config -- 封装数据库通用配置
|  |  |─qd-mall-swagger-config -- 封装swagger通用配置
|  |  |-qd-mall-redis-config -- redis 通用配置 （工具类、分布式锁、缓存、序列化）、jedis 连接池配置和相关命令工具类（springboot 整合redis(配置、工具类参考base模块) 和 jedis 两种方式 来操作redis ）
|  |  |-qd-mall-quarzt-config -- quartz 定时调度框架的封装 （基于springboot2.x starter）
|  |-qd-mall-fileserver -- 文件存储服务oss
|  |  |-file-business -- 业务实现 
|  |  |-file-client -- 暴露接口feign
|  |-qd-mall-gateway -- spring-cloud-gateway 动态路由网关
|  |-qd-mall-message -- rocketMq 消息队列（消息服务）
|  |  |-message-business -- 业务实现 
|  |  |-message-client -- 暴露接口feign
|  |-qd-mall-register -- nacos注册中心
|  |-qd-mall-search -- es搜索服务
|  |  |-search-business -- 业务实现 
|  |  |-search-client -- 暴露接口feign
|  |-qd-mall-uaa -- spring-security-oauth2 统一认证与授权（既是认证服务器，又是资源服务器）
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
* 相关rul认证限制已放行 在PermitProperties 配置类中
    ````
    "/*/v2/api-docs",
    "/v2/api-docs",  ---- 这个必须放行，要不然打开界面是空的 看不到接口列表
    "/swagger/api-docs",
    "/swagger-ui.html",
    "/swagger-resources/**",
    "/webjars/**",
    "/csrf",
    "/"
    ````
* 关于swagger ui界面的填充：
    ````
    # 相关其余配置项 可以查看SwaggerProperties 配置类进行填充
    mall:
      swagger:
        enabled: true
        title: 消息服务中心
        description: 消息服务中心接口文档
        version: 1.0
        base-package: com.qidian.mall.message.sms.controller
        globalOperationParameters:
        - name: token
          description: token
          modelRef: string
          parameterType: header
          required: false
    ````
 * 代码规范编写：
    * controller类注解 @Api(value = "AliyunSmsController", description = "消息服务")  或 @Api(tags = "用户认证token相关")
    * 方法注解 @ApiOperation(value = "发送短信", notes = "阿里云发送短信，支持多手机")
    * 入参或者实体： @ApiModel(value="SysUser对象", description="系统用户名")  
    * 属性：  @ApiModelProperty(value = "用户名")
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
* nacos后台地址：localhost:8848/nacos  账号密码nacos
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


#### （五）spring security + oauth2 + jwt 实现统一鉴权认证中心

* 基于spring security的HttpSecurity 配置，我们这里整合oauth2，所以流程如下：
    * WebSecurityConfiguration ：
    spring security配置安全认证类，配置从数据库读取用户信息，基于httpSecurity的安全配置，配置了登录登出和session以及自定义认证链的相关配置，
    优先级在资源服务器之前。
    * ResourceServerConfiguration：资源服务器，基于httpSecurity的安全配置，在WebSecurityConfiguration的之后执行，定义了需要放过的请求和需要认证的请求。
        * 注意：因为我们WebSecurityConfiguration 配置中只是做了那些请求免登录，没有对其他请求做拦截或者放行的配置，
        所以我们在资源服务器做这些请求的配置，那么.requestMatcher(request -> true) 这个要设置为true，否则匹配规则不生效，即不会匹配请求做认证拦截
    * AuthorizationServerConfiguration ：认证服务器，定义客户端详情配置（数据库读取）、令牌存储生成方式、授权模式（类型）、授权码管理、令牌端点约束等

* 关于资源服务器的配置简单说几句：
    * 1、我们本项目目前真正可以做到认证授权功能的模块只有qd-mall-uaa模块，有资源服务器的配置，而其他模块都没有做相关配置，所以不会对相应的请求做拦截判断是否需要认证授权
    * 2、如何看待本项目：
        * 如果我们将本项目统一看成一个客户端项目，我们使用网关做统一的路由、拦截验证分发各个模块请求，我们只需要在网关模块配置一个资源服务器进行安全认证即可。
        * 如果我们把我们本项目将每个微服务单独作为一个客户端，那我们就不再网关处做资源服务器配置，而是具体到每一个微服务模块（qd-mall-business下的每个模块，包括认证模块）进行配置资源服务器。         
        * 所以就是一个客户端大小的问题，当你以终端为界限，那web端、手机端app等终端将是客户端，每个对应终端的项目做配置就行了，如果你把客户端按微服务应用程序application来划分，那就对每个为服务模块做配置。
    * 3、资源服务器配置在网关和 具体的每个应用程序模块的总结：
        * 如果将资源服务器配置在网关，我们可以做统一的鉴权操作，这样每个应用程序彼此互相内部调用，不会出现鉴权操作。相对来讲降低了调用复杂度，但是公共化必然导致没有个性化
        * 如果将资源服务器配置在各个应用程序模块，可以针对每个应用程序实现各自需要的鉴权操作，如果内部互相调用，我们可以实现feign的拦截器，将认证信息token放在请求头中进行传递。 
        
* 实现认证授权的测试案例：
    * 不需要校验的请求 oauthController 的资源服务器已经做了配置，不认证授权即可访问 （PermitProperties在此类配置和yml文件）
    * 需要校验的请求 testController 过滤配置请求没有此类请求，所以 需要在请求头中添加 access-token 信息
    ````
    # 放在请求头中：
    Authorization 
    Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6bnVsbCwiZXhwIjoxNTg3MDQzNjEyLCJYLUFPSE8tVXNlcklkIjoxLCJqdGkiOiI2OWVmYjg5OS1jNzc5LTQ5MDktOGZmOC1lODhjZDY5M2FhNjIiLCJjbGllbnRfaWQiOiJ6bHQifQ.PuoEa-1c0G5Crk3FPq3r2XPiRvaPwqB-nmaqxT2ZGwdY1xvFPI9_-6DCG0i_4QCQHlnmjpRwV5aX_POV33TNNukpXD0JRlxIeo05h25Kj8q6JsHRmo1RNrx2KbCyh3xx668tE201Rtgnc2JwPG-GPXK0Oz7QYoj-FuidloHm91yaDrjcACxs-63TjQivefzWeN5JdA6fpIiLe_o9BiHPMdhZNCJcIDh33s7wo81Xws-9_UBhgY0oUyHTP7Of12v_RJPnWsbQXfdyaIAuE74XZNN8pRJXAIsFcw_aD_AF8pNu0__wAzq1R250a4Tx_Bgpe93PGgeFAxvD_MJ-TADAwQ
    
   # 或者将access_token 拼在请求url后面：
    http://localhost:9002/sys-user/queryAll?access_token=eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6bnVsbCwiZXhwIjoxNTg3MzA4MzQ4LCJYLUFPSE8tVXNlcklkIjoxLCJqdGkiOiJjNTg2ZGEzNi02ZDMxLTQzY2YtODkyYy1lMTIxOTI3NWZkOWQiLCJjbGllbnRfaWQiOiJ6bHQifQ.VN8Uya3lCyd9_ew1lY8weueFghmqW2nQIwSgnUk1M6NkwYXBlAPE4_aBUNFNbah8CPsG4RwOZSq53XRSTHPLNE5Z2mzm9w6oCiGKzVyaQS2_DdyOxhsLPUkFo0ICiSp2ze6RWbw_kzwDJmpVkwgKgccFWwAWdRLr6xGrkT9kFgKAQ78HvbleLVJbx_ftWD7TQfTzHCU8uP0otN6C9dILjV6_vQMRF64ZyzezJ6KHXWsAD3NF7-FdimcxTtHPDamehjXAoUFtKk6Z6wTQXVfZxY5E4bZy6QeDpYC-Kj-aOy2EpEmyGIElCw2plHv0ktISsoub8glrOD7XDsFYgfxjFg
  
    # 如果不加token请求，就会提示无权访问：
    {
        "error": "unauthorized",
        "error_description": "Full authentication is required to access this resource"
    }
    # 因为本项目对资源服务器的安全认证配置做了相关异常自定义处理，所以会报我们的错 
    {
      "header": {
          "code": "401",
          "message": "暂未登录或token已经过期"
      },
      "body": "Full authentication is required to access this resource"
    }
    ````
  
* 获取jwt---token的几个controller测试：(配置token的生成存储方式为jwt)
    * 请求头里 写client_id:app  client_secret:app 
    * 请求body {请求入参} 用户名密码 admin admin
      ````
      {
      	"username":"admin",
      	"password":"admin"
      }
      ````
    * clientId 和 clientSecret 是在数据库 oauth_client_details中配置的
    ````
    # 响应结果：自定义的CustomJwtAccessTokenConverter--->JwtAccessTokenConverter的enhance
    {
        "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6bnVsbCwiZXhwIjoxNTg3NDAxOTgxLCJYLUFPSE8tVXNlcklkIjoxLCJqdGkiOiI4OTQzYjAyMy1jNDY1LTRkOWUtYWFmYS0zZjFjODYwY2MzODAiLCJjbGllbnRfaWQiOiJ6bHQifQ.GeJD8CtPsBPPdfX9y2LfGJjDTRMDH_QloTOYFAJkpGyZggBCS3VUxp7aRd1M7Ayw0Rgcu87bkZwvot5hsmN3zmgAs5uqrM2cgZBKPvtl-F3TorTc0DAiU-uz1HAmRQn85MyZ1f_UBNIKeHhxGQddDa7wvyLlcBRHTMkZpC3fgYMHoUiBAkzFn55QqAZgvSnwSs7hZh1MPtXxs-qE4IOMYoy-HaOXATTKXiYI2Hu27Ix3bdsUq3c-KLGPlk_XQxat_CFNSte5XJa-qUBLF-jc0WI5IZCu6fNEh2NGM1r3WurWW1bhmRWdER6SoFdyJigKiQESt4NvJhp10n-UhXsv5g",
        "token_type": "bearer",
        "refresh_token": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJyb2xlcyI6bnVsbCwiYXRpIjoiODk0M2IwMjMtYzQ2NS00ZDllLWFhZmEtM2YxYzg2MGNjMzgwIiwiZXhwIjoxNTg3NDEyNzUyLCJYLUFPSE8tVXNlcklkIjoxLCJqdGkiOiI1MjBhNWEwZC02ZmVjLTQzNjgtOTZjMC1iNGU3YjI5OWQwZjIiLCJjbGllbnRfaWQiOiJ6bHQifQ.E-6mvjQucmYIRymHTN4k4d_5BPhEW2DjwbEdqiTcUw861peyLD4fo9Lo6zmqcSILoONYiyPbQSwyM8psT-jfg388UXQL2xCcgeciHEfAz6fAEuwXVjz7HzX3D1pcP0r6nZupb5HzfJHOTEuCab_xb_1j8UNFRtX-qH1AAO3B-DTLAJ4a3DXHwYlBs7PcrdeZNlyEPFW5mzlRpAIc8sz8BhMkearNWlpHR0DubG0x3SzYzXBmr95Wwhn56GsK-3zzCjkDxeG8_neCJHKsTke6TkAwQuteztp-ye0LPJgAcC0gOWKVn0p6nw71P0E4u29G3pLO77DedHPHKh79ERqxUA",
        "expires_in": 17479,
        "scope": "all",
        "X-AOHO-UserId": 1,
        "roles": null,
        "jti": "8943b023-c465-4d9e-aafa-3f1c860cc380",
        "X-AOHO-ClientId": null
    }
    # 底层默认的JwtAccessTokenConverter的enhance (没有追加用户身份信息)
    {
        "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODc0MDU1NjgsInVzZXJfbmFtZSI6ImFkbWluIiwianRpIjoiYTBkMzM0NGYtYzI5MS00NDEwLTg4ZTItN2Q5N2Q5NjhhOGY5IiwiY2xpZW50X2lkIjoiemx0Iiwic2NvcGUiOlsiYWxsIl19.hrxVtCaQB5XyayLqntVo6sQQ7lA54pOn8yu52ClD3wI",
        "token_type": "bearer",
        "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1ODc0MTYzNjgsInVzZXJfbmFtZSI6ImFkbWluIiwianRpIjoiNTBjOWE3ZjQtZmNkMS00MTgzLWE3OGUtOThkNTgwZWQxNjY2IiwiY2xpZW50X2lkIjoiemx0Iiwic2NvcGUiOlsiYWxsIl0sImF0aSI6ImEwZDMzNDRmLWMyOTEtNDQxMC04OGUyLTdkOTdkOTY4YThmOSJ9.OGTDtiw-8FXk5TawZfQxUrht0DqPAwl4phNzqwXTDKQ",
        "expires_in": 17967,
        "scope": "all",
        "jti": "a0d3344f-c291-4410-88e2-7d97d968a8f9"
    }
    解析：这里的access_token DefaultTokenServices的createAccessToken（）方法生成的uuid：8943b023-c465-4d9e-aafa-3f1c860cc380 就是jti，但是我们自行实现了jwt token的生成转换：CustomJwtAccessTokenConverter
    所以我们会添加额外信息（用户验证信息）到token的生成结果中，调用父类的JwtAccessTokenConverter的enhance() ,有一段代码，result.setValue(this.encode(result, authentication));会将token和用户认证信息authentication
    一起进行jwt的加密，
    jwt 3个组成部分：Header（头部） Payload（有效载荷） Signature（签名） 结构：xxx.yyy.zzz 就像现在的access_token
    
    jwt的优点：无状态、安全 
    jwt的缺点：JWT的最大缺点是服务器不保存会话状态，所以在使用期间不可能取消令牌或更改令牌的权限。
    也就是说，一旦JWT签发，在有效期内将会一直有效。
    为了减少盗用和窃取，JWT的有效期不宜设置太长，JWT不建议使用HTTP协议来传输代码，而是使用加密的HTTPS协议进行传输。
    
    使用方法：
    客户端接收服务器返回的JWT，将其存储在Cookie或localStorage中。
    此后，客户端将在与服务器交互中都会带JWT。如果将它存储在Cookie中，就可以自动发送，但是不会跨域，因此一般是将它放入HTTP请求的Header Authorization字段中。
    Authorization: Bearer xxx.yyy.zzz
    当跨域时，也可以将JWT被放置于POST请求的数据主体中。
    ````
* 测试配置token存储在redis：请求方式同上
    ````
    # 修改yml配置文件：
    mall:
      oauth2:
        token:
          store:
            type: redis
    # 响应结果：不是jwt，可以查看redis，已经存入token了 有9个文件
    {
        "access_token": "40ac7daa-3b6c-43cd-8d7d-878c0ea6dfd1",
        "token_type": "bearer",
        "refresh_token": "44643d56-8b8d-4fb0-bd4f-624c156697e9",
        "expires_in": 17999,
        "scope": "all"
    }
    # 同样携带token请求需要认证的资源（可以放在请求头Authorization bearer 281293sc-219321jci-2312c，可以放在表单access_token）
    
    # jwt是不能移除token的，因为底层remove方法没有处理逻辑，因为没有存储，而redis存储token，所以调用移除token接口，是直接可以从redis删除token
    可以调用我们的logout登出方法测试。查看redis变化
    
    # 当我们移除token后，再去那被移除的token调用资源时，就会报错， InvalidTokenException 这个异常进行处理，并打印日志
    {
        "error": "invalid_token",
        "error_description": "40ac7daa-3b6c-43cd-8d7d-878c0ea6dfd1"
    }  
    因为check_token端点会来判断token的合法性，即调用CustomRedisTokenStore的readAccessToken()来判断传入的token是否存在。
    CustomRedisTokenStore 这个类就是我们实现的redis 对token的存储 移除 读取等操作的集合类
    ```` 
    * 存储token在redis
    ![image](https://github.com/17661977890/qd-mall/blob/master/image/WechatIMG62.png)
    * 从redis移除token
    ![image](https://github.com/17661977890/qd-mall/blob/master/image/WechatIMG63.png)
    
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
    * 基于jwt ： 底层不存储，方法是空的，如果生成的token是jwt 则服务器不会保存，其他都会保存到数据库、redis等等
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

* token 的颁发生成管理(AuthorizationServerTokenServices接口：失效时间读客户端详情数据库的配置，每个客户端目前都是5H)：

* token的获取测试：（grant_type 不同模式认证传参不同）
    * （1） **自定义模式**：
        * 调用controller的3个方法，密码模式获取token （用户名密码、手机号密码、openId，注意请求头带client_id 和 client_secret两个参数)
        
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
 
* 其他模块使用qd-mall-uaa的认证鉴权功能：以用户中心为列
    *  在用户模块，增加资源服务器配置类，将认证无服务器远程调用的请求放开，否则认证服务器feign调用会失败，服务调用异常
    *  在配置文件增加如下配置：
    ````
    # 建立资源服务和认证服务器的绑定，注意client-id和secret一定要和认证服务器生成token时使用的clientid是一致的，否则401
    # 另外如果这里不配置，就会报错 无效token
    # pom依赖对应的是spring-cloud-starter-oauth2 如果不导入此依赖，这里配置会黄色，就无效
    security:
      oauth2:
        client:
          client-id: zlt
          client-secret: zlt
          access-token-uri: http://localhost:8088/oauth/token
          user-authorization-uri: http://localhost:8088/oauth/authorize
        resource:
          token-info-uri: http://localhost:8088/oauth/check_token
    ````
    * 后期优化： 
        * （1）将此依赖 spring-cloud-starter-oauth2 和一些认证鉴权的相关独立配置抽出来作为一个公共starter模块包提供依赖服务
    进而可以将用户资源服务器做相关配置优化
        * （2）对feign 做相关拦截配置，增加Authorization access-token的相关请求头等配置，进而在认证服务器调用用户服务查询时不会被拦截。


* 自定义provider 认证方式：
    * 添加自定义provider 得方式有两种：
    * （1） 通过 AuthenticationManagerBuilder 进行添加， 就是WebSecurityConfiguration 的configureGlobal 配置方法中调用 auth.authenticationProvider(new myProvider()) 记得给provider set属性 注入的XXXDetailService
    * （2） 通过 HttpSecurity 进行添加， 配置一个configure 配置类，调用 authenticationProvider(new myProvider（))方法,记得给provider set属性 注入的XXXDetailService
    我们本代码中采用的第二种方式。第二种也是最后调用第一种方式的AuthenticationManagerBuilder 的authenticationProvider方法进行添加provider。
    
    * 我们目前通过 重写3个类来实现：
    * （1） SmsAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>  重写config方法，在里面调用  http.authenticationProvider(provider);
    * （2）自定义 SmsAuthenticationProvider implements AuthenticationProvider 重写 authenticate 和 support方法 --- support 用于只当认证token 和 provider 的一一对应
    * （3）自定义认证token：  SmsAuthenticationToken extends AbstractAuthenticationToken
    最后需要在 WebSecurityConfiguration的 configure方法中 apply 我们自定义的SmsAuthenticationSecurityConfig 才可以生效
    或者 我们只是自定义 SmsAuthenticationProvider 和 SmsAuthenticationToken ，直接在 WebSecurityConfiguration的 configure方法中 追加auth.authenticationProvider(注入的provider)
    
    相关源码解读SecurityConfigurer 和 SecurityBuilder ：http://www.tianshouzhi.com/api/tutorials/spring_security_4/264
    
#### (六)、quartz定时调度框架的集成：

* 当前项目集成的是旧版 quartz （springboot1.x） 也可以集成springboot2.x之后的新版（starter依赖），在spring-security-oauth2 项目中集成案例
    ````
    # 旧版-需要配置quartz-properties 配置文件和configuration配置类
    dependency>
        <groupId>org.quartz-scheduler</groupId>
        <artifactId>quartz</artifactId>
    </dependency>
  
    # 新版 只需要将配置写在yml文件即可，不需要增加配置类
    <!--springboot 2.0 之后集成了quartz starter-->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-quartz</artifactId>
    </dependency>
  # quartz 在springboot2.0 之后配置在yml
  spring：  
    quartz:
      #相关属性配置
      properties:
        org:
          quartz:
            scheduler:
              instanceName: clusteredScheduler
              instanceId: AUTO
            jobStore:
              class: org.quartz.impl.jdbcjobstore.JobStoreTX
              driverDelegateClass: org.quartz.impl.jdbcjobstore.StdJDBCDelegate
              tablePrefix: QRTZ_
              isClustered: true
              clusterCheckinInterval: 10000
              useProperties: false
            threadPool:
              class: org.quartz.simpl.SimpleThreadPool
              threadCount: 10
              threadPriority: 5
              threadsInheritContextClassLoaderOfInitializingThread: true
    ````
* 官方github： 数据库脚本：https://github.com/quartz-scheduler/quartz/tree/master/quartz-core/src/main/resources/org/quartz/impl/jdbcjobstore
* 当前项目集成位置在user-center-business/quartz包下

* 在commons 模块新增配置quartz模块，基于springboot2.0的封装，使用方法如下：
    * 业务类pom依赖坐标
    ````
    <!--基于springboot2.x 的quartz定时调度框架封装-->
    <dependency>
        <groupId>com.qidian.mall</groupId>
        <artifactId>qd-mall-quartz-config</artifactId>
    </dependency>
    ````
    * 业务类的yml文件 中配置,引入集成quartz的配置
    ````
    spring:
      profiles:
        include: quartz
    ````
    * 所有的定时任务处理逻辑方法，以后就可以写在此配置模块下job-taskexecute包下，按业务类型区分，不用在业务模块增加
    * 业务模块方法中直接调用QuartzJobManager工具类的新增定时任务等方法 即可测试。

### 多线程异步 @Async 和 定时任务线程池 schedule 的引入：

* 异步任务的使用：
    * 直接在需要做异步任务的方法加 @Async 注解接口，注意不可同类调用，否则无效，aop动态代理的原因 和事务注解一样。 
    * 在需要的业务模块 yml文件中 配置线程池的配置参数，这里我们可以以用户中心模块为参考。----asyncController
    * 配合 程序计数器的CountDownLatch 的使用。
* 定时任务线程池的使用：
    * 可以做一下简单定时任务的处理，本项目因为引入了更强大的定时调度框架，所以这里没有在写相关的定时处理等配置类，有需要可以网上百度
* 多线程异常处理：
    * 利用异步方法返回值，延迟处理异常，手动回滚事务。

### spring cloud gateway 动态路由网关的引入：
* 官网：https://cloud.spring.io/spring-cloud-gateway/reference/html/
* 相关功能实现：https://blog.csdn.net/squirrelanimal0922/article/details/90517946
* 依赖：
    ````
    # springcloud gateway作为SpringCloud官方推出的第二代网关框架，取代了Zuul网关。
     <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-gateway</artifactId>
    </dependency>
    
    # 注意事项：Spring Cloud Gateway需要Spring Boot和Spring Webflux提供的Netty的运行时环境，因此，它不可以打包成war包，也不可以在传统的Servlet容器（比如tomcat）中运行。所以SpringCloudGateway项目中不能依赖 spring-boot-starter-web ，要不然会报错。
  
    # 此文网关模块 不引入base 和 swagger模块，并且把spring-boot-starter-web依赖从父pom剔除，各个业务模块需要自行引入
    ````
* 底层实现：底层基于netty传输层，而不是http应用层，作为流量入口，所以效率更高。
* 功能：动态路由转发、限流控制、权限校验、过滤器、断路器hystrix
    * 1、动态路由配置：gateway配置路由主要有两种方式，1.用yml配置文件，2.写在代码里@Bean注入。此文在yml配置
    * 2、全局过滤器配置---待做

### druid 数据源连接池
* （用户中心模块）监控访问地址：localhost:9002/druid/login.html

### feign 客户端调用 + hystrix

* 说明：
    * 在Spring Cloud微服务体系下，微服务之间的互相调用可以通过Feign进行声明式调用，在这个服务调用过程中Feign会通过Ribbon从服务注册中心获取目标微服务的服务器地址列表，之后在网络请求的过程中Ribbon就会将请求以负载均衡的方式打到微服务的不同实例上，从而实现Spring Cloud微服务架构中最为关键的功能即服务发现及客户端负载均衡调用。
    * 另一方面微服务在互相调用的过程中，为了防止某个微服务的故障消耗掉整个系统所有微服务的连接资源，所以在实施微服务调用的过程中我们会要求在调用方实施针对被调用微服务的熔断逻辑。而要实现这个逻辑场景在Spring Cloud微服务框架下我们是通过Hystrix这个框架来实现的。
    * 调用方会针对被调用微服务设置调用超时时间，一旦超时就会进入熔断逻辑，而这个故障指标信息也会返回给Hystrix组件，Hystrix组件会根据熔断情况判断被调微服务的故障情况从而打开熔断器，之后所有针对该微服务的请求就会直接进入熔断逻辑，直到被调微服务故障恢复，Hystrix断路器关闭为止。


* 在Spring Cloud中使用Feign进行微服务调用分为两层：Hystrix的调用和Ribbon的调用，Feign自身的配置会被覆盖。
    ![image](https://img-blog.csdnimg.cn/20191012221343851.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2NyYXp5bWFrZXJjaXJjbGU=,size_16,color_FFFFFF,t_70)
    
    
* feign 接口层面的熔断降级处理 hystrix

* 配置相关：
    * 谁调用，谁开启feign支持，以及配置feign拦截器和hystrix的相关隔离策略和超时配置等（配置文件）
    * feign 相关
    ````
      feign:
        #替换掉JDK默认HttpURLConnection实现的 Http Client
        httpclient:
          enabled: true
        hystrix:
          enabled: true
        client:
          config:
            default:
             #连接超时时间
              connectTimeout: 5000
             #读取超时时间
              readTimeout: 5000
    ````
  
    * hystrix 相关
    ````
      hystrix:
        propagate:
          request-attribute:
            enabled: true
        command:
          #全局默认配置
          default:
            #线程隔离相关
            execution:
              timeout:
                #是否给方法执行设置超时时间，默认为true。一般我们不要改。
                enabled: true
              isolation:
                #配置请求隔离的方式，这里是默认的线程池方式。还有一种信号量的方式semaphore，使用比较少。
                strategy: threadPool
                thread:
                  #方式执行的超时时间，默认为1000毫秒，在实际场景中需要根据情况设置
                  timeoutInMilliseconds: 10000
                  #发生超时时是否中断方法的执行，默认值为true。不要改。
                  interruptOnTimeout: true
                  #是否在方法执行被取消时中断方法，默认值为false。没有实际意义，默认就好！
                  interruptOnCancel: false
        circuitBreaker:   #熔断器相关配置
          enabled: true   #是否启动熔断器，默认为true，false表示不要引入Hystrix。
          requestVolumeThreshold: 20     #启用熔断器功能窗口时间内的最小请求数，假设我们设置的窗口时间为10秒，
          sleepWindowInMilliseconds: 5000    #所以此配置的作用是指定熔断器打开后多长时间内允许一次请求尝试执行，官方默认配置为5秒。
          errorThresholdPercentage: 50   #窗口时间内超过50%的请求失败后就会打开熔断器将后续请求快速失败掉,默认配置为50
    ````
    
    * ribbon 相关
    ````
    ribbon:
      # 饥饿加载(并指定那个feign服务端需要饥饿加载) ---- 注意：饥饿加载配置一定要指定那个客户端，否则无效
      eager-load:
        enabled: true
        clients: qd-mall-messageserver
      #说明：同一台实例的最大自动重试次数，默认为1次，不包括首次
      MaxAutoRetries: 1
      #说明：要重试的下一个实例的最大数量，默认为1，不包括第一次被调用的实例
      MaxAutoRetriesNextServer: 1
      #说明：是否所有的操作都重试，默认为true
      OkToRetryOnAllOperations: true
      #说明：从注册中心刷新服务器列表信息的时间间隔，默认为2000毫秒，即2秒
      ServerListRefreshInterval: 2000
      #说明：使用Apache HttpClient连接超时时间，单位为毫秒
      ConnectTimeout: 3000
      #说明：使用Apache HttpClient读取的超时时间，单位为毫秒
      ReadTimeout: 3000
  
       # 饥饿加载的生效日志，如果配置生效，启动完成后就会打印下边的日志，如果没有生效，需要调用的时候才会打印！！
       org.springframework.context.support.PostProcessorRegistrationDelegate$BeanPostProcessorChecker Bean 'configurationPropertiesRebinderAutoConfiguration' of type [org.springframework.cloud.autoconfigure.ConfigurationPropertiesRebinderAutoConfiguration$$EnhancerBySpringCGLIB$$15acfde5] is not eligible for getting processed by all BeanPostProcessors (for example: not eligible for auto-proxying)
       com.netflix.config.ChainedDynamicProperty Flipping property: qd-mall-usercenter.ribbon.ActiveConnectionsLimit to use NEXT property: niws.loadbalancer.availabilityFilteringRule.activeConnectionsLimit = 2147483647
       com.netflix.loadbalancer.BaseLoadBalancer Client: qd-mall-usercenter instantiated a LoadBalancer: DynamicServerListLoadBalancer:{NFLoadBalancer:name=qd-mall-usercenter,current list of Servers=[],Load balancer stats=Zone stats: {},Server stats: []}ServerList:null
       com.netflix.loadbalancer.DynamicServerListLoadBalancer Using serverListUpdater PollingServerListUpdater
    ````
    
    * 关于feign的超时配置，
        * 发现问题：uaa模块首次请求用户feign接口，总是会超时触发熔断，
        * 原因分析：是因为ribbon的懒加载，导致的，因为熔断超时时间默认1s，所以会出现超过1s的情况，就会触发熔断，服务调用异常，如果没有配置熔断，则会导致超时等报错。
        * 解决办法：就是配置熔断和ribbon的超时时间配置或者开启ribbon的饥饿加载。
        
        * 如果开启了Hystrix，那么Ribbon的超时时间配置与Hystrix的超时时间配置则存在依赖关系，因为涉及到Ribbon的重试机制，所以一般情况下都是Ribbon的超时时间小于Hystrix的超时时间
            * Ribbon重试次数(包含首次)= 1 + ribbon.MaxAutoRetries + ribbon.MaxAutoRetriesNextServer + (ribbon.MaxAutoRetries * ribbon.MaxAutoRetriesNextServer)
            * Hystrix的超时时间=Ribbon的重试次数(包含首次) * (ribbon.ReadTimeout + ribbon.ConnectTimeout)
        * 如果不启用Hystrix，Feign的超时时间则是Ribbon的超时时间，Feign自身的配置也会被覆盖。

* 关于feign拦截器的使用：
    * 使用背景： 我们之前都是外部调用web-controller接口，需要认证，但是有时候我们需要服务之间的内部调用也需要认证，所以我们将资源服务器配置在了每一个服务模块（如用户服务、文件服务、认证服务）
    但是，服务内部调用我们使用feign远程调用，不像controller层一样，可以由前端或者调用方将token配置在请求头或者请求body中来实现鉴权。
    关于feign，框架给我们提供了响应的拦截器，可以传递外部服务携带的token给到被调用方。
    * 举例说明：
    ````
    # uaa 认证模块 获取用户信息的接口 /api/oauth2/user/info ，如果这个接口也是需要鉴权的，就需要传递access_token,我们在这个接口中通过feign
    远程调用了 user-center 用户模块的 根据用户名查询用户feign接口 queryByUsername，这个接口也是需要鉴权的。如果不传递token的话，会鉴权不通过的。401
    所以我们在feign公共配置模块 增加了feign拦截器，将token信息，封装到feign调用的请求头中Authorization bearer 。。。。
    这样 我们就可以成功的通过feign 传递token的方式来实现需要鉴权的接口的远程调用。
  
    # 我们实现拦截的方法用两种，本项目暂时使用第二种，需要在启动了添加 @Import(FeignInterceptorConfig.class) 使拦截器生效。（谁调用谁配置）
    ````
    * **问题整理：**
        * （1） feign 拦截器 不能传递token
            * 因为我们为feign接口的调用，提供了熔断机制hystrix，而熔断机制的隔离策略（线程隔离thread 和 信号量隔离 semaphore）对feign拦截器有不同的影响。
            * feign的默认熔断隔离策略是线程隔离策略，这种隔离策略会导致 安全上下文 和请求上下文 用到threadlocal 置为null，即不能进行请求信息和token的传递。
            * 解决办法：1、关闭熔断：feign.enable.hystrix:false 2、配置信号量隔离  （都是在调用方配置） 
        * （2） 关于调用feign的api接口，直接走熔断！！！
            * 1、配置不完善，因为超时配置问题造成（hystrix 超时小于ribbon，或者因为懒加载默认超时1s，首次熔断）
            * 2、客户端调用方启动类 开启feign的注解扫描没有写basePackages====>正确写法，多个逗号分隔  @EnableFeignClients(basePackages = "com.qidian.mall.message")
                * 原因解释：如果使用了springcloud的openFeign,在application上需要添加@EnableFeignClients注解，如果没有明确指明basePackages的路劲，采用@EnableFeignClients注解的类所在的包作为basePackages。 
                因为我们的项目是微服务，调用的的feign接口再其他服务模块，如果没有只当basePackages，则spring ioc不会自动为外部引入的其他服务jar包里,标注了@FeignClient注解的interface自动生成bean对象
    
    * 关于hystrix隔离策略带来的问题，以及后期优化： 
        * 1、信号量隔离策略虽然开销小（资源浪费较低），但是对于下游服务是在同一线程中处理，同步操作，多个服务调用耗时会累计，性能较差 ？？？
        * 2、线程隔离策略，下游服务隔离，异步执行，互不影响，但是上下文不能从调用线程进行传递。这样就导致我们内部服务调用的鉴权操作会受影响。寻找其他替代方案？？？
        * 3、信号量隔离策略在hystrix 1.4.0后支持超时配置。之前不可以
        
* 关于feign接口的请求问题：
    * 默认不支持文件上传和表单请求。https://www.cnblogs.com/yangzhilong/p/11714620.html

### hystrix 熔断机制详解： ---- 后期优化 sentinel代替
* 隔离策略：https://www.jianshu.com/p/b8d21248c9b1、https://www.jianshu.com/p/dc0410558fc9
    ````
    线程池隔离：
      1、调用线程和hystrixCommand线程不是同一个线程，并发请求数受到线程池（不是容器tomcat的线程池，而是hystrixCommand所属于线程组的线程池）中的线程数限制，默认是10。
      2、这个是默认的隔离机制
      3、hystrixCommand线程无法获取到调用线程中的ThreadLocal中的值
   信号量隔离：
      1、调用线程和hystrixCommand线程是同一个线程，默认最大并发请求数是10
      2、调用速度快，开销小，由于和调用线程是处于同一个线程，所以必须确保调用的微服务可用性足够高并且返回快才用
    ````
* 超时配置：在feign接口的调用方，配置超时
* 配置完后，重启调用方和被调用方，才会生效
````
# 如果开启熔断 需要配置信号量隔离，默认的线程隔离，会使feign拦截器 不能获取请求上下文和安全上下文的信息（hystrixCommand线程无法获取到调用线程中的ThreadLocal中的值)
# hystrix 1.4.0 后支持信号量隔离的超时配置。通过增大熔断超时时间，避免请求（打断点）时间过长，直接熔断。
hystrix:
  command:
    default:
      execution:
        isolation:
          strategy: SEMAPHORE
          thread:
            timeoutInMilliseconds: 5000
````

### oss 云存储整合
* 其余相关api 参考，控制台开发者指南

### rocketMq 消息队列整合

## es 搜索引擎单机整合
* pom依赖 版本替换默认版本 注意在父pom做统一版本管理， 对照es服务端 为es 7.6.1版本
* 测试类包含相关基本crud api
* 完成仿京东 高级搜索实战--数据爬虫批量添加至索引库，高级搜索，高亮搜索等
* es相关环境配置在linux 虚拟机
* 相关请求拦截日志打印
  
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

* **maven相关**： 
  * 项目打包问题：父模块pom都不配置plugin 打包插件，以及接口依赖模块也不需要配置，只是在有启动类的模块配置即可，所以在有打包插件（spring-boot-plugin-maven）的地方都要配置如下：（如果接口或公共依赖模块也配置了：只是配置 groupid和artifactid 两个标签就行，或者加一个exec的配置）
  可以参考user-center模块的配置形式（有注释）
  ````
  <plugin>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-maven-plugin</artifactId>
      <!--打包时候，不加下面的配置，执行会报错 没有主清单属性-->
      <configuration>
          <mainClass>${start-class}</mainClass>
          <layout>ZIP</layout>
      </configuration>
      <executions>
          <execution>
              <goals>
                  <!--这个是默认 goal，在 mvn package 执行之后，这个命令再次打包生成可执行的 jar，
                  同时将 mvn package jps生成的 jar 重命名为 *.origin；-->
                  <goal>repackage</goal>
              </goals>
              <!--指定这个会生成一个exec的jar 可执行，另外的jar不能执行，只能被依赖-->
              <!--                   <configuration>-->
              <!--                      <classifier>exec</classifier>-->
              <!--                    </configuration>-->
          </execution>
      </executions>
  </plugin>
  ````
  * maven 打包插件有很多种，自行百度，我这里都是直接用的springboot打包插件。会将本地项目和依赖的jar都进行打包。
  * 本项目如果没有配置执行信息（goal：repackage）和 mainClass ，那么打出来的包，只能被依赖，而不会被执行，执行会报错没有主清单属性。
  * repackage 功能的 作用，就是在打包的时候，多做一点额外的事情：
    * 首先 mvn package 命令 对项目进行打包，打成一个 jar，这个 jar 就是一个普通的 jar，可以被其他项目依赖，但是不可以被执行
    * repackage 命令，对第一步 打包成的 jar 进行再次打包，将之打成一个 可执行 jar ，通过将第一步打成的 jar 重命名为 *.original 文件
  
  * 因为该项目不是继承spring boot 即顶层父pom中没有parent 是springboot，所以需要配置 repackage 和 mainClass，如果顶层pom是继承的spring-boot-parent话，可以不用配置两者，默认是repackage。
  
  
  * 关于指定maven编译的版本：三种方式：
    ````
        # 1 使用内置的属性配置
    	<properties>
    		<!-- 文件拷贝时的编码 -->
    		<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    		<project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    		<!-- 编译时的编码 -->
    		<maven.compiler.encoding>UTF-8</maven.compiler.encoding>
     
    		<maven.compiler.source>1.8</maven.compiler.source>
    		<maven.compiler.target>1.8</maven.compiler.target>
    		<maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
     
    		<java.version>1.8</java.version>
    	</properties>
        # 2 使用插件maven-compiler-plugin
    	<build>
    		<plugins>
    			<!-- java编译插件， 编译Java代码 -->
    			<plugin>
    				<groupId>org.apache.maven.plugins</groupId>
    				<artifactId>maven-compiler-plugin</artifactId>
    				<version>3.2</version><!--$NO-MVN-MAN-VER$ -->
    				<configuration>
    					<source>1.8</source>
    					<target>1.8</target>
    					<encoding>UTF-8</encoding><!-- 指定编码格式，否则在DOS下运行mvn compile命令时会出现莫名的错误，因为系统默认使用GBK编码 -->
    				</configuration>
    			</plugin>
     
    			<!-- 资源文件拷贝插件，处理资源文件 -->
    			<plugin>
    				<groupId>org.apache.maven.plugins</groupId>
    				<artifactId>maven-resources-plugin</artifactId>
    				<version>3.0.1</version><!--$NO-MVN-MAN-VER$ -->
    				<configuration>
    					<encoding>UTF-8</encoding><!-- 指定编码格式，否则在DOS下运行mvn命令时当发生文件资源copy时将使用系统默认使用GBK编码 -->
    				</configuration>
    			</plugin>
    		</plugins>
    	</build>
        # 3、本地maven setting.xml 配置 或者 写在顶层pom中
    	<profile>    
            <id>jdk-1.8</id>    
             <activation>    
                  <activeByDefault>true</activeByDefault>    
                  <jdk>1.8</jdk>    
              </activation> 
           
                <properties>    
                    <maven.compiler.source>1.8</maven.compiler.source>    
                    <maven.compiler.target>1.8</maven.compiler.target>    
                    <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>    
                </properties>   
         
        </profile>
    ````
    * 项目部署--deploy： 顶层pom配置私服仓库（发行版和快照版），修改本地maven setting.xml配置，即可打包部署
        * 可以用命令执行 mvn deploy -Dmaven.test.skip=true (跳过测试)
    * 项目-->本地-->私服-->官方远程仓库，按照这个流程下载坐标依赖，顶层pom新增配置代理仓库，以kaptcha jar为例，本地依赖坐标，
    直接从远程仓库（我们本地配置的是阿里云）是拉不到的没有，这时候需要我们手动下载jar 地址：https://mvnrepository.com/artifact/com.google.code.kaptcha/kaptcha/2.3
        * 命令上传第三方jar到私服：
        ````
        # 如第三方JAR包：kaptcha-2.3.jar Dfile 本地下载jar的所在路径 Durl 私服发行仓库 DrepositoryId maven server的私服id
        mvn deploy:deploy-file -DgroupId=com.google.code.kaptcha -DartifactId=kaptcha -Dversion=2.3 -Dpackaging=jar -Dfile=/Users/minyoung/Downloads/kaptcha-2.3.jar -Durl=http://47.103.18.65:8081/repository/maven-releases/ -DrepositoryId=nexus-releases
        ````
        * 配置pom后刷新maven 获取私服的jar，如果不行，或者打包报错无权，那就要配置nexus的匿名用户访问。

* 关于基于雪花算法的id 生成器的使用： 
    * id生成为Long 类型，传给其前端使用 数值范围超出js的最大范围，所以前端显示和后端给到的 不一致。
    * 问题解决： 全局配置将 id 转换为字符串传给前端，保证数据无误。

### 认证鉴权实现结构：
   * uaa 作为认证服务和资源服务
   * 每个业务微服务模块，作为单独的资源服务模块，配置资源服务器，请求需要提供认证信息（token）来通过鉴权
   * 外部服务调用鉴权，传递token即可
   * 内部服务调用鉴权，通过feign拦截器传递token ，但是会收到hystrix的熔断隔离策略的影响，
     信号量策略超时配置，在hystrix 1.4.0之后也支持了！！！！
   
   * 为什么将资源服务器的鉴权配置配置在每个微服务模块，而不是统一在网关配置：
       * 1、如果只是在网关配置，那么内部服务之间的访问或者不走网关直接请求微服务模块，不需要鉴权就可以访问，因为外部请求统一走网关也是约定的而已。
       * 2、我们希望，内部微服务之间调用也需要有鉴权的过程，从而也避免出现直接避开网关而直接请求业务微服务模块而导致不需要鉴权的情况
       * 3、我们只希望网关做服务转发和角色权限的访问校验， 
   * 弊端：每个服务都做鉴权资源配置，问题： 服务之前toekn的传递（通过feign 拦截器）受熔断的隔离策略影响。信号量隔离性能比线程隔离并发性能差。
   
   
   
