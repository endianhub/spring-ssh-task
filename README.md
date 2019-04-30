### 定时任务

定时任务 <br>

异常码/异常处理器/统一异常处理 <br>

AOP 日志 <br>

AOP自定义注解 <br>

操作结果集,返回数据 <br>

异常：
> 异常码 <br>
> 异常处理，对ajax类型的异常返回ajax错误，避免页面问题 <br>
> 异常处理器 <br>
> 自定义异常类 <br>


工具类： <br>
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


Base： <br>
> dao <br>
> service <br>
> controller <br>



@Around环绕有两种操作方式： <br>
一种有return返回值的:return point.proceed(); <br>
一种没有返回值的：Object obj = point.proceed(); obj中会有最终返回的参数