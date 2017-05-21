package weixin.vip.service;

import weixin.assistant.model.*;

import java.util.List;

/**
 * Created by worgen on 2015/9/9.
 */
public interface MemberService {
    //根据businessId获取商户信息
    public VipBusiness getVipBusiness(int businessID);
    public VipBusiness getVipBusinessByGzhID(int weixinGzhID);

    //根据memberID获取会员基本信息
    public VipMember getVipMember(int memberID);
    public VipMember getVipMember(String openID);

    //根据memberID获取会员充值记录
    public VipRechargeOrder getVipRechargeOrder(String outTradeNo);
    public VipRechargeOrder getVipRechargeOrder(int vipRechargeOrderID);
    public List<VipRechargeOrder> getVipRechargeOrders(VipRechargeOrderExample vipRechargeOrderExampleD);
    public int incVipMoneyInput(int memberID, int fee);
    public int incVipMoneyOutput(int memberID, int fee);
    //判断余额是否足够
    public boolean checkBalancePassword(int memberID, String password);
    public int getBalance(int memberID);
    public boolean balanceEnough(int memberID, int fee);
    public int costBalance(int memberID, int fee);
    //根据memberID获取会员消费记录
    public VipPayOrder getVipPayOrder(String outTradeNo);
    public VipPayOrder getVipPayOrder(int vipPayOrderID);
    public List<VipPayOrder> getVipPayOrders(VipPayOrderExample vipPayOrderExample);

    //根据memberID获取会员消息记录
    public List<VipMessage> getVipMessages(int memberID);


    //加入会员，返回ID
    public int insertVipMember(VipMember vipMember);
    public int updateVipMember(VipMember vipMember);
    //修改密码
    public int changeVipPassword(int memberID, String newPassword);

    //插入会员充值记录
    public int insertVipRechargeOrder(VipRechargeOrder vipRechargeOrder);
    //更新充值信息
    public int updateVipRechargeOrder(VipRechargeOrder vipRechargeOrder);
   // public int updateVipRechargeOrderSuccess(int rechargeOrderID);
    public int vipRechargeOrderSuccess(String outTradeNo);
  //  public int updateVipRechargeOrderFail(int rechargeOrderID);
    public int vipRechargeOrderFail(String outTradeNo);
    //会员充值
    public int vipRecharge(int memberID, int num);


    //插入会员消费记录
    public int insertVipPayOrder(VipPayOrder vipPayOrder);
    public int updateVipPayOrder(VipPayOrder vipPayOrder);
    //更新会员消费成功状态
  //  public int updateVipPayOrderSuccess(int payOrderID);
  //  public int updateVipPayOrderFail(int payOrderID);
    public int vipPayOrderSuccess(String outTradeNo);
    public int vipPayOrderFail(String outTradeNo);

    //会员消费
    public int vipPay(int memberID, int num, int type);

    //
    public int sendMessage(int businessID, int memberID, String title, String content, int type);
    public int sendMessage(int businessID, List<Integer> memberIDs, String title, String content);
    public int broadCastMessage(int businessID, String title, String content);

    /////
    //根据weixin_gzh_id获得businessID
    public int getBusinnessID(int weixinGzhID);
    //根据openID获得memberID
    public int getMemberID(String openID);
}
