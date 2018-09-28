package cn.wolfcode.p2p.base.exception;

/**
 * 用来封装可以展示给用户的异常信息
 * 比如:用户名为空
 */
public class DisplayableException extends RuntimeException{

    public DisplayableException(String msg){
        super(msg);
    }
}
