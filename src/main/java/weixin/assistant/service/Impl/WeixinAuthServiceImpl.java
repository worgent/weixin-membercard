package weixin.assistant.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.assistant.constants.WeixinConstant;
import weixin.assistant.dao.*;
import weixin.assistant.model.*;
import weixin.assistant.service.WeixinAuthService;
import weixin.assistant.util.RequestUtil;
import weixin.assistant.util.SignUtil;
import weixin.assistant.util.TimeUtil;
import weixin.assistant.util.WeixinUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by worgen on 2015/9/1.
 */
@Service
public class WeixinAuthServiceImpl implements WeixinAuthService {
    private static final Logger log = Logger.getLogger("BusinessLog");

    @Autowired
    private KeyValueMapper keyValueMapper;
    @Autowired
    private WeixinGzhMapper weixinGzhMapper;
    @Autowired
    private WeixinGzhAuthorizerMapper weixinGzhAuthorizerMapper;
    @Autowired
    private WeixinUserMapper weixinUserMapper;
    @Autowired
    private WeixinUserAuthorizerMapper weixinUserAuthorizerMapper;
    @Autowired
    private VipBusinessMapper vipBusinessMapper;

    @Override
    public String getComponentAppID() {
        return WeixinConstant.componentAppID;
    }

    @Override
    public String getComponentAppSecret() {
        return WeixinConstant.componentAppSecret;
    }

    @Override
    public String getMessageCryptKey() {
        return WeixinConstant.messageCryptKey;
    }

    @Override
    public String getMessageVerifyToken() {

        return WeixinConstant.messageVerifyToken;
    }
    @Override
    public String getRedirectUrl() {
        return WeixinConstant.redirectUrl;
    }

    @Override
    public String getJSRedirectUrl() {
        return WeixinConstant.jsRedirectUrl;
    }


    @Override
    public MPreAuthCode getPreAuthCode() {
//        KeyValueExample keyValueExample = new KeyValueExample();
//        String key = WeixinConstant.dbPreAuthCode;
//        keyValueExample.createCriteria().andKeyStrEqualTo(key);
//        List<KeyValue> keyValues = keyValueMapper.selectByExampleWithBLOBs(keyValueExample);
//        if( keyValues.size() != 0 && keyValues.size() != 1 ){
//            log.error("PreAuthCode,num error," + keyValues.size() + "," + JSON.toJSONString(keyValues));
//            return null;
//        }
//        boolean needHttpGet = false;
//        if( keyValues.size() == 1 ){
//            MPreAuthCode mPreAuthCode = JSON.parseObject(keyValues.get(0).getValueStr(),
//                    MPreAuthCode.class);
//            if( mPreAuthCode.expired() ){
//                needHttpGet = true;
//            }
//            else
//            {
//                return mPreAuthCode;
//            }
//        }
        //先设定无论如何都获取新的
        if( true /*|| needHttpGet == true || keyValues.size() == 0 */){
            //没有则进行获取
            String componentAppID = WeixinConstant.componentAppID;
            MComponentAccessToken mComponentAccessToken = this.getComponentAccessToken();
            if( mComponentAccessToken == null ){
                log.error("mComponentAccessToken null");
                return null;
            }
            MPreAuthCode mPreAuthCode = WeixinUtil.httpGetPreAuthCode(componentAppID, mComponentAccessToken.getAccessToken());
            this.savePreAuthCode(mPreAuthCode);
            return mPreAuthCode;
        }

        log.error("exception");
        return null;
    }

    @Override
    public int savePreAuthCode(MPreAuthCode mPreAuthCode) {
        KeyValueExample keyValueExample = new KeyValueExample();
        String key = WeixinConstant.dbPreAuthCode;
        keyValueExample.createCriteria().andKeyStrEqualTo(key);
        List<KeyValue> keyValues = keyValueMapper.selectByExampleWithBLOBs(keyValueExample);
        if( keyValues.size() == 0 ){
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr(key);
            keyValue.setValueStr(JSON.toJSONString(mPreAuthCode));
            keyValueMapper.insertSelective(keyValue);
        }else{
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr(key);
            keyValue.setValueStr(JSON.toJSONString(mPreAuthCode));
            keyValueMapper.updateByExampleSelective(keyValue, keyValueExample);
        }
        return 0;
    }

    @Override
    public MComponentVerifyTicket getComponentVerifyTicket() {
        KeyValueExample keyValueExample = new KeyValueExample();
        String key = WeixinConstant.dbComponentVerifyTicket;
        keyValueExample.createCriteria().andKeyStrEqualTo(key);
        List<KeyValue> keyValues = keyValueMapper.selectByExampleWithBLOBs(keyValueExample);
        if( keyValues.size() == 0 ){
            //推送值没办法直接获取
            return null;
        }
        if( keyValues.size() != 1 ){
            log.error("ComponentVerifyTicket,num error," + keyValues.size() + "," + JSON.toJSONString(keyValues));
            return null;
        }
        MComponentVerifyTicket mComponentVerifyTicket = JSON.parseObject(keyValues.get(0).getValueStr(),
                MComponentVerifyTicket.class);

        return mComponentVerifyTicket;
    }

    @Override
    public int saveComponentVerifyTicket(MComponentVerifyTicket mComponentVerifyTicket) {
        KeyValueExample keyValueExample = new KeyValueExample();
        String key = WeixinConstant.dbComponentVerifyTicket;
        keyValueExample.createCriteria().andKeyStrEqualTo(key);
        List<KeyValue> keyValues = keyValueMapper.selectByExampleWithBLOBs(keyValueExample);
        if( keyValues.size() == 0 ){
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr(key);
            keyValue.setValueStr(JSON.toJSONString(mComponentVerifyTicket));
            keyValueMapper.insertSelective(keyValue);
        }else{
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr(key);
            keyValue.setValueStr(JSON.toJSONString(mComponentVerifyTicket));
            keyValueMapper.updateByExampleSelective(keyValue, keyValueExample);
        }

        return 0;
    }

    @Override
    public MComponentAccessToken getComponentAccessToken() {
        KeyValueExample keyValueExample = new KeyValueExample();
        String key = WeixinConstant.dbComponentAccessToken;
        keyValueExample.createCriteria().andKeyStrEqualTo(key);
        List<KeyValue> keyValues = keyValueMapper.selectByExampleWithBLOBs(keyValueExample);
        if( keyValues.size() != 0 && keyValues.size() != 1 ){
            log.error("dbComponentAccessToken,num error," + keyValues.size() + "," + JSON.toJSONString(keyValues));
            return null;
        }
        //是否需要重新获取
        boolean needHttpGet = false;
        if( keyValues.size() == 1 ) {
            MComponentAccessToken mComponentAccessToken = JSON.parseObject(keyValues.get(0).getValueStr(),
                    MComponentAccessToken.class);
            if( mComponentAccessToken.expired() ){
                needHttpGet = true;
            }
            else
            {
                return mComponentAccessToken;
            }
        }
        if( needHttpGet == true || keyValues.size() == 0 ){
            //没有则直接获取
            MComponentVerifyTicket mComponentVerifyTicket = this.getComponentVerifyTicket();
            if( mComponentVerifyTicket == null ){
                log.error("mComponentVerifyTicket null");
                return null;
            }
            String componentAppID = WeixinConstant.componentAppID;
            String componentAppSecret = WeixinConstant.componentAppSecret;
            MComponentAccessToken mComponentAccessToken = WeixinUtil.httpGetComponentAccessToken(
                    componentAppID, componentAppSecret, mComponentVerifyTicket.getVerifyTicket());
            this.saveComponentAccessToken(mComponentAccessToken);
            return mComponentAccessToken;
        }
        log.error("exception");
        return null;
    }

    @Override
    public int saveComponentAccessToken(MComponentAccessToken mComponentAccessToken) {
        KeyValueExample keyValueExample = new KeyValueExample();
        String key = WeixinConstant.dbComponentAccessToken;
        keyValueExample.createCriteria().andKeyStrEqualTo(key);
        List<KeyValue> keyValues = keyValueMapper.selectByExampleWithBLOBs(keyValueExample);
        if( keyValues.size() == 0 ){
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr(key);
            keyValue.setValueStr(JSON.toJSONString(mComponentAccessToken));
            keyValueMapper.insertSelective(keyValue);
        }else{
            KeyValue keyValue = new KeyValue();
            keyValue.setKeyStr(key);
            keyValue.setValueStr(JSON.toJSONString(mComponentAccessToken));
            keyValueMapper.updateByExampleSelective(keyValue, keyValueExample);
        }
        return 0;
    }

    @Override
    public int insertWeixinGzh(WeixinGzh weixinGzh) {
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().
                andAppIdEqualTo(weixinGzh.getAppId()).
                andBusinessIdEqualTo(weixinGzh.getBusinessId());
        List<WeixinGzh> weixinGzhs = weixinGzhMapper.selectByExample(weixinGzhExample);
        if( weixinGzhs.size() == 0 ){
            return weixinGzhMapper.insertSelective(weixinGzh);
        }else{
            weixinGzhMapper.updateByExampleSelective(weixinGzh, weixinGzhExample);
            return weixinGzhs.get(0).getId();
        }
    }

    @Override
    public int insertWeixinGzhAuthorizer(WeixinGzhAuthorizer weixinGzhAuthorizer) {
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().
                andWeixinGzhIdEqualTo(weixinGzhAuthorizer.getWeixinGzhId());
        List<WeixinGzhAuthorizer> weixinGzhAuthorizers = weixinGzhAuthorizerMapper.selectByExample(weixinGzhAuthorizerExample);
        if( weixinGzhAuthorizers.size() == 0 ){
            weixinGzhAuthorizerMapper.insertSelective(weixinGzhAuthorizer);
        }else{
            weixinGzhAuthorizerMapper.updateByExampleSelective(weixinGzhAuthorizer, weixinGzhAuthorizerExample);
        }

        return 0;
    }

    @Override
    public boolean hasWeixinGzh(int id) {
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().andIdEqualTo(id);
        List<WeixinGzh> weixinGzhs = weixinGzhMapper.selectByExample(weixinGzhExample);
        if( weixinGzhs.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public WeixinGzh getWeixinGzh(int id) {
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
       /*修改前*/
       weixinGzhExample.createCriteria().andIdEqualTo(id);
//        weixinGzhExample.createCriteria().andBusinessIdEqualTo(id);
        List<WeixinGzh> weixinGzhs = weixinGzhMapper.selectByExample(weixinGzhExample);
        if( weixinGzhs.size() == 0 ){
            return null;
        }else{
            return weixinGzhs.get(0);
        }
    }

    @Override
    public WeixinGzh getWeixinGzh(int businessID, String appID) {
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().
                andBusinessIdEqualTo(businessID).andAppIdEqualTo(appID);
        List<WeixinGzh> weixinGzhs = weixinGzhMapper.selectByExample(weixinGzhExample);
        if( weixinGzhs.size() == 0 ){
            return null;
        }else{
            return weixinGzhs.get(0);
        }
    }

    @Override
    public WeixinGzh getWeixinGzh(String appID) {
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().
                andAppIdEqualTo(appID);
        List<WeixinGzh> weixinGzhs = weixinGzhMapper.selectByExample(weixinGzhExample);
        if( weixinGzhs.size() == 0 ){
            return null;
        }else{
            return weixinGzhs.get(0);
        }
    }

    @Override
    public WeixinGzh getWeixinGzhByBusinessId(int businessId) {
        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().
                andBusinessIdEqualTo(businessId);
        List<WeixinGzh> weixinGzhs = weixinGzhMapper.selectByExample(weixinGzhExample);
        if( weixinGzhs.size() == 0 ){
            return null;
        }else{
            return weixinGzhs.get(0);
        }
    }

    @Override
    public WeixinGzhAuthorizer checkGzhAuthorizerExpired(WeixinGzhAuthorizer weixinGzhAuthorizer) {
        if(gzhAuthorizerExpired(weixinGzhAuthorizer)){
            WeixinGzhAuthorizer newWeixinGzhAuthorizer = new WeixinGzhAuthorizer();
            newWeixinGzhAuthorizer = WeixinUtil.httpRefreshAuthorozerAccessToken(
                    this.getComponentAppID(),
                    this.getComponentAccessToken().getAccessToken(),
                    weixinGzhAuthorizer.getAppId(),
                    weixinGzhAuthorizer.getAuthorizerRefreshToken());
            newWeixinGzhAuthorizer.setId(weixinGzhAuthorizer.getId());
            newWeixinGzhAuthorizer.setWeixinGzhId(weixinGzhAuthorizer.getWeixinGzhId());
           // newWeixinGzhAuthorizer.
            this.insertWeixinGzhAuthorizer(newWeixinGzhAuthorizer);
             return newWeixinGzhAuthorizer;
        }
        //js失效
        if( jsapiTicketExpired(weixinGzhAuthorizer) ){
            WeixinGzhAuthorizer newWeixinGzhAuthorizer = new WeixinGzhAuthorizer();
            newWeixinGzhAuthorizer = WeixinUtil.httpGetJSApiTicket(
                    weixinGzhAuthorizer.getAuthorizerAccessToken());
            weixinGzhAuthorizer.setJsapiTicket(newWeixinGzhAuthorizer.getJsapiTicket());
            weixinGzhAuthorizer.setTicketExpireTime(newWeixinGzhAuthorizer.getTicketExpireTime());
            // newWeixinGzhAuthorizer.
            this.insertWeixinGzhAuthorizer(weixinGzhAuthorizer);
            return weixinGzhAuthorizer;
        }
        return weixinGzhAuthorizer;
    }

    private boolean gzhAuthorizerExpired(WeixinGzhAuthorizer weixinGzhAuthorizer) {
        if( weixinGzhAuthorizer.getTokenExpireTime() == null ) return true;
        if( weixinGzhAuthorizer.getTokenExpireTime().after(new Date()) ){
            return false;
        }
        return true;
    }
    private boolean jsapiTicketExpired(WeixinGzhAuthorizer weixinGzhAuthorizer) {
        if(weixinGzhAuthorizer.getTicketExpireTime() == null ) return true;
        if( weixinGzhAuthorizer.getTicketExpireTime().after(new Date()) ){
            return false;
        }
        return true;
    }

    @Override
    public boolean hasWeixinGzhAuthorizer(int weixinGzhID) {
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().andWeixinGzhIdEqualTo(weixinGzhID);
        List<WeixinGzhAuthorizer> weixinGzhAuthorizers = weixinGzhAuthorizerMapper.selectByExample(weixinGzhAuthorizerExample);
        if( weixinGzhAuthorizers.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public boolean hasWeixinGzhAuthorizer(int businessID, String appID) {
        WeixinGzh weixinGzh = getWeixinGzh(businessID, appID);
        if( weixinGzh == null ) return false;
        return hasWeixinGzhAuthorizer(weixinGzh.getId());
    }

    @Override
    public boolean hasWeixinGzhAuthorizer(String appID) {
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().andAppIdEqualTo(appID);
        List<WeixinGzhAuthorizer> weixinGzhAuthorizers = weixinGzhAuthorizerMapper.selectByExample(weixinGzhAuthorizerExample);
        if( weixinGzhAuthorizers.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }


    @Override
    public WeixinGzhAuthorizer getWeixinGzhAuthorizer(int id) {
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().andIdEqualTo(id);
        List<WeixinGzhAuthorizer> weixinGzhAuthorizers = weixinGzhAuthorizerMapper.selectByExample(weixinGzhAuthorizerExample);
        if( weixinGzhAuthorizers.size() == 0 ){
            return null;
        }else{
            return checkGzhAuthorizerExpired(weixinGzhAuthorizers.get(0));
        }
    }

    @Override
    public WeixinGzhAuthorizer getWeixinGzhAuthorizerByGzhID(int weixinGzhID) {
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().andWeixinGzhIdEqualTo(weixinGzhID);
        List<WeixinGzhAuthorizer> weixinGzhAuthorizers = weixinGzhAuthorizerMapper.selectByExample(weixinGzhAuthorizerExample);
        if( weixinGzhAuthorizers.size() == 0 ){
            return null;
        }else{
            return checkGzhAuthorizerExpired(weixinGzhAuthorizers.get(0));
        }
    }

    @Override
    public WeixinGzhAuthorizer getWeixinGzhAuthorizer(int businessID, String appID) {
        WeixinGzh weixinGzh = getWeixinGzh(businessID, appID);
        if( weixinGzh == null ) return null;
        return getWeixinGzhAuthorizerByGzhID(weixinGzh.getId());
    }
    @Override
    public WeixinGzhAuthorizer getWeixinGzhAuthorizerByBusinessId(int businessID) {
        WeixinGzh weixinGzh = getWeixinGzhByBusinessId(businessID);
        if( weixinGzh == null ) return null;
        return getWeixinGzhAuthorizerByGzhID(weixinGzh.getId());
    }
    @Override
    public WeixinGzhAuthorizer getWeixinGzhAuthorizer(String appID) {
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().andAppIdEqualTo(appID);
        List<WeixinGzhAuthorizer> weixinGzhAuthorizers = weixinGzhAuthorizerMapper.selectByExample(weixinGzhAuthorizerExample);
        if( weixinGzhAuthorizers.size() == 0 ){
            return null;
        }else{
            return checkGzhAuthorizerExpired(weixinGzhAuthorizers.get(0));
        }
    }
    @Override
    public int unAuthorizeGzh(String appID) {
        //删除
        WeixinGzhAuthorizerExample weixinGzhAuthorizerExample = new WeixinGzhAuthorizerExample();
        weixinGzhAuthorizerExample.createCriteria().andAppIdEqualTo(appID);
        weixinGzhAuthorizerMapper.deleteByExample(weixinGzhAuthorizerExample);

        WeixinGzhExample weixinGzhExample = new WeixinGzhExample();
        weixinGzhExample.createCriteria().andAppIdEqualTo(appID);
        List<WeixinGzh> weixinGzhList = weixinGzhMapper.selectByExample(weixinGzhExample);
        int businessID = weixinGzhList.get(0).getBusinessId();

        VipBusiness vipBusiness = new VipBusiness();
        vipBusiness.setType(0);
        VipBusinessExample vipBusinessExample = new VipBusinessExample();
        vipBusinessExample.createCriteria().andIdEqualTo(businessID);
        vipBusinessMapper.updateByExampleSelective(vipBusiness, vipBusinessExample);

        weixinGzhMapper.deleteByExample(weixinGzhExample);




        log.info("unAuthorizeGzh,"+appID);
        return 0;
    }

    @Override
    public int authorizeGzh(int businessID, String authCode, long expiresIn) {
        //
        WeixinGzhAuthorizer weixinGzhAuthorizer = new WeixinGzhAuthorizer();

        weixinGzhAuthorizer.setAuthorizationCode(authCode);
        weixinGzhAuthorizer.setCodeExpireTime(TimeUtil.getDate(TimeUtil.now() + expiresIn));
        //根据授权码获取公众号授权信息
        String componentAppID = this.getComponentAppID();

        MComponentAccessToken mComponentAccessToken = this.getComponentAccessToken();
        if (mComponentAccessToken == null) {
            log.error("mComponentAccessToken null");
            return -1;
        }
        String componentAccessToken = mComponentAccessToken.getAccessToken();
        weixinGzhAuthorizer = WeixinUtil.httpGetWeixinGzhAuthorizer(componentAppID, componentAccessToken, authCode);
        if (weixinGzhAuthorizer == null) {
            log.error("weixinGzhAuthorizer null," + componentAppID + "," + componentAccessToken + "," + authCode);
            return -2;
        }
        //判断是否已授权过
        if( this.hasWeixinGzhAuthorizer(businessID, weixinGzhAuthorizer.getAppId()) ){
            log.debug("其他公众号已经授权了，不能再授权");
            return -4;
        }
        if( this.hasWeixinGzhAuthorizer(weixinGzhAuthorizer.getAppId()) ){
            log.error("账号" + businessID + ",公众号" + weixinGzhAuthorizer.getAppId() + "已授权过");
            return -5;
        }
        //存储
        //获得个人信息

        WeixinGzh weixinGzh = new WeixinGzh();
        String appID = weixinGzhAuthorizer.getAppId();
        weixinGzh = WeixinUtil.httpGetWeixinGzh(componentAppID, componentAccessToken, appID);
        if (weixinGzh == null) {
            log.error("weixinGzh null," + componentAppID + "," + componentAccessToken + "," + appID);
            return -6;
        }

        //根据businessID 更新商户类型，1为已授权绑定公众号
        VipBusiness vipBusiness = new VipBusiness();
        vipBusiness.setType(1);
        VipBusinessExample vipBusinessExample = new VipBusinessExample();
        vipBusinessExample.createCriteria().andIdEqualTo(businessID);
        vipBusinessMapper.updateByExampleSelective(vipBusiness, vipBusinessExample);

        weixinGzh.setBusinessId(businessID);
        this.insertWeixinGzh(weixinGzh);
        int gzhID =weixinGzh.getId();
        weixinGzhAuthorizer.setWeixinGzhId(gzhID);
        weixinGzhAuthorizer.setStatus(0);
        this.insertWeixinGzhAuthorizer(weixinGzhAuthorizer);

        //记录账号信息
        log.info("authorizeAccessToken," + authCode + ","
                + appID + ","
                + com.alibaba.fastjson.JSONObject.toJSONString(weixinGzh) + ","
                + com.alibaba.fastjson.JSONObject.toJSONString(weixinGzhAuthorizer));
        return 0;
    }

    @Override
    public int insertWeixinUser(WeixinUser weixinUser) {
        WeixinUserExample weixinUserExample = new WeixinUserExample();
        weixinUserExample.createCriteria().andOpenIdEqualTo(weixinUser.getOpenId());
        List<WeixinUser> weixinUsers = weixinUserMapper.selectByExample(weixinUserExample);
        if( weixinUsers.size() == 0 ){
            return weixinUserMapper.insertSelective(weixinUser);
        }else{
            weixinUserMapper.updateByExampleSelective(weixinUser, weixinUserExample);
            return weixinUsers.get(0).getId();
        }
    }

    @Override
    public int insertWeixinUserAuthorizer(WeixinUserAuthorizer weixinUserAuthorizer) {
        WeixinUserAuthorizerExample weixinUserAuthorizerExample = new WeixinUserAuthorizerExample();
        weixinUserAuthorizerExample.createCriteria().andOpenIdEqualTo(weixinUserAuthorizer.getOpenId());
        List<WeixinUserAuthorizer> weixinUserAuthorizers = weixinUserAuthorizerMapper.selectByExample(weixinUserAuthorizerExample);
        if( weixinUserAuthorizers.size() == 0 ){
            weixinUserAuthorizerMapper.insertSelective(weixinUserAuthorizer);
        }else{
            weixinUserAuthorizerMapper.updateByExampleSelective(weixinUserAuthorizer, weixinUserAuthorizerExample);
        }

        return 0;
    }

    @Override
    public boolean hasWeixinUser(int id) {
        WeixinUserExample weixinUserExample = new WeixinUserExample();
        weixinUserExample.createCriteria().andIdEqualTo(id);
        List<WeixinUser> weixinUsers = weixinUserMapper.selectByExample(weixinUserExample);
        if( weixinUsers.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public WeixinUser getWeixinUser(int id) {
        WeixinUserExample weixinUserExample = new WeixinUserExample();
        weixinUserExample.createCriteria().andIdEqualTo(id);
        List<WeixinUser> weixinUsers = weixinUserMapper.selectByExample(weixinUserExample);
        if( weixinUsers.size() == 0 ){
            return null;
        }else{
            return weixinUsers.get(0);
        }
    }

    @Override
    public WeixinUser getWeixinUser(int weixinGzhID, String openID) {
        WeixinUserExample weixinUserExample = new WeixinUserExample();
        weixinUserExample.createCriteria().
                andOpenIdEqualTo(openID).andWeixinGzhIdEqualTo(weixinGzhID);
        List<WeixinUser> weixinUsers = weixinUserMapper.selectByExample(weixinUserExample);
        if( weixinUsers.size() == 0 ){
            return null;
        }else{
            return weixinUsers.get(0);
        }
    }

    @Override
    public boolean userAuthorizerExpired(WeixinUserAuthorizer weixinUserAuthorizer) {
        return false;
    }

    @Override
    public boolean hasWeixinUserAuthorizer(int weixinUserID) {
        WeixinUserAuthorizerExample weixinUserAuthorizerExample = new WeixinUserAuthorizerExample();
        weixinUserAuthorizerExample.createCriteria().andWeixinUserIdEqualTo(weixinUserID);
        List<WeixinUserAuthorizer> weixinUserAuthorizers = weixinUserAuthorizerMapper.selectByExample(weixinUserAuthorizerExample);
        if( weixinUserAuthorizers.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }


    @Override
    public boolean hasWeixinUserAuthorizer(int weixinGzhID, String openID) {
        WeixinUserAuthorizerExample weixinUserAuthorizerExample = new WeixinUserAuthorizerExample();
        weixinUserAuthorizerExample.createCriteria().andWeixinUserIdEqualTo(weixinGzhID).andOpenIdEqualTo(openID);
        List<WeixinUserAuthorizer> weixinUserAuthorizers = weixinUserAuthorizerMapper.selectByExample(weixinUserAuthorizerExample);
        if( weixinUserAuthorizers.size() == 0 ){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public WeixinUserAuthorizer getWeixinUserAuthorizer(int id) {
        WeixinUserAuthorizerExample weixinUserAuthorizerExample = new WeixinUserAuthorizerExample();
        weixinUserAuthorizerExample.createCriteria().andIdEqualTo(id);
        List<WeixinUserAuthorizer> weixinUserAuthorizers = weixinUserAuthorizerMapper.selectByExample(weixinUserAuthorizerExample);
        if( weixinUserAuthorizers.size() == 0 ){
            return null;
        }else{
            return weixinUserAuthorizers.get(0);
        }
    }
    @Override
    public WeixinUserAuthorizer getWeixinUserAuthorizerByUserID(int weixinUserID) {
        WeixinUserAuthorizerExample weixinUserAuthorizerExample = new WeixinUserAuthorizerExample();
        weixinUserAuthorizerExample.createCriteria().andWeixinUserIdEqualTo(weixinUserID);
        List<WeixinUserAuthorizer> weixinUserAuthorizers = weixinUserAuthorizerMapper.selectByExample(weixinUserAuthorizerExample);
        if( weixinUserAuthorizers.size() == 0 ){
            return null;
        }else{
            return weixinUserAuthorizers.get(0);
        }
    }
    @Override
    public WeixinUserAuthorizer getWeixinUserAuthorizer(int weixinGzhID, String openID) {
        WeixinUser weixinUser = getWeixinUser(weixinGzhID, openID);
        if( weixinUser == null ) return null;
        return getWeixinUserAuthorizerByUserID(weixinUser.getId());

    }

    @Override
    public int userAuthorize(int gzhID, String appID, String code, String state) {
        //
        String componentAppID = this.getComponentAppID();
        MComponentAccessToken mComponentAcessToken = this.getComponentAccessToken();
        if( mComponentAcessToken == null ){
            log.error("mComponentAcessToken null");
            return -1;
        }
        String componentAcessToken = mComponentAcessToken.getAccessToken();
        WeixinUserAuthorizer weixinUserAuthorizer = new WeixinUserAuthorizer();
        weixinUserAuthorizer = WeixinUtil.httpGetJSAccessToken(
                componentAppID, componentAcessToken, appID, code);
        if(weixinUserAuthorizer == null){
            log.error("weixinUserAuthorizer null");
            return -2;
        }
        //个人信息，使用base scope不需要获取
        WeixinUser weixinUser = new WeixinUser();
        weixinUser.setWeixinGzhId(gzhID);
        weixinUser.setOpenId(weixinUserAuthorizer.getOpenId());

        int userID = this.insertWeixinUser(weixinUser);
        weixinUserAuthorizer.setWeixinUserId(userID);
        this.insertWeixinUserAuthorizer(weixinUserAuthorizer);
        //记录账号信息
        log.info("userAuthorize,"
                +gzhID+","
                +appID+","
                +code+","
                +state+","
                + com.alibaba.fastjson.JSONObject.toJSONString(weixinUser) + ","
                + com.alibaba.fastjson.JSONObject.toJSONString(weixinUserAuthorizer));

        //根据state信息进行页面跳转
        //JSONObject jsonState = JSON.parseObject(state);
       // if(jsonState)
        return userID;
    }


    @Override
    public String decryptMessage(String cryptCode, String timestamp, String nonce, String msgSignature) throws AesException {
        String componentAppID = WeixinConstant.componentAppID;
        String messageCryptKey = WeixinConstant.messageCryptKey;
        String messageVerifyToken = WeixinConstant.messageVerifyToken;
        log.debug(
                "timestamp,nonce,msg_signature,componentAppID,messageCryptKey,messageVerifyToken:"
                        +timestamp+","+nonce+","+msgSignature+","+componentAppID+","+messageCryptKey+","+messageVerifyToken);

        return WeixinUtil.decryptMessage(messageVerifyToken, messageCryptKey, componentAppID,
                cryptCode, timestamp, nonce, msgSignature);
    }
    @Override
    public String cryptMessage(String content, String timestamp, String nonce) throws AesException {
        String componentAppID = WeixinConstant.componentAppID;
        String messageCryptKey = WeixinConstant.messageCryptKey;
        String messageVerifyToken = WeixinConstant.messageVerifyToken;

        return WeixinUtil.cryptMessage(messageVerifyToken, messageCryptKey, componentAppID,
                content, timestamp, nonce);
    }
    @Override
    public String decryptMessage(HttpServletRequest request) throws IOException, AesException {
        //获得密文
        String miwen = RequestUtil.getString(request);
        // miwen = "   <xml><AppId><![CDATA[wxdd316cc902ac3fdc]]></AppId> <Encrypt><![CDATA[2knvRUsrg6KYbnIs3pydfcy4lC75bjJNl+y8JdgfHgMaCbHujyGy7Ys7COmhhqvvne2m3vzx/VyPDiVTtIjr8e/D9KNniuPWzMlqtYec8pjdo0h3Feyrw51B4bR6RltGaMVpz7JhcGkbYAImG1jV3zm91dJ8PaAz5yTzd6iGbdifJP7ionmUP4w25V0BXnCIs88lqb+6oN9Q18wH9o32WJCVFi6OVJ8ZFI2KU3pDNjF26rnPl0UfbHHSqCZ2v4AeQkzukAMfMnGK+kgkONpTjho6BUYKdRv5eXrqY8hvUI5iaMgZTNs41LTf3+VHZsE8mH9t9zNvc1V08cidxpJZh2wU4ZtjPLb0mj263v4YbbiBRTKW7DEDhMY+rqgHGP/+S9KxH4CwYOFaewvU/sba3igV3+EwTHHo/azM0/w4BghTaREukP5lyxByKNeb7t/iVMpsKyDCaNDB6cJ2GVJxBg==]]></Encrypt></xml>";
        log.debug("miwen,"+miwen);

        //解密
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String msg_signature = request.getParameter("msg_signature");
        //  timestamp = "1441165836";
        //  nonce = "81724097";
        //  msg_signature = "5f87db26b70c139416e7bc01b2f541d82fbf214e";
        String componentAppID = this.getComponentAppID();
        String decryptCode = this.decryptMessage(miwen, timestamp, nonce, msg_signature);

        log.debug("decryptCode,"+decryptCode);
        return decryptCode;

    }

    @Override
    public int dealComponentPush(String xml) throws DocumentException {
        Document doc = null;
        doc = DocumentHelper.parseText(xml); // 将字符串转为XML
        Element rootElement = doc.getRootElement(); // 获取根节点smsReport
       // String component_verify_ticket = rootElement.elementTextTrim("ComponentVerifyTicket");
       // String createTime = rootElement.elementTextTrim("CreateTime");
        String infoType = rootElement.elementTextTrim("InfoType");
        if(infoType.equals("component_verify_ticket") ){
            MComponentVerifyTicket mComponentVerifyTicket = new MComponentVerifyTicket();

            String component_verify_ticket = rootElement.elementTextTrim("ComponentVerifyTicket");
            String createTime = rootElement.elementTextTrim("CreateTime");
            String appID = rootElement.elementTextTrim("AppId");

            mComponentVerifyTicket.setVerifyTicket(component_verify_ticket);
            mComponentVerifyTicket.setAppID(appID);
            mComponentVerifyTicket.setInfoType(infoType);
            mComponentVerifyTicket.setCreateTime(Integer.parseInt(createTime));

            this.saveComponentVerifyTicket(mComponentVerifyTicket);
            log.info("dealComponentPush,component_verify_ticket,"
                    +xml+","
                    + com.alibaba.fastjson.JSONObject.toJSONString(mComponentVerifyTicket));

            MComponentAccessToken mComponentAccessToken = this.getComponentAccessToken();
            log.info("dealComponentPush,MComponentAccessToken,"+
                    com.alibaba.fastjson.JSONObject.toJSONString(mComponentAccessToken));

            //
            MPreAuthCode mPreAuthCode = this.getPreAuthCode();
            log.info("dealComponentPush,MPreAuthCode,"+
                    com.alibaba.fastjson.JSONObject.toJSONString(mPreAuthCode));
        }
        else if(infoType.equals("unauthorized")){
            String authorizerAppid = rootElement.elementTextTrim("AuthorizerAppid");
            String createTime = rootElement.elementTextTrim("CreateTime");
            String appID = rootElement.elementTextTrim("AppId");

            int ret = unAuthorizeGzh(authorizerAppid);
            log.info("dealComponentPush,unAuthorizeGzh,"+xml+","+authorizerAppid+","+ret);
        }

        return 0;
    }

    @Override
    public Map<String, String> sign(int businessId, String url) {

        WeixinGzhAuthorizer weixinGzhAuthorizer = getWeixinGzhAuthorizerByBusinessId(businessId);
         String jsapiTicket = weixinGzhAuthorizer.getJsapiTicket();
        return SignUtil.sign(jsapiTicket, url);
    }

    @Scheduled(
           cron = "0/5 * * * * ?"
    )
    public void sign() {
//        log.error("quartz fuck");
    }
}
