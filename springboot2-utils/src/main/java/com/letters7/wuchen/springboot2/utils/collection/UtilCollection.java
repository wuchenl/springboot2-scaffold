package com.letters7.wuchen.springboot2.utils.collection;


import com.letters7.wuchen.springboot2.utils.exception.UtilException;
import com.letters7.wuchen.springboot2.utils.json.UtilFastjson;
import com.letters7.wuchen.springboot2.utils.string.UtilString;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.util.*;

/**
 * zoubin02
 */
public class UtilCollection {
    //private static final Logger logger = LoggerFactory.getLogger(UtilCollection.class);

    private UtilCollection() {
    }



    //集合是否为空
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.size() == 0;
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }

    public static boolean isEmpty(Map map) {
        return map == null || map.size() == 0;
    }

    public static boolean isNotEmpty(Map map) {
        return map != null && map.size() > 0;
    }




    //集合长度
    public static int sizeOf(Collection<?> collection) {
        if (isEmpty(collection)) {
            return 0;
        }
        return collection.size();
    }

    public static int sizeOf(Map<?, ?> map) {
        if (map == null) {
            return 0;
        }
        return map.size();
    }




    /**
     * M排序
     */
    public static <K extends Comparable, V> Map<K, V> sortMapByKey(Map<K, V> data) {
        Map<K, V> data_ = new LinkedHashMap<>();
        List<K> list = new LinkedList<>(data.keySet());
        Collections.sort(list);
        for (K k : list) {
            data_.put(k, data.get(k));
        }
        return data_;
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> List<T> sortList(List<T> list) {
        if (list != null && list.size() > 0) {
            Collections.sort((List) list);
        }
        return list;
    }





    /**
     * SET转换为List
     */
    public static List set2List(Set itemSet) {
        if (itemSet == null) {
            return null;
        }
        List _list = new ArrayList();
        if (itemSet.isEmpty()) {
            return _list;
        }
        //
        Iterator iter = itemSet.iterator();
        while (iter.hasNext()) {
            _list.add(iter.next());
        }
        return _list;
    }

    //数组转换为List
    public static List<String> array2List(String... items) {
        if (items == null) {
            return null;
        }
        List _list = new ArrayList();
        if (items.length==0) {
            return _list;
        }
        //
        for (Object obj : items) {
            _list.add(obj);
        }
        return _list;
    }


    /**
     * List转换为SET
     */
    public static Set list2Set(List itemList) {
        if (itemList == null) {
            return null;
        }
        Set _set = new HashSet();
        if (itemList.isEmpty()) {
            return _set;
        }
        //
        for (Object obj : itemList) {
            _set.add(obj);
        }
        return _set;
    }

    //数组转换为SET
    public static Set<String> array2Set(String... items) {
        if (items == null) {
            return null;
        }
        Set _set = new HashSet();
        if (items.length==0) {
            return _set;
        }
        //
        for (Object obj : items) {
            _set.add(obj);
        }
        return _set;
    }



    /**
     * 集合连接为字符串
     */
    public static String joinArray(Object... objs){
        Collection collection = Arrays.asList(objs);
        return joinCollection(collection, ",");
    }

    public static String joinCollection(Collection objs) {
        return joinCollection(objs, ",");
    }

    public static String joinCollection(Collection objs, String spliter) {
        if (objs == null || objs.isEmpty()) {
            return "";
        }

        StringBuilder buffer = new StringBuilder();
        int i=0 ;
        for(Object obj : objs){
            if(i!=0){
                buffer.append(",");
            }
            buffer.append(obj);
            i++;
        }
        return buffer.toString();
    }



    /**
     * 字符串拆分成集合
     */
    public static List<String> splitString2List(String temp) {
        return splitString2List(temp, ',');
    }

    public static List<String> splitString2List(String temp, char spliter) {
        List<String> list = new ArrayList<String>();
        if (UtilString.isEmpty(temp)) {
            return list;
        }
        String[] ary = UtilString.split(temp, spliter);
        if (ary != null && ary.length > 0) {
            for (String str : ary) {
                list.add(str);
            }
        }
        return list;
    }



    // 将String数组转换成int数组 转换出错者返回null
    public static Integer[] stringArr2IntArr(String[] strArr) {
        Integer[] inArr = new Integer[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            try {
                inArr[i] = Integer.parseInt(strArr[i].trim());
            } catch (NumberFormatException e) {
                throw e;
            }
        }
        //Arrays.sort(inArr);
        return inArr;
    }


    /**
     * 提取集合中的对象的一个属性(通过Getter函数), 组合成由分割符分隔的字符串.
     */
    public static String extractToString(final Collection collection, final String propertyName, final String separator) {
        List list = extractToList(collection, propertyName);
        return StringUtils.join(list, separator);
    }

    @SuppressWarnings("unchecked")
    public static List extractToList(final Collection collection, final String propertyName) {
        List list = new ArrayList(collection.size());
        try {
            for (Object obj : collection) {
                list.add(PropertyUtils.getProperty(obj, propertyName));
            }
        } catch (Exception e) {
            throw UtilException.convertReflectExceptionToUnchecked(e);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    public static Map extractToMap(final Collection collection, final String keyPropertyName, final String valuePropertyName) {
        Map map = new HashMap(collection.size());

        try {
            for (Object obj : collection) {
                map.put(PropertyUtils.getProperty(obj, keyPropertyName),
                        PropertyUtils.getProperty(obj, valuePropertyName));
            }
        } catch (Exception e) {
            throw UtilException.convertReflectExceptionToUnchecked(e);
        }

        return map;
    }


    public static Map<String, String> toStringMap(String... pairs) {
        Map<String, String> parameters = new HashMap<String, String>();
        if (pairs.length > 0) {
            if (pairs.length % 2 != 0) {
                throw new IllegalArgumentException("pairs must be even.");
            }
            for (int i = 0; i < pairs.length; i = i + 2) {
                parameters.put(pairs[i], pairs[i + 1]);
            }
        }
        return parameters;
    }

    public static Map<String, String> toMap(Properties properties) {
        if (properties == null) {
            return new HashMap<String, String>(0);
        }
        Map<String, String> map = new HashMap<String, String>(properties.size());
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        return map;
    }






    /**
     * 返回第一个列表中比第二个多出来的元素
     */
    public static <T> List<T> getLeftDiff(List<T> list1, List<T> list2) {
        if (isEmpty(list2)) {
            return list1;
        }
        List<T> list = new ArrayList<T>();
        if (isNotEmpty(list1)) {
            for (T o : list1) {
                if (!list2.contains(o)) {
                    list.add(o);
                }
            }
        }
        return list;
    }



    // ----------------------调试打印集合类-----------------------------




    public static void infoMap(Logger logger, Map map) {
        if (map == null || map.isEmpty()) {
            if (logger.isInfoEnabled()) {
                logger.info("this map is build!");
            }
        } else {
            int i = 0;
            Iterator iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                i++;
                Object key = iterator.next();
                Object value = map.get(key);
                if (logger.isInfoEnabled()) {
                    logger.info("(" + i + ")" + key + " -> " + UtilFastjson.toJSONString(value));
                }
            }
        }
    }

    public static void infoArray(Logger logger, Object[] objs) {
        if (objs == null || objs.length == 0) {
            if (logger.isInfoEnabled()) {
                logger.info("this objs is build!");
            }
        } else {
            int i = 0;
            for (Object obj : objs) {
                i++;
                if (obj != null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("(" + i + ")" + UtilFastjson.toJSONString(obj));
                    }
                }
            }
        }
    }

    public static void infoCollection(Logger logger, Collection objs) {
        if (objs == null || objs.size() == 0) {
            if (logger.isInfoEnabled()) {
                logger.info("this objs is build!");
            }
        } else {
            int i = 0;
            for (Object obj : objs) {
                i++;
                if (obj != null) {
                    if (logger.isInfoEnabled()) {
                        logger.info("(" + i + ")" + UtilFastjson.toJSONString(obj));
                    }
                }
            }
        }
    }

}