package base.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * String Utility Class This is used to encode passwords programmatically
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseStringUtil {

    public static String checkIds(String ids){
        if(ids==null){
            return "";
        }

        ids = ids.trim();
        if(ids.startsWith(",")){
            ids = ids.substring(1,ids.length());
        }
        if(ids.endsWith(",")){
            ids = ids.substring(0,ids.length()-1);
        }
        return ids;
    }

    public static String getString(Object obj) {
        if (obj == null) {
            return "";
        }
        String str = (String) obj;
        str = str.trim();
        if ("null".equals(str.toLowerCase())) {
            return "";
        }
        return str;
    }

    /**
     * get方式传递来的汉字做utf-8转码
     *
     * @param str
     * @return
     */
    public static String getUtf8(String str) {
        if (str == null) {
            return "";
        }
//        try {
//            str = URLDecoder.decode(str,"UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//
//
        try {
            str = new String(str.getBytes("iso8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static boolean hasIt(String ids, long id) {
        if (ids == null || "".equals(ids) || id == 0) {
            return false;
        }
        String[] strs = ids.split(",");
        int len = strs.length;
        for (int i = 0; i < len; i++) {
            if (!"".equals(strs[i]) && id == Long.parseLong(strs[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 如果 ids里包含 id 则 返回 ids 否则 返回  ids+"," + id
     *
     * @param ids
     * @param id
     * @return
     */
    public static String getStr(String ids, long id) {
        if (ids == null || "".equals(ids) || id == 0) {
            return "";
        }
        String[] strs = ids.split(",");
        int len = strs.length;
        for (int i = 0; i < len; i++) {
            if (!"".equals(strs[i]) && id == Long.parseLong(strs[i])) {
                return ids;
            }
        }
        return ids + "," + id;
    }

    /**
     * 把 "liujm<刘继明>,xiaoh,liuwt<刘文涛>"
     * 转成 ："liujm,xiaoh,liuwt"
     *
     * @param usernames
     * @return String
     */
    public static String getMailUserNames(String usernames) {
        //非空判断
        if (usernames == null || "".equals(usernames)) {
            return "";
        }

        String[] strs = filterStr(usernames).split(",");
        int len = strs.length;
        String[] strs2 = new String[len];  //数组：装过滤后的
        StringBuilder str2 = new StringBuilder(); //字符串： 装过滤后

        int k = 0;
        boolean canAppend = true; //排除重复的
        for (int i = 0; i < len; i++) {
            k = strs[i].indexOf("<");
            if (k != -1) {
                strs2[i] = strs[i].substring(0, k);
            } else {
                strs2[i] = strs[i];
            }

            //看看以前的 i 个 中  是否 已经  出现过，出现过则 不 在 append
            for (int j = 0; j < i; j++) {
                if (strs2[i].equals(strs2[j])) {
                    canAppend = false;
                    break;
                }
            }
            if (canAppend) {
                str2.append(strs2[i]);
                if (i != len - 1) {
                    str2.append(",");
                }
            }
            canAppend = true;
        }
        return str2.toString();
    }

    //~ Static fields/initializers =============================================

    private final static Log log = LogFactory.getLog(BaseStringUtil.class);

    //~ Methods ================================================================

    /**
     * Encode a string using algorithm specified in web.xml and return the
     * resulting encrypted password. If exceptions, the plain credentials
     * string is returned
     *
     * @param password  Password or other credentials to use in authenticating
     *                  this username
     * @param algorithm Algorithm used to do the digest
     * @return encypted password based on the algorithm.
     */
    public static String encodePassword(String password, String algorithm) {
        byte[] unencodedPassword = password.getBytes();

        MessageDigest md = null;

        try {
            // first create an instance, given the provider
            md = MessageDigest.getInstance(algorithm);
        } catch (Exception e) {
            log.error("Exception: " + e);

            return password;
        }

        md.reset();

        // call the update method one or more times
        // (useful when you don't know the size of your data, eg. stream)
        md.update(unencodedPassword);

        // now calculate the hash
        byte[] encodedPassword = md.digest();

        StringBuffer buf = new StringBuffer();

        for (byte anEncodedPassword : encodedPassword) {
            if ((anEncodedPassword & 0xff) < 0x10) {
                buf.append("0");
            }

            buf.append(Long.toString(anEncodedPassword & 0xff, 16));
        }

        return buf.toString();
    }

    /**
     * Encode a string using Base64 encoding. Used when storing passwords
     * as cookies.
     * <p/>
     * This is weak encoding in that anyone can use the decodeString
     * routine to reverse the encoding.
     *
     * @param str
     * @return String
     */
    public static String encodeString(String str) {
        Base64 encoder = new Base64();
        return String.valueOf(encoder.encode(str.getBytes())).trim();
    }

    /**
     * Decode a string using Base64 encoding.
     *
     * @param str
     * @return String
     */
    public static String decodeString(String str) {
        Base64 dec = new Base64();
        try {
            return String.valueOf(dec.decode(str));
        } catch (Exception de) {
            throw new RuntimeException(de.getMessage(), de.getCause());
        }
    }

    /**
     * 得到 id int 数组
     *
     * @param ids
     * @return long[]
     */
    public static int[] getIds(String ids) {
        if (ids == null || ids.trim().equals("")) {
            return null;
        }
        String[] ids_string = ids.split(",");
        if (ids_string == null || ids_string.length == 0) {
            return null;
        }

        int len = ids_string.length;
        int[] ids_int = new int[len];
        for (int i = 0; i < len; i++) {
            ids_int[i] = Integer.parseInt(ids_string[i].trim());
        }
        return ids_int;
    }

    /**
     * @param ids
     * @param id
     * @return
     */
    public static String getIds(String ids, long id) {
        int[] ids2 = getIds(ids);
        if (ids2 == null) {
            return String.valueOf(id);
        }

        int len = ids2.length;

        for (int i = 0; i < len; i++) {
            if (id == ids2[i]) {
                return ids;
            }
        }

        return ids + "," + id;
    }

    public static String getIds(String ids, int id) {
        return getIds(ids, id, 0);
    }

    public static String getIds(String ids, int id, int size) {
        String idsLast = String.valueOf(id);
        int[] idsArray = getIds(ids);
        if (idsArray == null) {
            return idsLast;
        }

        int i = 0;
        for (int id2 : idsArray) {
            if (id2 == id) {
                continue;
            }
            i++;
            if (size != 0 && i > size) {
                break;
            }
            idsLast += "," + id2;
        }


        return idsLast;
    }


    /**
     * 过滤无意义 字，和 全角转半角
     *
     * @param str
     * @return String
     */
    public static String filterStr(String str) {
        str = str.trim();
        if (str == null || str.equals("")) {
            return "";
        }
        HashMap map = new HashMap();
        map.put("，", ",");
        map.put(" ", "");
        map.put("|", ",");
        map.put("|", ",");

        int length = str.length();
        for (int i = 0; i < length; i++) {
            String charat = str.substring(i, i + 1);
            if (map.get(charat) != null) {
                str = str.replace(charat, (String) map.get(charat));
                length = str.length();
            }
        }

        //去掉 空格

        String[] strs = str.split(",");
        int len = strs.length;
        str = "";
        for (int i = 0; i < len; i++) {
            if (!strs[i].trim().equals("")) {
                if (str.equals("")) {
                    str = strs[i];
                } else {
                    str = str + "," + strs[i];
                }
            }
        }

        return str;
    }


    //==============================最新整理============================

    /**
     * 判断一个字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isBlank(String str) {
        if (str == null) {
            return true;
        }
        str = str.trim();
        if (str.equals("") || str.toLowerCase().equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }


    /**
     * 截字符串长度 2个英文按一个中文计算
     *
     * @param s
     * @param length
     * @param flag   : 如果截了是否显示 ".."
     * @return
     */
    public static String subStrUnicode(String s, int length, boolean flag) {
        if (length == 0) {
            return s;
        }
        if (s == null) {
            return "null";
        }

        s = s.replace("&ldquo;", "“");
        s = s.replace("&rdquo;", "”");

        length = length * 2;
        try {
            byte[] bytes = s.getBytes("Unicode");
            int n = 0; // 表示当前的字节数
            int i = 2; // 要截取的字节数，从第3个字节开始
            int typelen = bytes.length;
            if (typelen <= length) {
                return s;
            }
            for (; i < bytes.length && n < length; i++) {
                // 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
                if (i % 2 == 1) {
                    n++; // 在UCS2第二个字节时n加1
                } else {
                    // 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
                    if (bytes[i] != 0) {
                        n++;
                    }
                }
            }
            // 如果i为奇数时，处理成偶数
            if (i % 2 == 1)

            {
                // 该UCS2字符是汉字时，去掉这个截一半的汉字
                if (bytes[i - 1] != 0) i = i - 1;
                    // 该UCS2字符是字母或数字，则保留该字符
                else i = i + 1;
            }
            String str = new String(bytes, 0, i, "Unicode");

            boolean b = str.equals(s);
            if (!b && flag) {
                return str + "...";
            }
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @param queryString
     * @param param
     * @return
     */
    public static int getParamValueInt(String queryString, String param) {
        String value = BaseStringUtil.getParamValue(queryString, param);
        return BaseIntUtil.getInt(value);
    }

    /**
     * 得到值
     *
     * @param queryString : ownerId=1&kindId=22&other=2
     * @param param       :kindId
     * @return String : 22
     */
    public static String getParamValue(String queryString, String param) {
        if (BaseStringUtil.isBlank(queryString) || BaseStringUtil.isBlank(param)) {
            return "";
        }
        String[] params = queryString.split("&");
        for (String p : params) {
            String[] ps = p.split("=");
            if (ps[0].equals(param)) {
                return ps[1];
            }
        }
        return "";
    }

    /**
     * 按格式输出
     *
     * @param pattern
     * @param number
     * @return
     */
    public static String getStringDecimalFormat(String pattern, Number number) {
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(number);
    }


    /**
     * originate 中 flag之前的内容 换成 fragment
     * http://newcourse.koolearn.com/a/b/c 换成
     * http://newtrans.koolearn.com/a/b/c
     *
     * @param originate : http://newcourse.koolearn.com/a/b/c
     * @param fragment  :http://newtrans.koolearn.com
     * @param flag      :.com
     * @param before    : 是否替换之前的
     * @return String : http://newtrans.koolearn.com/a/b/c
     */
    public static String replace(String originate, String fragment, String flag, boolean before) {
        if (fragment == null) {
            return originate;
        }

        if (BaseStringUtil.isBlank(originate)) {
            return "";
        }

        if (flag == null) {
            return fragment;
        }

        int i = originate.indexOf(flag);
        if (i != -1) {
            if (before) {
                //替换之前的
                String str = originate.substring(i + flag.length());
                originate = fragment + str;
            } else {
                //替换之后的
                String str = originate.substring(0, i + flag.length());
                originate = str + fragment;
            }
        }
        return originate;
    }

    /**
     * java过滤html标签函数
     *
     * @param htmlStr
     * @return
     */
    public static String html2Text(String htmlStr) {
        String textStr = "";
        Pattern p_script;
        Matcher m_script;
        Pattern p_style;
        Matcher m_style;
        Pattern p_html;
        Matcher m_html;

        try {
//       String regEx_script = "<[\s]*?script[^>]*?>[\s\S]*?<[\s]*?\/[\s]*?script[\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\s\S]*?<\/script> }
//       String regEx_style = "<[\s]*?style[^>]*?>[\s\S]*?<[\s]*?\/[\s]*?style[\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\s\S]*?<\/style> }
//          String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签

            p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签

            textStr = htmlStr;

        } catch (Exception e) {
            System.err.println("Html2Text: " + e.getMessage());
        }

        return textStr;//返回文本字符串
    }

    /**
     * 字符串转成String数组 ,按逗号分隔
     *
     * @param str
     * @return
     */
    public static String[] getArray(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        str = str.trim();
        str = str.replaceAll("，", ",");
        if (str.endsWith(",")) {
            str = str.substring(0, str.length() - 1);
        }
        String[] strs = str.split(",");
        int len = strs.length;
        String[] array = new String[len];
        for (int i = 0; i < len; i++) {
            array[i] = strs[i].trim();
        }
        return array;
    }

    /**
     * 排重
     *
     * @param str
     * @return
     */
    public static Map getMap(String str) {
        String[] array = BaseStringUtil.getArray(str);
        Map<String, String> map = new LinkedHashMap<String, String>();

        if (array == null) {
            return map;
        }

        for (String str2 : array) {
            map.put(str2, str2);
        }

        return map;
    }

    /**
     * @param map
     * @return
     */
    public static String getStr(Map map) {
        String str = "";
        for (Object key : map.keySet()) {
            if (!str.equals("")) {
                str += ",";
            }
            str += map.get(key);
        }
        return str;
    }

    /**
     * @param str1
     * @param str2
     * @param max
     * @return
     */
    public static String getStr(String str1, String str2, int max) {
        Map<String, String> map1 = BaseStringUtil.getMap(str1);
        Map<String, String> map2 = BaseStringUtil.getMap(str2);
        for (String key : map2.keySet()) {
            if (map1.size() >= max) {
                return BaseStringUtil.getStr(map1);
            }
            map1.put(key, key);
        }
        return BaseStringUtil.getStr(map1);
    }

    /**
     * 将 a=1&b= 2&c = 3 转成 map
     *
     * @param queryString
     * @return
     */
    public static Map<String, String> getParamMap(String queryString) {
        Map<String, String> paramMap = new HashMap<String, String>();

        if (!BaseStringUtil.isBlank(queryString)) {
            String[] params = queryString.trim().split("&");
            for (String param : params) {
                if (BaseStringUtil.isBlank(param)) {
                    continue;
                }
                String[] kvs = param.split("=");
                paramMap.put(kvs[0].trim(), kvs[1].trim());
            }
        }
        return paramMap;
    }


    /**
     * 过滤html有害字符
     *
     * @param str
     * @return
     */
    public static String filterAttackHtml(String str) {
        if (BaseStringUtil.isBlank(str)) {
            return "";
        }

        str = str.replaceAll("&", "&amp;")
                .replaceAll(" ", "&nbsp;")
                .replaceAll(">", "&gt;")
                .replaceAll("<", "&lt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("©", "&copy;")
                .replaceAll("®", "&reg;")
                        //.replaceAll("*", "&times;")
                .replaceAll("%", "&divide;");
        return str;
    }

    /**
     * 长短名 截取
     *
     * @param name
     * @param shortName
     * @param width
     * @return
     */
    public static String subString(String name, String shortName, int width) {
        boolean flag = false;
        if (width == 0) {
            return name;
        }
        if (isBlank(name)) {
            return subStrUnicode(shortName, width, flag);
        }
        if (isBlank(shortName)) {
            return subStrUnicode(name, width, flag);
        }
        return subStrUnicode(name, width, flag);
    }


    /**
     * 用法：fromEncodedUnicode(str.toCharArray(), 0, str.length());
     * author:lvrujia
     *
     * @param in
     * @param off
     * @param len
     * @return
     */
    public static String fromEncodedUnicode(char[] in, int off, int len) {
        char aChar;
        char[] out = new char[len]; // 只短不长
        int outLen = 0;
        int end = off + len;
        while (off < end) {
            aChar = in[off++];
            if (aChar == '\\') {
                aChar = in[off++];
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = in[off++];
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed \\uxxxx encoding.");
                        }
                    }
                    out[outLen++] = (char) value;
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    out[outLen++] = aChar;
                }
            } else {
                out[outLen++] = (char) aChar;
            }
        }
        return new String(out, 0, outLen);
    }


    public static String getUrl(String input) {
        String regex = "(http://.*?)[\\s\u4e00-\u9fa5\\'\\\">)） ，,(（<]";
        //regex = "http(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$" ;
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            // System.out.println(matcher.group(1));
            return matcher.group(1);
        }
        return "";
    }

    /**
     *
     * @param content
     * @return
     */
    public static boolean containChinese(String content) {
        if(BaseStringUtil.isBlank(content)){
            return false;
        }
        String regEx = "[\u4e00-\u9fa5]";
        Pattern pat = Pattern.compile(regEx);
        Matcher matcher = null;
        boolean flg = false;
        try {
            matcher = pat.matcher(content);
        } catch (Exception e) {
            log.error("content=" + content);
            e.printStackTrace();
            return false;
        }
        if (matcher.find()) {
            flg = true;
        }
        return flg;
    }



    public static int getSize(String str){
        String[] array = getArray(str);
        return array.length;
    }
    public static String getEmailUrl(String email) {
        if (BaseStringUtil.isBlank(email)) {
            log.error("email=" + email);
            return "";
        }
        String[] args = email.split("@");
        return "http://mail." + args[1];
    }

    public static String getExtName(String fileName) {
        if (BaseStringUtil.isBlank(fileName)) {
            log.error("fileName=" + fileName);
            return "";
        }

        int begin = fileName.lastIndexOf(".");
        String extName = fileName.substring(begin);
        return extName;
    }

}
