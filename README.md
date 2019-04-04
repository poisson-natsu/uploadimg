# uploadimg
upload image to remote file system

很简单的图片上传到远程服务的功能，图片保存到本地，数据库中只记录图片的名字，大小，类型等信息。
mysql使用的8.x
sql添加到目录中，uploadimg.sql


过程中遇到了一个很经典的问题，Autowired注入失败，找不到JdbcTemplate

Field jdbcTemplate in .......... required a bean of type '...........' that could not be found.
Consider defining a bean of type '..............' in your configuration.

然而网上的问题没有一个能解决的，最后经同事发现，原来gradle添加JdbcTemplate依赖时，应该使用如下：
    compile group: 'org.springframework.boot', name: 'spring-boot-starter-jdbc', version: '2.1.3.RELEASE'
	
而我添加的是：
    compile group: 'org.springframework', name: 'spring-boot-starter-jdbc', version: '5.1.5.RELEASE'
	
>_<

