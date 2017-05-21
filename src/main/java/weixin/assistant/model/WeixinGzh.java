package weixin.assistant.model;

public class WeixinGzh {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.id
     *
     * @mbggenerated
     */
    private Integer id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.business_id
     *
     * @mbggenerated
     */
    private Integer businessId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.app_id
     *
     * @mbggenerated
     */
    private String appId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.name
     *
     * @mbggenerated
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.alias
     *
     * @mbggenerated
     */
    private String alias;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.origin_id
     *
     * @mbggenerated
     */
    private String originId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.head_img
     *
     * @mbggenerated
     */
    private String headImg;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.qrcode_url
     *
     * @mbggenerated
     */
    private String qrcodeUrl;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.service_type_info
     *
     * @mbggenerated
     */
    private Integer serviceTypeInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.verify_type_info
     *
     * @mbggenerated
     */
    private Integer verifyTypeInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.func_info
     *
     * @mbggenerated
     */
    private String funcInfo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column weixin_gzh.status
     *
     * @mbggenerated
     */
    private Integer status;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_gzh
     *
     * @mbggenerated
     */
    public WeixinGzh(Integer id, Integer businessId, String appId, String name, String alias, String originId, String headImg, String qrcodeUrl, Integer serviceTypeInfo, Integer verifyTypeInfo, String funcInfo, Integer status) {
        this.id = id;
        this.businessId = businessId;
        this.appId = appId;
        this.name = name;
        this.alias = alias;
        this.originId = originId;
        this.headImg = headImg;
        this.qrcodeUrl = qrcodeUrl;
        this.serviceTypeInfo = serviceTypeInfo;
        this.verifyTypeInfo = verifyTypeInfo;
        this.funcInfo = funcInfo;
        this.status = status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table weixin_gzh
     *
     * @mbggenerated
     */
    public WeixinGzh() {
        super();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.id
     *
     * @return the value of weixin_gzh.id
     *
     * @mbggenerated
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.id
     *
     * @param id the value for weixin_gzh.id
     *
     * @mbggenerated
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.business_id
     *
     * @return the value of weixin_gzh.business_id
     *
     * @mbggenerated
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.business_id
     *
     * @param businessId the value for weixin_gzh.business_id
     *
     * @mbggenerated
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.app_id
     *
     * @return the value of weixin_gzh.app_id
     *
     * @mbggenerated
     */
    public String getAppId() {
        return appId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.app_id
     *
     * @param appId the value for weixin_gzh.app_id
     *
     * @mbggenerated
     */
    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.name
     *
     * @return the value of weixin_gzh.name
     *
     * @mbggenerated
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.name
     *
     * @param name the value for weixin_gzh.name
     *
     * @mbggenerated
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.alias
     *
     * @return the value of weixin_gzh.alias
     *
     * @mbggenerated
     */
    public String getAlias() {
        return alias;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.alias
     *
     * @param alias the value for weixin_gzh.alias
     *
     * @mbggenerated
     */
    public void setAlias(String alias) {
        this.alias = alias == null ? null : alias.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.origin_id
     *
     * @return the value of weixin_gzh.origin_id
     *
     * @mbggenerated
     */
    public String getOriginId() {
        return originId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.origin_id
     *
     * @param originId the value for weixin_gzh.origin_id
     *
     * @mbggenerated
     */
    public void setOriginId(String originId) {
        this.originId = originId == null ? null : originId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.head_img
     *
     * @return the value of weixin_gzh.head_img
     *
     * @mbggenerated
     */
    public String getHeadImg() {
        return headImg;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.head_img
     *
     * @param headImg the value for weixin_gzh.head_img
     *
     * @mbggenerated
     */
    public void setHeadImg(String headImg) {
        this.headImg = headImg == null ? null : headImg.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.qrcode_url
     *
     * @return the value of weixin_gzh.qrcode_url
     *
     * @mbggenerated
     */
    public String getQrcodeUrl() {
        return qrcodeUrl;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.qrcode_url
     *
     * @param qrcodeUrl the value for weixin_gzh.qrcode_url
     *
     * @mbggenerated
     */
    public void setQrcodeUrl(String qrcodeUrl) {
        this.qrcodeUrl = qrcodeUrl == null ? null : qrcodeUrl.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.service_type_info
     *
     * @return the value of weixin_gzh.service_type_info
     *
     * @mbggenerated
     */
    public Integer getServiceTypeInfo() {
        return serviceTypeInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.service_type_info
     *
     * @param serviceTypeInfo the value for weixin_gzh.service_type_info
     *
     * @mbggenerated
     */
    public void setServiceTypeInfo(Integer serviceTypeInfo) {
        this.serviceTypeInfo = serviceTypeInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.verify_type_info
     *
     * @return the value of weixin_gzh.verify_type_info
     *
     * @mbggenerated
     */
    public Integer getVerifyTypeInfo() {
        return verifyTypeInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.verify_type_info
     *
     * @param verifyTypeInfo the value for weixin_gzh.verify_type_info
     *
     * @mbggenerated
     */
    public void setVerifyTypeInfo(Integer verifyTypeInfo) {
        this.verifyTypeInfo = verifyTypeInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.func_info
     *
     * @return the value of weixin_gzh.func_info
     *
     * @mbggenerated
     */
    public String getFuncInfo() {
        return funcInfo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.func_info
     *
     * @param funcInfo the value for weixin_gzh.func_info
     *
     * @mbggenerated
     */
    public void setFuncInfo(String funcInfo) {
        this.funcInfo = funcInfo == null ? null : funcInfo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column weixin_gzh.status
     *
     * @return the value of weixin_gzh.status
     *
     * @mbggenerated
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column weixin_gzh.status
     *
     * @param status the value for weixin_gzh.status
     *
     * @mbggenerated
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}