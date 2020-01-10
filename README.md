# qd-mall
奇点商城--->旨在项目搭建以及电商系统架构设计学习和研究，以及相关技术栈的整合学习



### mybatis-plus代码生成
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