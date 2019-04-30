### 定时任务

定时任务
异常码/异常处理器/统一异常处理
AOP 日志
AOP自定义注解
操作结果集,返回数据
异常：
	异常码
	异常处理，对ajax类型的异常返回ajax错误，避免页面问题
	异常处理器
	自定义异常类
工具类：
	数据数据校验
	对象类型转换
	常量工具类
	日期格式化
	Redis
	日志管理
	Spring工具类
	防止XSS攻击
	任务池
	WEB工具
Base：
	dao
	service
	controller



@Around环绕有两种操作方式：
一种有return返回值的:return point.proceed();
一种没有返回值的：Object obj = point.proceed(); obj中会有最终返回的参数