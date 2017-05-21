package base.util;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * User: liuwentao@kongzhong.com
 * Date: 2010-7-20 10:13:47
 * <p/>
 * 说明:
 */
public class BaseIntUtil {

    public static void main(String[] args) {
        String ids = "1,2,3,4,5";

        int[] array = upsetIds(ids,1);

        System.out.println("----------------------------------------");
        for (int id : array) {
            System.out.println(id);
        }
    }

    public static int[] upsetIds(String ids, int feed) {
        if (feed == 0) {
            feed = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        }

        int[] array = getArray(ids);
        if (array == null || array.length == 1) {
            return array;
        }

        int length = array.length;

        int[] array2 = new int[length];

        for (int i = 0; i < length; i++) {
            int j = (i + feed) % length;
            int k = i / 2;
            if (i % 2 == 1) {
                k = (length - 1) / 2 + (i) / 2 + 1;
            }
            array2[k] = array[j];
        }

        return array2;
    }

    /**
     * @param i
     * @return
     */
    public static boolean isNotBlank(Integer i) {
        return !isBlank(i);
    }


    /**
     * 判断是否为空
     *
     * @param i
     * @return
     */
    public static boolean isBlank(Integer i) {
        if (i == null || i == 0) {
            return true;
        }
        return false;
    }

    /**
     * @param request
     * @param paramName
     * @return
     */
    public static int getInt(HttpServletRequest request, String paramName) {
        return getInt(request, paramName, 0);
    }

    public static int getInt(HttpServletRequest request, String paramName, int defaultValue) {
        String obj = request.getParameter(paramName);
        int i = getInt(obj);
        if (i == 0) {
            i = defaultValue;
        }
        return i;
    }


    public static int[] getIntArray(HttpServletRequest request, String paramName) {
        String obj = request.getParameter(paramName);
        return getIntArray(obj);
    }


    /**
     * 得到 int
     *
     * @param obj
     * @return
     */
    public static int getInt(Object obj) {
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        if (obj instanceof Double) {
            Double d = (Double) obj;
            return d.intValue();
        }

        if (obj instanceof String) {
            String str = (String) obj;
            if (BaseStringUtil.isBlank(str)) {
                return 0;
            }
            str = str.trim();
            try {
                if (str.indexOf(".") != -1) {
                    Double d = Double.parseDouble(str);
                    return d.intValue();
                }
                return Integer.parseInt(str);
            } catch (NumberFormatException e) {
                //e.printStackTrace();
            }
            return 0;
        }

        if (obj instanceof Boolean) {
            boolean b = (Boolean) obj;
            if (b) {
                return 1;
            }
            return 0;
        }
        if (obj instanceof Long) {
            Long value = (Long) obj;
            if (value == null) {
                return 0;
            }
            return value.intValue();
        }

        return 0;
    }

    /**
     * 字符串转成int数组 ,按逗号分隔
     *
     * @param str
     * @return
     */
    public static int[] getIntArray(String str) {
        String[] strs = BaseStringUtil.getArray(str);
        if (strs == null) {
            return null;
        }
        int len = strs.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = BaseIntUtil.getInt(strs[i]);
        }
        return array;
    }

    public static int getIntArrayLength(String str) {
        int[] array = getIntArray(str);
        if (array == null) {
            return 0;
        }
        return array.length;
    }

    /**
     * @param array
     * @return
     */
    public static int[] getArrayRepeat(int[] array) {
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        List<Integer> list = new ArrayList<Integer>();

        for (int i : array) {
            if (map.containsKey(i)) {
                list.add(i);
                continue;
            }
            map.put(i, i);
        }
        int size = list.size();
        int[] array2 = new int[size];
        for (int i = 0; i < size; i++) {
            array2[i] = list.get(i);
        }

        return array2;
    }

    /**
     * array1里面有，而array2里面没有 的数据
     *
     * @param array1
     * @param array2
     * @return
     */
    public static int[] getArrayNotIn(int[] array1, int[] array2) {
        List<Integer> result = new ArrayList<Integer>();
        if (array1 == null) {
            return new int[]{};
        }
        if (array2 == null) {
            return array1;
        }

        for (int newInt : array1) {
            boolean add = true;
            for (int oldInt : array2) {
                if (newInt == oldInt) {
                    add = false;
                }
            }
            if (add) {
                result.add(newInt);
            }
        }
        int size = result.size();
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = result.get(i);
        }

        return array;
    }

    /**
     * 字符串转成int数组 ,按逗号分隔
     *
     * @param str
     * @return
     */
    public static int[] getArray(String str) {
        String[] strs = BaseStringUtil.getArray(str);
        if (strs == null) {
            return null;
        }
        int len = strs.length;
        int[] array = new int[len];
        for (int i = 0; i < len; i++) {
            array[i] = BaseIntUtil.getInt(strs[i]);
        }
        return array;
    }

    public static boolean isIn(int id, String ids) {
        int[] idArray = getIntArray(ids);
        if (idArray == null) {
            return false;
        }
        for (int id2 : idArray) {
            if (id == id2) {
                return true;
            }
        }
        return false;
    }

    public static int getRandom(int feed) {
        int i = (int) (Math.random() * feed);
        return i;
    }
}
