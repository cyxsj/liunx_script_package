package cn.wolfcode.p2p.util;

import cn.wolfcode.p2p.base.exception.DisplayableException;
import org.springframework.util.StringUtils;

public class AsserUtil {
    /**
     * 断言字符串是否为空
     * @param username
     * @param message
     */
    public static void isNull(String username,String message){
        //用户名
        /*if (username == null){
            throw new DisplayableException(message);
        }*/
        if (!StringUtils.hasLength( username)){
            throw new DisplayableException(message);
        }
    }

    /**
     * 断言对象是否为空
     */
    public static void isObjNull(Object obj,String message){
        if (obj == null){
            throw new DisplayableException(message);
        }
    }

    /**
     * 断言两个字符串的值是否相同
     */
    public static void isEquals(String str1,String str2, String message){
        if (!str1.equals(str2)){
            throw new DisplayableException(message);
        }
    }

    public static void isTrue(boolean b, String message) {
        if (b){
            throw new DisplayableException(message);
        }
    }
}
