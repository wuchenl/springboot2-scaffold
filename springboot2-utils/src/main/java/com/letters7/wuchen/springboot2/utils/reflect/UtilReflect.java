package com.letters7.wuchen.springboot2.utils.reflect;


import com.letters7.wuchen.springboot2.utils.clazz.UtilClassLoader;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.*;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * ReflectUtils
 *
 * @author zoubin02
 */
public final class UtilReflect {

    private static final ConcurrentMap<String, Class<?>> NAME_CLASS_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Method> NAME_METHODS_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, Map<String, Method>> READ_METHODS_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Map<String, Method>> WRITE_METHODS_CACHE = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, Map<String, Field>> FIELD_CACHE = new ConcurrentHashMap<>();

    private final static Map<Class, Object> primitiveValueMap = new ConcurrentHashMap<Class, Object>(16);

    private UtilReflect() {
    }

    /**
     * 改变private/protected的方法为public，尽量不调用实际改动的语句，避免JDK的SecurityManager抱怨。
     */
    public static void makeAccessible(Method method) {
        if ((!Modifier.isPublic(method.getModifiers()) || !Modifier.isPublic(method.getDeclaringClass().getModifiers()))
                && !method.isAccessible()) {
            method.setAccessible(true);
        }
    }

    //循环向上转型,获取对象的DeclaredField.
    public static void makeAccessible(final Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }


    /**
     * 反射获取bean的属性值集合
     */
    public static Map<String, Object> getFieldValues(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Map<String, Field> declaredFields = getBeanFields(obj.getClass());
        if (declaredFields != null && !declaredFields.isEmpty()) {
            Iterator<String> iterator = declaredFields.keySet().iterator();
            while (iterator.hasNext()) {
                String fieldName = iterator.next();
                map.put(fieldName, getFieldValue(obj, fieldName));
            }
        }
        return map;
    }

    /**
     * 直接读取对象属性值,无视private/protected修饰符,不经过getter函数.
     */
    public static Object getFieldValue(final Object object, final String fieldName) {
        Object result = null;
        try {
            Field field = getDeclaredField(object, fieldName);
            if (field == null) {
                //throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
                return null;
            }
            makeAccessible(field);
            result = field.get(object);
        } catch (IllegalAccessException e) {
        }
        return result;
    }

    private static Field getDeclaredField(final Object object, final String fieldName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
            }
        }
        return null;
    }

    /**
     * 直接设置对象属性值,无视private/protected修饰符,不经过setter函数.
     */
    public static void setFieldValue(final Object object, final String fieldName, final Object value) {
        try {
            Field field = getDeclaredField(object, fieldName);
            if (field == null) {
                throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
            }
            makeAccessible(field);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    /**
     * 递归获取类及父类所有的属性集合
     */
    public static Map<String, Field> getBeanFields(Class cl) {
        String className = descClass(cl);
        if (FIELD_CACHE.containsKey(className)) {
            return FIELD_CACHE.get(className);
        }
        Map<String, Field> properties = new HashMap<String, Field>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isTransient(field.getModifiers())
                        || Modifier.isStatic(field.getModifiers())) {
                    continue;
                }
                field.setAccessible(true);
                properties.put(field.getName(), field);
            }
        }
        FIELD_CACHE.put(className, properties);
        return properties;
    }

    //找出指定的属性
    public static Field getBeanField(Class<?> clazz, String property) {
        String className = descClass(clazz);
        if (!FIELD_CACHE.containsKey(className)) {
            getBeanFields(clazz);
        }
        Map<String, Field> fieldMap = FIELD_CACHE.get(className);
        if (fieldMap == null) {
            return null;
        }
        return fieldMap.get(property);
    }

    //找出所有的读方法
    public static Map<String, Method> getBeanReadMethods(Class cl) {
        String className = descClass(cl);
        if (READ_METHODS_CACHE.containsKey(className)) {
            return READ_METHODS_CACHE.get(className);
        }

        Map<String, Method> properties = new HashMap<String, Method>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Method[] methods = cl.getDeclaredMethods();
            for (Method method : methods) {
                if (isPropertyReadMethod(method)) {
                    method.setAccessible(true);
                    String property = getPropertyNameFromReadMethod(method);
                    properties.put(property, method);
                }
            }
        }
        READ_METHODS_CACHE.put(className, properties);
        return properties;
    }

    //找出指定属性的读方法
    public static Method getBeanReadMethod(Class<?> clazz, String property) {
        String className = descClass(clazz);
        if (!READ_METHODS_CACHE.containsKey(className)) {
            getBeanReadMethods(clazz);
        }
        Map<String, Method> mmap = READ_METHODS_CACHE.get(className);
        if (mmap == null) {
            return null;
        }
        return mmap.get(property);
    }

    //找出所有的写方法
    public static Map<String, Method> getBeanWritedMethods(Class cl) {
        String className = descClass(cl);
        if (WRITE_METHODS_CACHE.containsKey(className)) {
            return WRITE_METHODS_CACHE.get(className);
        }

        Map<String, Method> properties = new HashMap<String, Method>();
        for (; cl != null; cl = cl.getSuperclass()) {
            Method[] methods = cl.getDeclaredMethods();
            for (Method method : methods) {
                if (isPropertyWriteMethod(method)) {
                    method.setAccessible(true);
                    String property = getPropertyNameFromWriteMethod(method);
                    properties.put(property, method);
                }
            }
        }
        WRITE_METHODS_CACHE.put(className, properties);
        return properties;
    }

    //找出指定属性的写方法
    public static Method getBeanWriteMethod(Class<?> clazz, String property) {
        String className = descClass(clazz);
        if (!WRITE_METHODS_CACHE.containsKey(className)) {
            getBeanWritedMethods(clazz);
        }
        Map<String, Method> mmap = WRITE_METHODS_CACHE.get(className);
        if (mmap == null) {
            return null;
        }
        return mmap.get(property);
    }

    /**
     * 找出指定方法:
     * 循环向上转型, 获取对象的DeclaredMethod,并强制设置为可访问.
     * 如向上转型到Object仍无法找到, 返回null.
     * 匹配函数名+参数类型。
     * <p>
     * 用于方法需要被多次调用的情况. 先使用本函数先取得Method,然后调用Method.invoke(Object obj, Object... args)
     */
    public static Method findMethodByMethodName(final Object obj, final String methodName, final Class<?>... parameterTypes) {
        Objects.requireNonNull(obj, "object can't be null");
        Objects.requireNonNull(methodName, "methodName can't be blank");
        for (Class<?> searchType = obj.getClass(); searchType != Object.class; searchType = searchType.getSuperclass()) {
            try {
                Method method = searchType.getDeclaredMethod(methodName, parameterTypes);
                makeAccessible(method);
                return method;
            } catch (NoSuchMethodException e) {
                // Method不在当前类定义,继续向上转型
                continue;// new add
            }
        }
        return null;
    }

    //找出指定方法
    public static Method findMethodByMethodName(Class<?> clazz, String methodName)
            throws NoSuchMethodException, ClassNotFoundException {
        return findMethodByMethodSignature(clazz, methodName, null);
    }

    public static Method findMethodByMethodSignature(Class<?> clazz, String methodName, String... parameterTypes)
            throws NoSuchMethodException, ClassNotFoundException {
        String signature = methodName;
        if (parameterTypes != null && parameterTypes.length > 0) {
            signature = methodName + Stringjoin(parameterTypes);
        }
        Method method = NAME_METHODS_CACHE.get(signature);
        if (method != null) {
            return method;
        }
        if (parameterTypes == null) {
            List<Method> finded = new ArrayList<Method>();
            for (Method m : clazz.getMethods()) {
                if (m.getName().equals(methodName)) {
                    finded.add(m);
                }
            }
            if (finded.isEmpty()) {
                throw new NoSuchMethodException("No such method " + methodName + " in class " + clazz);
            }
            if (finded.size() > 1) {
                String msg = String.format("Not unique method for method name(%s) in class(%s), find %d methods.",
                        methodName, clazz.getName(), finded.size());
                throw new IllegalStateException(msg);
            }
            method = finded.get(0);
        } else {
            Class<?>[] types = new Class<?>[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                types[i] = name2class(parameterTypes[i]);
            }
            method = clazz.getMethod(methodName, types);

        }
        NAME_METHODS_CACHE.put(signature, method);
        return method;
    }

    private static String Stringjoin(String[] array) {
        if (array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String s : array) {
            sb.append(s);
        }
        return sb.toString();
    }

    //找出构造函数
    public static Constructor<?> findConstructor(Class<?> clazz, Class<?> paramType) throws NoSuchMethodException {
        Constructor<?> targetConstructor;
        try {
            targetConstructor = clazz.getConstructor(new Class<?>[]{paramType});
        } catch (NoSuchMethodException e) {
            targetConstructor = null;
            Constructor<?>[] constructors = clazz.getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (Modifier.isPublic(constructor.getModifiers())
                        && constructor.getParameterTypes().length == 1
                        && constructor.getParameterTypes()[0].isAssignableFrom(paramType)) {
                    targetConstructor = constructor;
                    break;
                }
            }
            if (targetConstructor == null) {
                throw e;
            }
        }
        return targetConstructor;
    }

    //从读方法中获取属性名称
    public static String getPropertyNameFromReadMethod(Method method) {
        if (isPropertyReadMethod(method)) {
            if (method.getName().startsWith("get")) {
                return method.getName().substring(3, 4).toLowerCase()
                        + method.getName().substring(4);
            }
            if (method.getName().startsWith("is")) {
                return method.getName().substring(2, 3).toLowerCase()
                        + method.getName().substring(3);
            }
        }
        return null;
    }

    //是否读方法
    public static boolean isPropertyReadMethod(Method method) {
        return method != null
                && Modifier.isPublic(method.getModifiers())
                && !Modifier.isStatic(method.getModifiers())
                && method.getReturnType() != void.class
                && method.getDeclaringClass() != Object.class
                && method.getParameterTypes().length == 0
                && ((method.getName().startsWith("get") && method.getName().length() > 3)
                || (method.getName().startsWith("is") && method.getName().length() > 2));
    }

    //从写方法中获取属性名称
    public static String getPropertyNameFromWriteMethod(Method method) {
        if (isPropertyWriteMethod(method)) {
            return method.getName().substring(3, 4).toLowerCase()
                    + method.getName().substring(4);
        }
        return null;
    }

    //是否写方法
    public static boolean isPropertyWriteMethod(Method method) {
        return method != null
                && Modifier.isPublic(method.getModifiers())
                && !Modifier.isStatic(method.getModifiers())
                && method.getDeclaringClass() != Object.class
                && method.getParameterTypes().length == 1
                && method.getName().startsWith("set")
                && method.getName().length() > 3;
    }


    //属性转换为Map
    public static Map<String, PropertyDescriptor> getPropertyDescriptorsAsMap(Class clazz) {
        Set<PropertyDescriptor> pds = getPropertyDescriptors(clazz);
        Map<String, PropertyDescriptor> maps = new HashMap<>();
        if (pds == null || pds.isEmpty()) {
            return maps;
        }
        for (PropertyDescriptor pd : pds) {
            maps.put(pd.getName(), pd);
        }
        return maps;
    }

    public static Set<PropertyDescriptor> getPropertyDescriptors(Class clazz) {
        Set<PropertyDescriptor> sets = new HashSet();
        //c.isAssignableFrom(List.class);  //判断c是否是List的父类
        if (List.class.isAssignableFrom(clazz) || Set.class.isAssignableFrom(clazz) || Map.class.isAssignableFrom(clazz)) {
            return sets;
        }

        BeanInfo beanInfo = null;
        try {
            //如果不想从父类中也获取父类的属性那么使用如下:
            //Introspector.getBeanInfo(beanClass, stopClass)
            //stopClass代表不用从父类获取
            beanInfo = Introspector.getBeanInfo(clazz, Object.class);
        } catch (IntrospectionException e) {
            return sets;
        }

        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        if (descriptors == null) {
            return sets;
        }

        for (PropertyDescriptor pd : descriptors) {
            if (!pd.getName().equalsIgnoreCase("class")) {
                sets.add(pd);
            }
            //新的规范中大量的set方法返回的是return this,而这种写法并不被标准的Introspector所支持，这里做了特殊处理
            if (pd.getWriteMethod() == null) {
                try {
                    Method writeMethod = getBeanWriteMethod(clazz, pd.getName());
                    if (writeMethod != null) {
                        pd.setWriteMethod(writeMethod);
                    }
                } catch (IntrospectionException e) {
                }
            }
        }
        return sets;
    }

    /**
     * 特殊处理，允许通过带参数的constructor创建对象
     */
    public static Object newInstance(Class type) {
        Constructor _constructor = null;
        Object[] _constructorArgs = new Object[0];
        try {
            _constructor = type.getConstructor(new Class[]{});// 先尝试默认的空构造函数
        } catch (NoSuchMethodException e) {
            // ignore
        }

        if (_constructor == null) {// 没有默认的构造函数，尝试别的带参数的函数
            Constructor[] constructors = type.getConstructors();
            if (constructors.length == 0) {
                throw new UnsupportedOperationException("Class[" + type.getName() + "] has no public constructors");
            }
            _constructor = constructors[0];// 默认取第一个参数
            Class[] params = _constructor.getParameterTypes();
            _constructorArgs = new Object[params.length];
            for (int i = 0; i < params.length; i++) {
                _constructorArgs[i] = getDefaultValue(params[i]);
            }
        }

        return org.springframework.cglib.core.ReflectUtils.newInstance(_constructor, _constructorArgs);
    }

    /**
     * 根据class类型返回默认值值
     */
    public static Object getDefaultValue(Class cl) {
        if (cl.isArray()) {// 处理数组
            return Array.newInstance(cl.getComponentType(), 0);
        } else if (cl.isPrimitive() || primitiveValueMap.containsKey(cl)) { // 处理原型
            return primitiveValueMap.get(cl);
        } else {
            return newInstance(cl);
            // return null;
        }
    }


    //获取包装类
    public static Class<?> getBoxedClass(Class<?> c) {
        if (c == int.class) {
            c = Integer.class;
        } else if (c == boolean.class) {
            c = Boolean.class;
        } else if (c == long.class) {
            c = Long.class;
        } else if (c == float.class) {
            c = Float.class;
        } else if (c == double.class) {
            c = Double.class;
        } else if (c == char.class) {
            c = Character.class;
        } else if (c == byte.class) {
            c = Byte.class;
        } else if (c == short.class) {
            c = Short.class;
        }
        return c;
    }

    public static boolean isPrimitives(Class<?> cls) {
        if (cls.isArray()) {
            return isPrimitive(cls.getComponentType());
        }
        return isPrimitive(cls);
    }

    public static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive()
                || cls == String.class
                || cls == Boolean.class
                || cls == Character.class
                || Number.class.isAssignableFrom(cls)
                || Date.class.isAssignableFrom(cls);
    }

    /**
     * is compatible..
     */
    public static boolean isCompatible(Class<?> c, Object o) {
        boolean pt = c.isPrimitive();
        if (o == null) {
            return !pt;
        }

        if (pt) {
            if (c == int.class) {
                c = Integer.class;
            } else if (c == boolean.class) {
                c = Boolean.class;
            } else if (c == long.class) {
                c = Long.class;
            } else if (c == float.class) {
                c = Float.class;
            } else if (c == double.class) {
                c = Double.class;
            } else if (c == char.class) {
                c = Character.class;
            } else if (c == byte.class) {
                c = Byte.class;
            } else if (c == short.class) {
                c = Short.class;
            }
        }
        if (c == o.getClass()) {
            return true;
        }
        return c.isInstance(o);
    }

    /**
     * 检查对象是否是指定接口的实现。
     * 不会触发到指定接口的{@link Class}，所以如果ClassLoader中没有指定接口类时，也不会出错。
     */
    public static boolean isInstance(Object obj, String interfaceClazzName) {
        for (Class<?> clazz = obj.getClass();
             clazz != null && !clazz.equals(Object.class);
             clazz = clazz.getSuperclass()) {
            Class<?>[] interfaces = clazz.getInterfaces();
            for (Class<?> itf : interfaces) {
                if (itf.getName().equals(interfaceClazzName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 检查字段是否公共属性public static final
     */
    public static boolean isPublicInstanceField(Field field) {
        return Modifier.isPublic(field.getModifiers())
                && !Modifier.isStatic(field.getModifiers())
                && !Modifier.isFinal(field.getModifiers())
                && !field.isSynthetic();
    }


    /**
     * 获取包路径
     */
    public static String descCodeBase(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        ProtectionDomain domain = cls.getProtectionDomain();
        if (domain == null) {
            return null;
        }
        CodeSource source = domain.getCodeSource();
        if (source == null) {
            return null;
        }
        URL location = source.getLocation();
        if (location == null) {
            return null;
        }
        return location.getFile();
    }

    /**
     * 方法描述为易读的字符串
     * getByUserId method name. "void do(int)", "void do()", "int do(java.lang.String,boolean)"
     */
    public static String descMethod(final Method m) {
        StringBuilder ret = new StringBuilder();
        ret.append(descClass(m.getReturnType())).append(' ');
        ret.append(m.getName()).append('(');
        Class<?>[] parameterTypes = m.getParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i > 0) {
                ret.append(',');
            }
            ret.append(descClass(parameterTypes[i]));
        }
        ret.append(')');
        return ret.toString();
    }

    /**
     * 方法描述为易读的字符串
     * getByUserId name. java.lang.Object[][].class == "java.lang.Object[][]"
     */
    public static String descClass(Class<?> c) {
        if (c.isArray()) {
            StringBuilder sb = new StringBuilder();
            do {
                sb.append("[]");
                c = c.getComponentType();
            }
            while (c.isArray());

            return c.getName() + sb.toString();
        }
        return c.getName();
    }


    /**
     * 字符串转换为Class
     */
    public static Class<?> name2class(String name) {
        return name2class(UtilClassLoader.getClassLoader(), name);
    }

    private static Class<?> name2class(ClassLoader cl, String name) {
        if ("void".equals(name)) {
            return void.class;
        } else if ("boolean".equals(name)) {
            return boolean.class;
        } else if ("byte".equals(name)) {
            return byte.class;
        } else if ("char".equals(name)) {
            return char.class;
        } else if ("double".equals(name)) {
            return double.class;
        } else if ("float".equals(name)) {
            return float.class;
        } else if ("int".equals(name)) {
            return int.class;
        } else if ("long".equals(name)) {
            return long.class;
        } else if ("short".equals(name)) {
            return short.class;
        } else if ("string".equals(name)) {
            return String.class;
        } else if ("map".equals(name)) {
            return Map.class;
        }
        //
        if (cl == null) {
            cl = UtilClassLoader.getClassLoader();
        }
        Class<?> clazz = NAME_CLASS_CACHE.get(name);
        if (clazz == null) {
            try {
                clazz = Class.forName(name, true, cl);
                NAME_CLASS_CACHE.put(name, clazz);
            } catch (ClassNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
        return clazz;
    }


    static {
        primitiveValueMap.put(Boolean.class, Boolean.FALSE);
        primitiveValueMap.put(Byte.class, Byte.valueOf((byte) 0));
        primitiveValueMap.put(Character.class, Character.valueOf((char) 0));
        primitiveValueMap.put(Short.class, Short.valueOf((short) 0));
        primitiveValueMap.put(Double.class, Double.valueOf(0));
        primitiveValueMap.put(Float.class, Float.valueOf(0));
        primitiveValueMap.put(Integer.class, Integer.valueOf(0));
        primitiveValueMap.put(Long.class, Long.valueOf(0));
        primitiveValueMap.put(boolean.class, Boolean.FALSE);
        primitiveValueMap.put(byte.class, Byte.valueOf((byte) 0));
        primitiveValueMap.put(char.class, Character.valueOf((char) 0));
        primitiveValueMap.put(short.class, Short.valueOf((short) 0));
        primitiveValueMap.put(double.class, Double.valueOf(0));
        primitiveValueMap.put(float.class, Float.valueOf(0));
        primitiveValueMap.put(int.class, Integer.valueOf(0));
        primitiveValueMap.put(long.class, Long.valueOf(0));
    }
}