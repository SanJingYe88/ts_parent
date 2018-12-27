package util;

/**
 * 自定义字符串工具类
 */
public class MyStringUtils {

    /**
     * 判断字符串是否为null或者为空
     * @param str
     * @return true-为null或者为空,false-不为null且不为空
     */
    public static boolean isNullOrEmpty(String str){
        if (str == null || str.trim().length() == 0){
            return true;
        }
        return false;
    }
}
