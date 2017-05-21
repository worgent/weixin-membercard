//package weixin.assistant.service;
//
//import iwpoo.xigua.model.Account;
//
//import java.util.List;
//
///**
// * Created by worgen on 2015/8/17.
// */
//public interface AssistantService {
//
//
//    //根据日期，分类，查询榜单数据
//
//    //根据分类，时间范围，查询指数排行文章
//
//
//    //添加素材,
//    public int addMaterial();
//    //同步素材,调用微信接口
//    public int syncMaterial();
//    //获得账号素材列表
//    public List getMaterialList();
//    //定时群发
//    public int broadcastByTime();
//
//
//    //账号绑定公众号,记录公众号信息包括授权信息，并创建账号
//    public int bindGzh(Account account, String authcode, String expiresIn);
//    //账号解绑公众号，账号状态置为解绑
//    public int unbindGzh(Account account, int gzhID);
//    //关注公众号
//    public int attentionGzh();
//    //取消关注
//    public int unattentionGzh();
//}
