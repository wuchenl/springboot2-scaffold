package com.letters7.wuchen.springboot2.utils.clazz;


import com.letters7.wuchen.springboot2.assertion.Assert;
import com.letters7.wuchen.springboot2.utils.exception.UtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author zoubin.
 */
public final class UtilClassLoader {

    private static Logger logger = LoggerFactory.getLogger(UtilClassLoader.class);

    private static final ConstructorCache CONSTRUCTOR_CACHE = new ConstructorCache();

    private UtilClassLoader() {
    }


    /**
     * 获取ClassLoader下的文件流，一般用于读取配置文件
     */
    public static InputStream getResourceAsStream(String filePath) {
        ClassLoader classLoader = UtilClassLoader.getClassLoader();

        // 加载文件es
        try {
            InputStream is = classLoader.getResourceAsStream(filePath);
            return is;
        } catch (Exception e1) {
            logger.error(e1.toString());
        }
        return null;
    }

    public static File getResourceAsFile(String filePath) {
        ClassLoader classLoader = UtilClassLoader.getClassLoader();

        // 加载文件es
        try {
            URL url = classLoader.getResource(filePath);
            URI uri = url.toURI();
            File file = new File(uri);
            return file;
        } catch (Exception e1) {
            logger.error(e1.toString());
        }
        return null;
    }

    public static List<URL> getResourceAsUrl(final String resourceName) {
        List<URL> lis = new ArrayList<URL>();
        Enumeration<URL> urls = null;
        try {
            urls = UtilClassLoader.getClassLoader().getResources(resourceName);
        } catch (IOException e) {
            throw new RuntimeException("文件读取发生异常，原因是" + e.getMessage(), e);
        }
        if (urls != null) {
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                lis.add(url);
            }
        }
        return lis;
    }


    /**
     * Return the dsl ClassLoader to use: typically the thread context
     * ClassLoader, if available; the ClassLoader that loaded the ClassUtils
     * class will be used as fallback.
     * <p/>
     * Call this method if you intend to use the thread context ClassLoader in a
     * scenario where you absolutely need a non-null ClassLoader reference: for
     * example, for class path resource loading (but not necessarily for
     * <code>Class.forName</code>, which accepts a <code>null</code> ClassLoader
     * reference as well).
     *
     * @return the dsl ClassLoader (never <code>null</code>)
     * @see Thread#getContextClassLoader()
     */
    public static ClassLoader getClassLoader() {
        return getClassLoader(UtilClassLoader.class);
    }

    public static ClassLoader getClassLoader(Class<?> cls) {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back to system class loader...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = cls.getClassLoader();
        }
        return cl;
    }


    @SuppressWarnings("unchecked")
    public static <T> T newInstance(final String className) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return newInstance(cl , className);
    }
    public static <T> T newInstance(ClassLoader classLoader, final String className) {
        try {
            classLoader = classLoader == null ? UtilClassLoader.class.getClassLoader() : classLoader;
            Constructor<T> constructor = CONSTRUCTOR_CACHE.get(classLoader, className);
            if (constructor != null) {
                return constructor.newInstance();
            }
            Class<T> clazz = (Class<T>) loadClass(classLoader, className);
            return (T) newInstance(clazz, classLoader, className);
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    public static <T> T newInstance(Class<T> clazz, ClassLoader classLoader, String className) {
        try {
            final Constructor<T> constructor = clazz.getDeclaredConstructor();
            if (!constructor.isAccessible()) {
                constructor.setAccessible(true);
            }
            CONSTRUCTOR_CACHE.put(classLoader, className, constructor);
            return constructor.newInstance();
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    public static Class<?> loadClass(final ClassLoader classLoader, final String className) {
        Assert.assertNotNull(className);
        ClassLoader theClassLoader = classLoader;
        if (theClassLoader == null) {
            theClassLoader = Thread.currentThread().getContextClassLoader();
        }
        if (theClassLoader != null) {
            try {
                return tryLoadClass(className, theClassLoader);
            } catch (ClassNotFoundException ignore) {
            }
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw UtilException.unchecked(e);
        }
    }

    private static Class<?> tryLoadClass(String className, ClassLoader classLoader)
            throws ClassNotFoundException {

        if (className.startsWith("[")) {
            return Class.forName(className, false, classLoader);
        } else {
            return classLoader.loadClass(className);
        }
    }


    private static final class ConstructorCache {
        private final ConcurrentMap<ClassLoader, ConcurrentMap<String, WeakReference<Constructor>>> cache;

        private ConstructorCache() {
            cache = new ConcurrentHashMap<ClassLoader, ConcurrentMap<String, WeakReference<Constructor>>>();
        }

        private <T> Constructor put(ClassLoader classLoader, String className, Constructor<T> constructor) {
            ClassLoader cl = classLoader == null ? UtilClassLoader.class.getClassLoader() : classLoader;
            ConcurrentMap<String, WeakReference<Constructor>> innerCache = cache.get(cl);
            if (innerCache == null) {
                innerCache = new ConcurrentHashMap<String, WeakReference<Constructor>>(100);
                ConcurrentMap<String, WeakReference<Constructor>> old = cache.putIfAbsent(cl, innerCache);
                if (old != null) {
                    innerCache = old;
                }
            }
            innerCache.put(className, new WeakReference<Constructor>(constructor));
            return constructor;
        }

        @SuppressWarnings("unchecked")
        public <T> Constructor<T> get(ClassLoader classLoader, String className) {
            Assert.assertNotNull(className);
            ConcurrentMap<String, WeakReference<Constructor>> innerCache = cache.get(classLoader);
            if (innerCache == null) {
                return null;
            }
            WeakReference<Constructor> reference = innerCache.get(className);
            Constructor constructor = reference == null ? null : reference.get();
            if (reference != null && constructor == null) {
                innerCache.remove(className);
            }
            return (Constructor<T>) constructor;
        }
    }
}
