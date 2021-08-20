package com.rubin.rpan.util;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 反射工具类
 */
public class ReflectUtil {

    /**
     * 创建实体
     *
     * @param classPath
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static Object createInstance(String classPath) {
        Object o;
        try {
            Class clazz = Class.forName(classPath);
            o = clazz.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("the class " + classPath + " can not be found");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("load class " + classPath + " fail");
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("load class " + classPath + " fail");
        }
        return o;
    }

    /**
     * 创建类实例
     *
     * @param clazz
     * @return
     */
    public static Object createInstance(Class clazz) {
        Object o;
        try {
            o = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("load class " + clazz.getCanonicalName() + " fail");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("load class " + clazz.getCanonicalName() + " fail");
        }
        return o;
    }

    /**
     * 获取Class类
     *
     * @param classPath
     * @return
     */
    public static Class getClass(String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("the class " + classPath + "can not be found");
        }
    }

    /**
     * 注入属性值
     *
     * @param o
     * @param fieldName
     * @param fieldValue
     */
    public static void injectFieldValue(Object o, String fieldName, Object fieldValue) {
        try {
            Field field = o.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(o, fieldValue);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            throw new RuntimeException("there are no field named " + fieldName + " in class " + o.getClass().getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException("inject field value fail : " + fieldName);
        }
    }

    /**
     * set注入属性值
     *
     * @param o
     * @param fieldName
     * @param fieldValue
     */
    public static void setFieldValue(Object o, String fieldName, Object fieldValue) {
        Method[] declaredMethods = o.getClass().getDeclaredMethods();
        for (int i = 0; i < declaredMethods.length; i++) {
            if (declaredMethods[i].getName().equalsIgnoreCase("set" + fieldName)) {
                try {
                    declaredMethods[i].invoke(o, new Object[]{fieldValue});
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    throw new RuntimeException("set field value fail : " + fieldName);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    throw new RuntimeException("set field value fail : " + fieldName);
                }
            }
        }
    }

    /**
     * 获取接口所有实现类
     *
     * @param interfaceClass
     * @param baseScanPath
     * @return
     */
    public static Set<Class> getAllSubTypeOf(Class interfaceClass, String baseScanPath) {
        if (!interfaceClass.isInterface()) {
            throw new RuntimeException("the class must be interface");
        }
        Reflections reflections = new Reflections(baseScanPath);
        return reflections.getSubTypesOf(interfaceClass);
    }

    /**
     * 校验改类上面有没有该注解或者子注解
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static boolean hasAnnotationOrSubAnnotation(Class clazz, Class annotationClass) {
        if (clazz.isAnnotationPresent(annotationClass)) {
            return true;
        }
        Annotation[] annotations = clazz.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if ( annotations[i].annotationType().isAnnotationPresent(annotationClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取clazz的接口集合
     *
     * @param clazz
     * @return
     */
    public static List<Class> getInterfaces(Class clazz) {
        List<Class> interfaces = new ArrayList<>();
        Class[] interfaceArr = clazz.getInterfaces();
        if (interfaceArr != null && interfaceArr.length > 0) {
            interfaces = Arrays.asList(interfaceArr);
        }
        return interfaces;
    }

    /**
     * 获取子注解
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static Annotation getSubAnnotation(Class clazz, Class annotationClass) {
        Annotation[] annotations = clazz.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].annotationType().isAnnotationPresent(annotationClass)) {
                return annotations[i];
            }
        }
        throw new RuntimeException("there are no subType matched the annotation " + annotationClass.getCanonicalName());
    }

}
