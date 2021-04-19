package com.wang.android.starter.utils;

public class Constants {

    // Generate
    public static final String SEPARATOR = "_";
    public static final String PROJECT = "STARTER";
    public static final String TAG = PROJECT + "::";
    public static final String WARNING_TIPS = "DO NOT EDIT THIS FILE!!! IT WAS GENERATED BY STARTER.";
    public static final String NAME_OF_EXECUTOR = PROJECT + SEPARATOR + "INDEX";
    public static final String NAME_OF_MANAGER = "MANAGER" + SEPARATOR;


    //启动器接口类相关
    public static final String STARTER_SUPER_CLASS_PATH = "com.wang.android.starter";
    public static final String PACKAGE_OF_GENERATE_EXECUTOR = STARTER_SUPER_CLASS_PATH + ".executor";
    public static final String PACKAGE_OF_GENERATE_MANAGER = STARTER_SUPER_CLASS_PATH + ".manager";


    public static final String STARTER_SUPER_CLASS_EXECUTOR = STARTER_SUPER_CLASS_PATH + ".IStarterExecutor";
    public static final String STARTER_SUPER_CLASS_WARP = STARTER_SUPER_CLASS_PATH + ".AbstractStarter";
    public static final String STARTER_SUPER_CLASS_MANAGER = STARTER_SUPER_CLASS_PATH + ".IManager";
    //IManager接口里面的方法名称
    public static final String STARTER_SUPER_CLASS_MANAGER_METHOD = "initExecutor";

    public static final String STARTER_SUPER_CLASS_MANAGER_GET_EXECUTOR = "getExecutor";


    public static final String BASE_STARTER_METHOD_EXECUTE = "execute";
    public static final String BASE_STARTER_METHOD_ON_FINISH = "onFinish";

    public static final String STARTER_INTERFACE_NAME = "com.wang.android.starter.IStarter";

    //注解字段名以及方法名（设计方法名称和注解字段相同）
    public static final String PARAM_PRIORITY_NAME = "priority";
    public static final String PARAM_IS_SYNC_NAME = "isSync";
    public static final String PARAM_IS_DELAY_NAME = "isDelay";


    // System interface
    public static final String APPLICATION = "android.app.Application";
    public static final String ACTIVITY = "android.app.Activity";
    public static final String FRAGMENT = "android.app.Fragment";
    public static final String FRAGMENT_V4 = "android.support.v4.app.Fragment";
    public static final String SERVICE = "android.app.Service";
    public static final String PARCELABLE = "android.os.Parcelable";

    public static final String ISTARTER = "com.wang.android.starter.IStarter";


    // Java type
    private static final String LANG = "java.lang";
    public static final String BYTE = LANG + ".Byte";
    public static final String SHORT = LANG + ".Short";
    public static final String INTEGER = LANG + ".Integer";
    public static final String LONG = LANG + ".Long";
    public static final String FLOAT = LANG + ".Float";
    public static final String DOUBEL = LANG + ".Double";
    public static final String BOOLEAN = LANG + ".Boolean";
    public static final String CHAR = LANG + ".Character";
    public static final String STRING = LANG + ".String";
    public static final String SERIALIZABLE = "java.io.Serializable";


    public static final String KEY_MODULE_NAME = "STARTER_MODULE_NAME";
    public static final String KEY_GENERATE_DOC_NAME = "STARTER_GENERATE_DOC";

    public static final String VALUE_ENABLE = "enable";


    // Log
    static final String PREFIX_OF_LOGGER = PROJECT + "::Compiler ";
    public static final String NO_MODULE_NAME_TIPS = "These no module name, at 'build.gradle', like :\n" +
            "android {\n" +
            "    defaultConfig {\n" +
            "        ...\n" +
            "        javaCompileOptions {\n" +
            "            annotationProcessorOptions {\n" +
            "                arguments = [STARTER_MODULE_NAME: project.getName()]\n" +
            "            }\n" +
            "        }\n" +
            "    }\n" +
            "}\n";


}
