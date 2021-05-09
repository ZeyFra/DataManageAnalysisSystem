package zeyfra.dmas.core.utils;

import org.apache.commons.lang3.RandomStringUtils;

/**
 * @author ZeyFra
 * @date 2021/2/16 14:39
 */
public class PasswordHandler {

    /**
     * checkPass:校验密码是否一致
     * @author Bool
     * @param inputPass 用户传入的密码
     * @param salt 数据库保存的密码随机码
     * @param pass 数据库保存的密码MD5
     * @return
     */
    public static boolean checkPass(String inputPass , String salt , String pass){
        String pwdMd5 = MD5Utils.MD5(inputPass);
        return MD5Utils.MD5(pwdMd5 + salt).equals(pass);
    }


    /**
     *
     * buildPassword:用于用户注册时产生一个密码
     * @author Bool
     * @param inputPass 输入的密码
     * @return PassInfo 返回一个密码对象，记得保存
     */
    public static PasswordInfo buildPassword(String inputPass) {

        //产生一个6位数的随机码
        String salt = RandomStringUtils.randomAlphabetic(6);
        //加密后的密码
        String encryptPassword = MD5Utils.MD5(MD5Utils.MD5(inputPass)+salt);
        //返回对象
        return new PasswordInfo(salt,encryptPassword);
    }

}
