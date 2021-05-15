package com.wang.android.starter.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.wang.android.starter.annotation.Starter;
import com.wang.android.starter.annotation.StarterFinish;
import com.wang.android.starter.annotation.StarterMethod;
import com.wang.android.starter.utils.Constants;

import org.apache.commons.collections4.CollectionUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;

@AutoService(Processor.class)
@SupportedAnnotationTypes({"com.wang.android.starter.annotation.Starter"})
public class StarterProcessor extends BaseProcessor {

    private int starterCounter = 0;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        starterCounter = 0;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        if (CollectionUtils.isNotEmpty(annotations)) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Starter.class);
            if (CollectionUtils.isNotEmpty(elements)) {
                try {
                    parseType(elements);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
    }

    private void parseType(Set<? extends Element> elements) throws IOException {
        for (Element element : elements) {
            TypeElement typeElement = (TypeElement) element;
            String className = typeElement.getQualifiedName().toString();

            int lastIndexOf = className.lastIndexOf(".");
            String simpleName = className.substring(lastIndexOf + 1, className.length());

            List<? extends TypeMirror> interfaces = typeElement.getInterfaces();
            if (interfaces == null) {
                logger.typeError(simpleName, "请实现IStart接口");
                return;
            }

            boolean checkInterfaceStatus = false;
            for (TypeMirror typeMirror : interfaces) {
                if (Constants.STARTER_INTERFACE_NAME.equals(typeMirror.toString())) {
                    checkInterfaceStatus = true;
                    break;
                }
            }

            if (!checkInterfaceStatus) {
                logger.typeError(simpleName, "请实现IStart接口");
                return;
            }

            List<String> executorPaths = new ArrayList<>();
            List<? extends Element> enclosedElements = element.getEnclosedElements();


            /**
             * key 指定要监听的初始化方法
             * value 监听方法本身的方法名
             */
            Map<String, String> listenMap = new HashMap<>();
            //先遍历一遍所有方法，拿出监听方法
            for (Element subElement : enclosedElements) {
                if (subElement.getKind() == ElementKind.METHOD) {
                    StarterFinish annotation = subElement.getAnnotation(StarterFinish.class);
                    if (annotation != null) {

                        Class<? extends Annotation> aClass = annotation.annotationType();
                        if (!subElement.getModifiers().contains(PUBLIC)) {
                            logger.methodError(simpleName, subElement.toString(), "注解「" + aClass.getSimpleName() + "」只能使用在public方法上");
                            return;
                        }

                        ExecutableElement method = (ExecutableElement) subElement;
                        List<? extends VariableElement> parameters = method.getParameters();
                        if (parameters != null && parameters.size() != 0) {

                            if (parameters.size() == 1) {
                                VariableElement variableElement = parameters.get(0);
                                TypeMirror typeMirror = variableElement.asType();
                                if (!typeMirror.toString().equals("java.lang.Exception")) {
                                    logger.methodError(simpleName, subElement.toString(), "注解「" + aClass.getSimpleName() + "」不支持java.lang.Exception以外的参数");
                                    return;
                                }
                            } else {
                                logger.methodError(simpleName, subElement.toString(), "注解「" + aClass.getSimpleName() + "」不支持java.lang.Exception以外的参数");
                                return;
                            }
                        }

                        String listen = annotation.listen();
                        listenMap.put(listen, subElement.toString());
                    }
                }
            }

            for (Element subElement : enclosedElements) {
                if (subElement.getKind() == ElementKind.CONSTRUCTOR) {
                    String methodName = subElement.toString();
                    if (!(methodName.equals(simpleName + "()"))) {
                        logger.typeError(simpleName, "不能为该类重写构造方法");
                        return;
                    }

                    if (!subElement.getModifiers().contains(PUBLIC)) {
                        logger.typeError(simpleName, "只支持public类型的构造方法");
                        return;
                    }
                } else if (subElement.getKind() == ElementKind.METHOD) {
                    StarterMethod starterMethod = subElement.getAnnotation(StarterMethod.class);
                    if (starterMethod != null) {
                        Class<? extends Annotation> aClass = starterMethod.annotationType();
                        if (!subElement.getModifiers().contains(PUBLIC)) {
                            logger.methodError(simpleName, subElement.toString(), "注解「" + aClass.getSimpleName() + "」只能使用在public方法上");
                            return;
                        }

                        ExecutableElement method = (ExecutableElement) subElement;
                        List<? extends VariableElement> parameters = method.getParameters();
                        if (parameters != null && parameters.size() != 0) {
                            if (parameters.size() == 1) {
                                VariableElement variableElement = parameters.get(0);
                                TypeMirror typeMirror = variableElement.asType();
                                if (!typeMirror.toString().equals(Constants.APPLICATION)) {
                                    logger.methodError(simpleName, subElement.toString(), "注解「" + aClass.getSimpleName() + "」不支持" + Constants.APPLICATION + "以外的参数");
                                    return;
                                }
                            } else {
                                logger.methodError(simpleName, subElement.toString(), "注解「" + aClass.getSimpleName() + "」不支持" + Constants.APPLICATION + "以外的参数");
                                return;
                            }
                        }

                        String methodSimpleName = subElement.toString().split("\\(")[0];
                        String listenerName = listenMap.get(methodSimpleName);
                        String fileAbsName = generateExecutor(typeElement, simpleName, subElement.toString(), starterMethod, listenerName);
                        executorPaths.add(fileAbsName);
                    }
                }

            }

            if (!executorPaths.isEmpty()) {
                Starter starterAnnotation = element.getAnnotation(Starter.class);
                boolean mainProcessOnly = starterAnnotation.mainProcessOnly();
                generateManager(typeElement, simpleName, executorPaths, mainProcessOnly);
            }
        }
    }

    private void generateManager(TypeElement typeElement, String simpleName, List<String> executorPaths, boolean mainProcessOnly) throws IOException {
        String fileName = Constants.NAME_OF_MANAGER + simpleName.toUpperCase() + Constants.SEPARATOR + moduleName.toUpperCase();
        if (mainProcessOnly) {
            fileName += Constants.SEPARATOR + Constants.MANAGER_FOR_MAIN_PROCESS;
        } else {
            fileName += Constants.SEPARATOR + Constants.MANAGER_FOR_ALL_PROCESS;
        }
        String collectionFieldName = "mExecutors";
        String iStarterFieldName = "m" + simpleName;

        //参数类型为List
        ClassName listTypeName = ClassName.get("java.util", "List");
        //List的泛型类型
        ClassName fieldTypeName = ClassName.get(Constants.STARTER_SUPER_CLASS_PATH, "AbstractStarter");
        //结合List与泛型
        ParameterizedTypeName parameterizedTypeName = ParameterizedTypeName.get(listTypeName, fieldTypeName);
        FieldSpec mExecutors = FieldSpec.builder(parameterizedTypeName, collectionFieldName, PRIVATE).build();

        TypeName originType = TypeName.get(typeElement.asType());
        FieldSpec originTypeField = FieldSpec.builder(originType, iStarterFieldName, PRIVATE).build();


        MethodSpec constructor = MethodSpec
                .constructorBuilder()
                .addModifiers(PUBLIC)
                .addCode("mExecutors = new java.util.ArrayList();\n")
                .addCode(iStarterFieldName + " = new " + simpleName + "();\n")
                .build();

        MethodSpec.Builder initExecutor = MethodSpec
                .methodBuilder(Constants.STARTER_SUPER_CLASS_MANAGER_METHOD)
                .addAnnotation(Override.class)
                .addModifiers(PUBLIC);


        for (String className : executorPaths) {
            String newExecutor = "new " + className + "(" + iStarterFieldName + ")";
            initExecutor.addCode(collectionFieldName + ".add(" + newExecutor + ");\n");
        }

        initExecutor
                .addCode("return " + collectionFieldName + ";\n")
                .returns(parameterizedTypeName);

        TypeSpec typeBuild = TypeSpec
                .classBuilder(fileName)
                .addJavadoc(Constants.WARNING_TIPS)
                .addModifiers(PUBLIC)
                .addSuperinterface(ClassName.get(elementUtils.getTypeElement(Constants.STARTER_SUPER_CLASS_MANAGER)))
                .addField(mExecutors)
                .addField(originTypeField)
                .addMethod(constructor)
                .addMethod(initExecutor.build())
                .build();

        JavaFile.builder(Constants.PACKAGE_OF_GENERATE_MANAGER, typeBuild).build().writeTo(mFiler);
    }

    private String generateExecutor(TypeElement typeElement, String simpleName, String methodName, StarterMethod annotation, String listenerName) throws IOException {
        Map<String, Object> paramsMap = new HashMap<>();

        int priority = annotation.priority();
        priority = Math.min(99, priority);
        priority = Math.max(0, priority);

        paramsMap.put(Constants.PARAM_PRIORITY_NAME, priority);
        paramsMap.put(Constants.PARAM_IS_SYNC_NAME, annotation.isSync());
        paramsMap.put(Constants.PARAM_IS_DELAY_NAME, annotation.isDelay());
        return generateExecutorWithParams(typeElement, simpleName, methodName, paramsMap, listenerName);
    }

    private String generateExecutorWithParams(TypeElement typeElement, String simpleName, String methodName, Map<String, Object> paramsMap, String listenerName) throws IOException {
        String fileName = Constants.NAME_OF_EXECUTOR + starterCounter + Constants.SEPARATOR + moduleName.toUpperCase();

        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(fileName)
                .addJavadoc(Constants.WARNING_TIPS)
                .superclass(ClassName.get(elementUtils.getTypeElement(Constants.STARTER_SUPER_CLASS_WARP)))
                .addModifiers(PUBLIC);

        TypeName filedTypeName = TypeName.get(typeElement.asType());
        String fileNameString = "m" + simpleName;
        MethodSpec constructorMethod = MethodSpec
                .constructorBuilder()
                .addParameter(filedTypeName, fileNameString)
                .addModifiers(PUBLIC)
                .addCode("this." + fileNameString + " = " + fileNameString + ";\n")
                .build();
        typeBuilder.addMethod(constructorMethod);

        FieldSpec typeField = FieldSpec.builder(filedTypeName, fileNameString, Modifier.PRIVATE).build();
        typeBuilder.addField(typeField);

        for (String key : paramsMap.keySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec
                    .methodBuilder(key)
                    .addModifiers(PUBLIC)
                    .addAnnotation(Override.class);

            if (key.equals(Constants.PARAM_PRIORITY_NAME)) {
                methodBuilder.returns(TypeName.INT);
            } else if (key.equals(Constants.PARAM_IS_SYNC_NAME)) {
                methodBuilder.returns(TypeName.BOOLEAN);
            } else if (key.equals(Constants.PARAM_IS_DELAY_NAME)) {
                methodBuilder.returns(TypeName.BOOLEAN);
            }
            methodBuilder.addCode("return " + paramsMap.get(key) + ";");
            typeBuilder.addMethod(methodBuilder.build());
        }

        MethodSpec.Builder executeMethodBuilder = MethodSpec
                .methodBuilder(Constants.BASE_STARTER_METHOD_EXECUTE)
                .addModifiers(PUBLIC)
                .addParameter(ClassName.get("android.app", "Application"), "application")
                .addAnnotation(Override.class);

        if (methodName.contains(Constants.APPLICATION)) {
            String replace = methodName.replace("(" + Constants.APPLICATION + ")", "");
            executeMethodBuilder.addCode(fileNameString + "." + replace + "(application);\n");
        } else {
            executeMethodBuilder.addCode(fileNameString + "." + methodName + ";\n");
        }

        MethodSpec.Builder onFinishMethodBuilder = MethodSpec.methodBuilder(Constants.BASE_STARTER_METHOD_ON_FINISH)
                .addModifiers(PUBLIC)
                .addParameter(ClassName.get("java.lang", "Exception"), "exception");


        //如果有配置的监听方法
        if (listenerName != null) {
            if (listenerName.contains("(java.lang.Exception)")) {
                String replace = listenerName.replace("(java.lang.Exception)", "");
                onFinishMethodBuilder.addCode(fileNameString + "." + replace + "(exception);\n");
            } else {
                onFinishMethodBuilder.addCode(fileNameString + "." + listenerName + ";\n");
            }
        }


        typeBuilder.addMethod(executeMethodBuilder.build());
        typeBuilder.addMethod(onFinishMethodBuilder.build());

        JavaFile.builder(Constants.PACKAGE_OF_GENERATE_EXECUTOR, typeBuilder.build()).build().writeTo(mFiler);
        starterCounter++;

        return Constants.PACKAGE_OF_GENERATE_EXECUTOR + "." + fileName;
    }
}
