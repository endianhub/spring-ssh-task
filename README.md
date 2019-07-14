## 定时任务

1. 定时任务
2. 异常码/异常处理器/统一异常处理
3. AOP 日志
4. AOP自定义注解
5. 操作结果集,返回数据


**异常：**

> 异常码 <br>
> 异常处理，对ajax类型的异常返回ajax错误，避免页面问题 <br>
> 异常处理器 <br>
> 自定义异常类 <br>


**工具类：**

> 数据数据校验 <br>
> 对象类型转换 <br>
> 常量工具类 <br>
> 日期格式化 <br>
> Redis <br>
> 日志管理 <br>
> Spring工具类 <br>
> 防止XSS攻击 <br>
> 任务池 <br>
> WEB工具 <br>


**Base：**

> dao <br>
> service <br>
> controller <br>



**@Around环绕有两种操作方式：**
一种有return返回值的:return point.proceed(); <br>
一种没有返回值的：Object obj = point.proceed(); obj中会有最终返回的参数



<br>
<hr>
<br>

### 技术更新：

Spring5.0以后不再提供Log4jConfigListener类，而是要引入 log4j-api、log4j-core、log4j-web 三个类。

**pom.xml**

    <dependency>
    	<groupId>org.apache.logging.log4j</groupId>
    	<artifactId>log4j-api</artifactId>
    	<version>2.11.1</version>
    </dependency>
    <dependency>
    	<groupId>org.apache.logging.log4j</groupId>
    	<artifactId>log4j-core</artifactId>
    	<version>2.11.1</version>
    </dependency>
    <dependency>
    	<groupId>org.apache.logging.log4j</groupId>
    	<artifactId>log4j-web</artifactId>
    	<version>2.11.1</version>
    </dependency>


**web.xml**

    <!-- log4j配置，context-param可以省略不配，因为默认就会在src/main/resources找这个文件，所以不需要额外配置引用-->
    <context-param>
    	<param-name>Log4jServletContextLocation</param-name>
    	<param-value>classpath:log4j2.xml</param-value>
    </context-param>
    
    <listener>
    	<listener-class>org.apache.logging.log4j.web.Log4jServletContextListener</listener-class>
    </listener>
    
    <filter>
    	<filter-name>log4jServletFilter</filter-name>
    	<filter-class>org.apache.logging.log4j.web.Log4jServletFilter</filter-class>
    </filter>
    <filter-mapping>
    	<filter-name>log4jServletFilter</filter-name>
    	<url-pattern>/*</url-pattern>
    	<dispatcher>REQUEST</dispatcher>
    	<dispatcher>FORWARD</dispatcher>
    	<dispatcher>INCLUDE</dispatcher>
    	<dispatcher>ERROR</dispatcher>
    </filter-mapping>


相比以前配置如下：

    <context-param>
    	<param-name>log4jConfigLocation</param-name>
    	<param-value>classpath:config/log4j.properties</param-value>
    </context-param>
    <listener>
    	<listener-class>org.springframework.web.util.Log4jConfigListener</listener-class>
    </listener>

**log4j2.xml**

    <?xml version="1.0" encoding="UTF-8"?>
    <!--日志级别以及优先级排序: OFF > FATAL > ERROR > WARN > INFO > DEBUG > TRACE > ALL -->
    <!--Configuration后面的status，这个用于设置log4j2自身内部的信息输出，可以不设置，当设置成trace时，你会看到log4j2内部各种详细输出 -->
    <!--monitorInterval：Log4j能够自动检测修改配置 文件和重新配置本身，设置间隔秒数 -->
    <configuration status="INFO" monitorInterval="30">
    	<!--先定义所有的appender -->
    	<appenders>
    		<!--这个输出控制台的配置 -->
    		<console name="Console" target="SYSTEM_OUT">
    			<!--输出日志的格式 -->
    			<PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n" />
    		</console>
    		<!--文件会打印出所有信息，这个log每次运行程序会自动清空，由append属性决定，这个也挺有用的，适合临时测试用 -->
    		<File name="log" fileName="D:/log/test.log" append="false">
    			<PatternLayout pattern="%d{HH:mm:ss.SSS} %-5level %class{36} %L %M - %msg%xEx%n" />
    		</File>
    
    		<!-- 这个会打印出所有的info及以下级别的信息，每次大小超过size，则这size大小的日志会自动存入按年份-月份建立的文件夹下面并进行压缩，作为存档 -->
    		<RollingFile name="RollingFileInfo" fileName="D:/logs/info.log" filePattern="D:/logs/$${date:yyyy-MM}/info-%d{yyyy-MM-dd}-%i.log">
    			<!--控制台只输出level及以上级别的信息（onMatch），其他的直接拒绝（onMismatch） -->
    			<ThresholdFilter level="info" onMatch="ACCEPT" onMismatch="DENY" />
    			<PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n" />
    			<Policies>
    				<TimeBasedTriggeringPolicy />
    				<SizeBasedTriggeringPolicy size="100 MB" />
    			</Policies>
    		</RollingFile>
    
    		<RollingFile name="RollingFileWarn" fileName="D:/logs/warn.log" filePattern="D:/logs/$${date:yyyy-MM}/warn-%d{yyyy-MM-dd}-%i.log">
    			<ThresholdFilter level="warn" onMatch="ACCEPT" onMismatch="DENY" />
    			<PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n" />
    			<Policies>
    				<TimeBasedTriggeringPolicy />
    				<SizeBasedTriggeringPolicy size="100 MB" />
    			</Policies>
    			<!-- DefaultRolloverStrategy属性如不设置，则默认为最多同一文件夹下7个文件，这里设置了20 -->
    			<DefaultRolloverStrategy max="20" />
    		</RollingFile>
    
    		<RollingFile name="RollingFileError" fileName="D:/logs/error.log" filePattern="D:/logs/$${date:yyyy-MM}/error-%d{yyyy-MM-dd}-%i.log">
    			<ThresholdFilter level="error" onMatch="ACCEPT" onMismatch="DENY" />
    			<PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n" />
    			<Policies>
    				<TimeBasedTriggeringPolicy />
    				<SizeBasedTriggeringPolicy size="100 MB" />
    			</Policies>
    		</RollingFile>
    	</appenders>
    	<!--然后定义logger，只有定义了logger并引入的appender，appender才会生效 -->
    	<loggers>
    		<!--过滤掉spring和mybatis的一些无用的DEBUG信息 -->
    		<logger name="org.springframework" level="ERROR"></logger>
    		<logger name="org.mybatis" level="ERROR"></logger>
    		<root level="all">
    			<appender-ref ref="Console" />
    			<!-- <appender-ref ref="RollingFileInfo" /> -->
    			<!-- <appender-ref ref="RollingFileWarn" /> -->
    			<!-- <appender-ref ref="RollingFileError" /> -->
    		</root>
    	</loggers>
    </configuration>


<br>
<hr>
<br>

### 遇到问题及解决方案：

**问题1：** 
commons/global.jsp" 中的行 3 上发生错误: JSP 2.1 或更高版本支持属性 "trimDirectiveWhitespaces"。<%@ include file="/commons/global.jsp"%>

**解决方案：**
jsp中会经常使用到使用jsp标签和jstl的标签，比如<%@ page …%>, <%@ taglib …%>, <c:forEach…%>, 尤其是循环标签，在jsp最终输出的html中会产生大量的空行，使得性能降低。

**方法1：**（所有的jsp页面）在web.xml 中添加以下设置：

    <jsp-config>
	    	<jsp-property-group>
	    		<url-pattern>*.jsp</url-pattern>
	    		<trim-directive-whitespaces>true</trim-directive-whitespaces>
	    	</jsp-property-group>
    </jsp-config>

**方法2：在单个的jsp中添加**

    <%@ page trimDirectiveWhitespaces="true"%>

.tomcat 目录下 \conf\web.xml文件，在jsp servlet增加参数。 <br>
1.升级tomcat至6.0以上版本 <br>
2.Tomcat 5.5.x+，不要使用trimDirectiveWhitespaces，改用这种方法：
在Tomcat安装目录/conf/web.xml中找到名叫"jsp"的servlet，添加下面一段代码：

    <init-param>
	    	<param-name>trimSpaces</param-name>
	    	<param-value>true</param-value>
    </init-param>

查看JSP版本可以使用下面的命令：

    JSP version: <%= JspFactory.getDefaultFactory().getEngineInfo().getSpecificationVersion() %>

**方法：servlet**

tomcat 目录下 \conf\web.xml文件，在jsp servlet增加参数

    <init-param>
	    	<param-name>trimSpaces</param-name>
	    	<param-value>true</param-value>
    </init-param>

<br>

**问题2：** 
运行时出现：`java.lang.ClassNotFoundException: org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter`

**解决方案**
Spring5.0以后负责对了 Bean 进行加工处理的类做了变动，废弃了以前所用的AnnotationMethodHandlerAdapter，而使用最新的RequestMappingHandlerAdapter

废弃的   `org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter`
最新使用 `org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter`
    
