package entity;

/***
 * 状态码
 */

public class StatusCode {
    public static final int OK = 20000;     //成功
    public static final int ERROR = 20001;  //错误
    public static final int NO_PERMISSION = 401;    //权限不足
    public static final int NOT_FOUND = 404;    //NOT FOUND
    public static final int TOKEN_ERROR = 30001;    //token出错
}
