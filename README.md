**什么是启动器？**

其实就是一个对项目中一些需要初始化的逻辑进行统一管理调度的工具类库，它可以通过简单的配置就能实现启动项的加载顺序以及是否延迟初始化或者在子线程中进行初始化，而且本身支持模块化的设计，能够最大程度做到项目解耦。

**启动器能做什么？**

启动器主要作用有以下两点：

一、支持设置「启动优先级」、「是否同步初始化」、「是否懒初始化」操作，对初始化项进行分类，提高app启动速度；

二、支持模块化，每个模块只关注于本模块的业务，本模块中修改初始化项无需对其他模块或壳工程进行修改；

**如何使用启动器？**

第一步，引入项目依赖，starter_version = "1.0.0-rc"。

在Java模块中：

```groovy
android {
  defaultConfig{
    javaCompileOptions {
      annotationProcessorOptions {
         arguments = [STARTER_MODULE_NAME: project.getName()]
      }
    }
  }
}

dependencies {
  	implementation "com.wangkh.moduler:XStarter:$starter_version"
		annotationProcessor "com.wangkh.moduler:XStarter-compiler:$starter_version"
}
```

在Kotlin模块中：

```groovy
apply plugin: 'kotlin-kapt'
kapt {
    arguments {
        arg("STARTER_MODULE_NAME", project.getName())
    }
}

dependencies {
		implementation "com.wangkh.moduler:XStarter:$starter_version"
    kapt "com.wangkh.moduler:XStarter-compiler:$starter_version"
}
```

第二步，在Application中进行注册。

```kotlin
XStarter.isDebug = true // 是否为测试模式
XStarter.emit(DemoApplication.instance) // 开启启动器
```

第三部，在需要初始化数据的模块中创建启动器类。

```kotlin
@Starter(mainProcessOnly = false)
class KotlinStarter : IStarter { // 类名可以任意命名
		@StarterMethod(priority = 99, isSync = false, isDelay = true)
  	public fun initTest(application:Application) { //方法名可以任意命名
				//TODO 执行模块中需要初始化的逻辑
    }

    @StarterFinish(listen = "initTest")
    public fun listenTest(e:Exception) { // 监听initTest方法是否执行完毕，如果不需要监听，可以不写listenTest方法
        Log.e("KotlinStarter", "test初始化完成")
    }
  
  	@StarterMethod(priority = 60, isSync = true, isDelay = true)
  	public fun initTest2() { //方法名可以任意命名
				//TODO 初始化另外一些东西，由于这个方法下初始化不需要用到Application，所以参数为空就行。
    }
}
```

只需要通过以上代码，项目在启动的时候就会自动的寻找**KotlinStarter**类并调用生命的初始化方法**initTest**，如果需要监听**initTest**是否已经执行完毕，可以定义一个监听方法**listenTest**（方法名可以任意命名），但是其注解@StarterFinish的参数listen中的字符串一定要与被监听的方法名保持一致即可。

**注解及参数说明**

```html
@Starter // 标注启动管理类

    mainProcessOnly -- 是否只在主进程初始化 true 只在主进程中初始化 false 所有进程都进行初始化

@StarterMethod  //标注启动方法，只能用到@Starter修饰的类中，否则无效。

    priority -- 该启动方法的优先级，[0-99],数值越大优先级越高，默认50；
    
    isSync -- 是否同步初始化（即在主线程中进行初始化操作），true 是 false 不是，即可以在子线程中进行初始化，默认true；

    isDelay -- 是否可以延迟初始化，true 是 false 不是，立即初始化，不延迟，默认false；

@StarterFinish //启动方法的监听方法，只能用到@Starter修饰的类中，否则无效。

    listen -- 要监听方法的方法名，必填参数；

```

**混淆配置**

```
​```
-keep public class com.wang.android.starter.executor.**{*;}
-keep public class com.wang.android.starter.manager.**{*;}
​```
```