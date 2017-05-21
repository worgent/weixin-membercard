package weixin.vip.Util;

/**
 * Created by worgen on 2015/9/11.
 */
public class MathUtil {
    public static long parseLong(String str){
        long ret = 0L;
        if( str == null || str.isEmpty() ){
            return 0L;
        }
        ret = Long.parseLong(str);
        return ret;
    }
    public static long parseLong(Object obj){
        if( obj == null ) return 0L;

        return parseLong(obj.toString());
    }
}
