package zeyfra.dmas.core.utils;

/**
 * 密码实体
 * @author ZeyFra
 * @date 2021/2/16 14:39
 */
public class PasswordInfo {

    //密码随机串码
    private String salt;

    //MD5后的密码
    private String password;

    public PasswordInfo(String salt, String password) {
        super();
        this.salt = salt;
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }
    public void setSalt(String salt) {
        this.salt = salt;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
}
