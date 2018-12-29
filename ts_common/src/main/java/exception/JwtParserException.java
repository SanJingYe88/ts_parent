package exception;

/**
 * 自定义异常 - token 解析出错
 */
public class JwtParserException extends RuntimeException {

    public JwtParserException() {
        super();
    }

    public JwtParserException(String message) {
        super(message);
    }
}
