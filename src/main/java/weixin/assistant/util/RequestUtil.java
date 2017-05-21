package weixin.assistant.util;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by worgen on 2015/9/1.
 */
public class RequestUtil {

    public static String getString(HttpServletRequest request) throws IOException {
        java.io.BufferedInputStream bis = new java.io.BufferedInputStream(
                request.getInputStream());

        byte read[] = new byte[1024 * 1024];
        String miwen = "";

        try {
            while ((bis.read(read, 0, 1 * 1)) != -1) {
                miwen += new String(read, 0, 1 * 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bis.close();
        }

        return miwen;
    }
}
