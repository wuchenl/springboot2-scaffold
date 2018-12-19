package com.letters7.wuchen.springboot2.utils.json;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.letters7.wuchen.springboot2.utils.exception.UtilException;

import java.util.List;

/**
 * Created by zoubin02 on 2017/5/8.
 */
public class UtilFastjson {

    private static final String TAG = "JSON";


    /**
     SerializerFeature有用的一些枚举值:
     QuoteFieldNames———-输出key时是否使用双引号,默认为true
     WriteMapNullValue——–是否输出值为null的字段,默认为false
     WriteNullNumberAsZero—-数值字段如果为null,输出为0,而非null
     WriteNullListAsEmpty—–List字段如果为null,输出为[],而非null
     WriteNullStringAsEmpty—字符类型字段如果为null,输出为”“,而非null
     WriteNullBooleanAsFalse–Boolean字段如果为null,输出为false,而非null
     以上这些属性 ： 主要是针对Object对象序列化转换时的情况（这个时候才能判断参数的类型），而在Map中，你放进入了null就是null，进行序列化时已经没法判断它原来的类型了，所以并没有起作用
     */
    //所有为null的字符串都变成空字符串，最简单的做法就是加一个值过滤器
    private static SerializeFilter nullValueFilter = new ValueFilter() {
        @Override
        public Object process(Object obj, String s, Object v) {
            if(v==null){
                return "";
            }
            return v;
        }
    };





    /**json转JSONObject
     * @param obj
     * @return
     */
    public static JSONObject parseObject(Object obj) {
        return parseObject(toJSONString(obj));
    }
    /**json转JSONObject
     * @param json
     * @return
     */
    public static JSONObject parseObject(String json) {
        int features = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE;
        features |= Feature.OrderedField.getMask();
        return parseObject(json, features);
    }
    /**json转JSONObject
     * @param json
     * @param features
     * @return
     */
    public static JSONObject parseObject(String json, int features) {
        try {
            return com.alibaba.fastjson.JSON.parseObject(json, JSONObject.class, features);
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    /**JSONObject转实体类
     * @param object
     * @param clazz
     * @return
     */
    public static <T> T parseObject(JSONObject object, Class<T> clazz) {
        return parseObject(toJSONString(object), clazz);
    }
    /**json转实体类
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            int features = com.alibaba.fastjson.JSON.DEFAULT_PARSER_FEATURE;
            features |= Feature.OrderedField.getMask();
            return com.alibaba.fastjson.JSON.parseObject(json, clazz, features);
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    /**json转JSONArray
     * @param json
     * @return
     */
    public static JSONArray parseArray(String json) {
        try {
            return com.alibaba.fastjson.JSON.parseArray(json);
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }
    /**JSONArray转实体类列表
     * @param array
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(JSONArray array, Class<T> clazz) {
        return parseArray(toJSONString(array), clazz);
    }
    /**json转实体类列表
     * @param json
     * @param clazz
     * @return
     */
    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            return com.alibaba.fastjson.JSON.parseArray(json, clazz);
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    /**实体类转json
     * @param obj
     * @return
     */
    public static String toJSONString(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            String json = com.alibaba.fastjson.JSON.toJSONString(obj);
            return json;
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    /**实体类转json
     * @param obj
     * @param features
     * @return
     */
    public static String toJSONString(Object obj, SerializerFeature... features) {
        if (obj instanceof String) {
            return (String) obj;
        }
        try {
            String json = com.alibaba.fastjson.JSON.toJSONString(obj, features);
            return json;
        } catch (Exception e) {
            throw UtilException.unchecked(e);
        }
    }

    /**格式化，显示更好看
     * @param json
     * @return
     */
    /*public static String format(String json) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);
        return gson.toJson(jsonElement);
    }
   */

    /**判断是否为JSONObject
     * @param obj instanceof String ? parseObject
     * @return
     */
    public static boolean isJSONObject(Object obj) {
        if (obj instanceof JSONObject) {
            return true;
        }
        if (obj instanceof String) {
            try {
                JSONObject json = parseObject((String) obj);
                return json != null && json.isEmpty() == false;
            } catch (Exception e) {
                throw UtilException.unchecked(e);
            }
        }

        return false;
    }
    /**判断是否为JSONArray
     * @param obj instanceof String ? parseArray
     * @return
     */
    public static boolean isJSONArray(Object obj) {
        if (obj instanceof JSONArray) {
            return true;
        }
        if (obj instanceof String) {
            try {
                JSONArray json = parseArray((String) obj);
                return json != null && json.isEmpty() == false;
            } catch (Exception e) {
                throw UtilException.unchecked(e);
            }
        }

        return false;
    }


}
