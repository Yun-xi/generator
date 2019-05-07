##项目说明
该项目旨在让开发者告别大量的基础CRUD，让开发者把精力集中在业务逻辑上。

##使用说明
1. 需要修改generator.properties中的配置信息
    + package: 项目的groupId，正常情况是不需要改的
    + moduleName：项目的artifactId
    + domainName：模块的包名
    + author： 作者
    + email：邮箱 
2. 打开localhost:8082，在swagger上的生成代码处输入tableSchema（库名）和tableName（表名）
3. 下载，拷贝到项目中的文件路径即可

##可能遇到的问题
1. 当库名和表名匹配不到相应的表时，会报java.lang.NullPointerException: null
