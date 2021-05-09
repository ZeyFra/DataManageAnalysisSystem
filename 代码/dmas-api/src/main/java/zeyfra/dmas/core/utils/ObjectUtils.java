package zeyfra.dmas.core.utils;

import java.lang.reflect.Field;

/**
 * @author ZeyFra
 * @date 2021/3/25 17:44
 */
public class ObjectUtils {

    public static boolean checkObjectFieldIsNull(Object obj) throws IllegalAccessException {

        for(Field f : obj.getClass().getDeclaredFields()){
            f.setAccessible(true);
            if(f.get(obj) == null){
                return true;
            }
        }
        return false;
    }
}
