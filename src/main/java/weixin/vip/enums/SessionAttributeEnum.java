package weixin.vip.enums;

/**
 * Created with IntelliJ IDEA.
 * User: tianbc
 * Date: 15-9-11
 * Time: 下午5:43
 * To change this template use File | Settings | File Templates.
 */
public enum SessionAttributeEnum {

    //页面来的验证码来源
    REGIST_VIP_CODE("registVipCode", "VIP注册验证码"),
    REGIST_VIP_PHONE("registVipPhone", "VIP注册手机号"),
    REGIST_VIP_CODE_TIME("registVipCodeTime", "VIP注册验证码申请时间"),
    VIP_CHANGE_PASSWORD_CODE("vipChangePasswordCode", "VIP注册验证码"),
    VIP_CHANGE_PASSWORD_PHONE("vipChangePasswordPhone", "VIP注册手机号"),
    VIP_CHANGE_PASSWORD_CODE_TIME("vipChangePasswordCodeTime", "VIP注册验证码申请时间"),
    OTHER_CODE("otherCode","其它"),
    OTHER_PHONE("otherPhone","其它"),
    OTHER_CODE_TIME("otherCodeTime","其它时间");

    private String key;
    private String desc;

    SessionAttributeEnum(String key, String desc) {
        this.desc = desc;
        this.key = key;
    }
    public static SessionAttributeEnum getCheckCodeEnum(String key) {

        for (SessionAttributeEnum enm : SessionAttributeEnum.values()) {
            if (enm.getKey().equals(key.trim())) {
                return enm;
            }
        }
        return null;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

     public static void  main(String args[]){
           // System.out.println(CheckCodeEnum.CREATE_ARTICLE.getKey()+" "+CheckCodeEnum.CREATE_ARTICLE.getDesc()+" "+CheckCodeEnum.CREATE_ARTICLE);
    }
}
