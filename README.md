# qd-mall
* 奇点商城--->旨在项目搭建以及电商系统架构设计学习和研究，以及相关技术栈的整合学习

## 目前实现依赖功能：


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




#### LAST: 问题整理：

* springboot整合mybatis-plus 过程中启动项目报错，mapper注入失败，主要是配置问题：
    * 启动类加@MapperScan 扫描注解
    * 有没有依赖mybatis-plus-boot-starter （因为被封装在db-config 公共以来中，user-center直接依赖db-config就行）
    * yml配置文件