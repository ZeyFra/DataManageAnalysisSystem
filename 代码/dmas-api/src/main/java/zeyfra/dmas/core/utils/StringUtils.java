package zeyfra.dmas.core.utils;

import java.util.Map;

/**
 * 字符串常用工具类
 * @author ZeyFra
 * @date 2021/2/16 14:38
 */
public class StringUtils {

    /**
     * 判断是否为空字符
     * @param str
     * @return
     */
    public static boolean isBlank(String str){
        return str==null || "".equals(str);
    }


    /**
     * 将MAP转换成一个xml格式，格式为<xml><key>value</key>...</xml>
     * @param params
     * @return
     */
    public static String mapToXml(Map<String, String> params){
        StringBuffer sb = new StringBuffer("<xml>");
        for(String key:params.keySet()){
            sb.append("<")
                    .append(key).append(">")
                    .append(params.get(key))
                    .append("</").append(key).append(">");
        }

        sb.append("</xml>");
        return sb.toString();
    }
}
