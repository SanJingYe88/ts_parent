package exception;

/**
 * 自定义异常 - 权限不足
 */
public class PermissionException extends RuntimeException {

    public PermissionException() {
        super();
    }

    public PermissionException(String msg) {
        super(msg);
    }
}
