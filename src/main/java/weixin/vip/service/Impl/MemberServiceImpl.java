package weixin.vip.service.Impl;

import com.alibaba.fastjson.JSON;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.assistant.dao.*;
import weixin.assistant.model.*;
import weixin.vip.service.MemberService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by worgen on 2015/9/9.
 */
@Service
public class MemberServiceImpl implements MemberService{
    private static final Logger log = Logger.getLogger("BusinessLog");

    @Autowired
    private VipBusinessMapper vipBusinessMapper;
    @Autowired
    private VipMemberMapper vipMemberMapper;
    @Autowired
    private VipMemberExtendMapper vipMemberExtendMapper;
    @Autowired
    private VipPayOrderMapper vipPayOrderMapper;
    @Autowired
    private VipRechargeOrderMapper vipRechargeOrderMapper;
    @Autowired
    private VipMessageMapper vipMessageMapper;
    @Autowired
    private WeixinGzhMapper weixinGzhMapper;

    @Override
    public VipBusiness getVipBusiness(int businessID) {
        return vipBusinessMapper.selectByPrimaryKey(businessID);
    }
    @Override
    public VipBusiness getVipBusinessByGzhID(int weixinGzhID) {

        WeixinGzh weixinGzh = weixinGzhMapper.selectByPrimaryKey(weixinGzhID);
        int businessId = weixinGzh.getBusinessId();

        VipBusiness vipBusiness = vipBusinessMapper.selectByPrimaryKey(businessId);
            return vipBusiness;
    }
    @Override
    public VipMember getVipMember(int memberID) {
        return vipMemberMapper.selectByPrimaryKey(memberID);
    }
    @Override
    public VipMember getVipMember(String openID) {
        VipMemberExample vipMemberExample = new VipMemberExample();
        vipMemberExample.createCriteria().andOpenIdEqualTo(openID);
        List<VipMember> vipMembers = vipMemberMapper.selectByExample(vipMemberExample);
        if( vipMembers.size() == 1 ){
            return vipMembers.get(0);
        }
        return null;
    }
    @Override
    public VipRechargeOrder getVipRechargeOrder(String  outTradeNo) {
        VipRechargeOrderExample vipRechargeOrderExample = new VipRechargeOrderExample();
        vipRechargeOrderExample.createCriteria().andOutTradeNoEqualTo(outTradeNo);
        List<VipRechargeOrder> vipRechargeOrders = vipRechargeOrderMapper.selectByExample(vipRechargeOrderExample);
        if( vipRechargeOrders.size() == 1 ){
            return vipRechargeOrders.get(0);
        }

        return null;
    }

    @Override
    public VipRechargeOrder getVipRechargeOrder(int vipRechargeOrderID) {
        return vipRechargeOrderMapper.selectByPrimaryKey(vipRechargeOrderID);
    }

    @Override
    public List<VipRechargeOrder> getVipRechargeOrders(VipRechargeOrderExample vipRechargeOrderExample) {
        return vipRechargeOrderMapper.selectByExample(vipRechargeOrderExample);
    }

//    @Override
//    public List<VipRechargeOrder> getSuccessVipRechargeOrders(int memberID) {
//        VipRechargeOrderExample vipRechargeOrderExample = new VipRechargeOrderExample();
//        vipRechargeOrderExample.createCriteria().
//                andMemberIdEqualTo(memberID).
//                andStatusEqualTo((short) 1);
//        return vipRechargeOrderMapper.selectByExample(vipRechargeOrderExample);
//    }

    @Override
    public int incVipMoneyInput(int memberID, int fee) {
        Map<String, Integer> paras = new HashMap<String, Integer>();
        paras.put("id", memberID);
        paras.put("fee", fee);
        vipMemberExtendMapper.incMoneyInputByExample(paras);

        log.info("money,input,add,"+memberID+","+fee);
        return 0;
    }

    @Override
    public int incVipMoneyOutput(int memberID, int fee) {
        Map<String, Integer> paras = new HashMap<String, Integer>();
        paras.put("id", memberID);
        paras.put("fee", fee);
        vipMemberExtendMapper.incMoneyOutputByExample(paras);
        log.info("money,output,add,"+memberID+","+fee);

        return 0;
    }

    @Override
    public boolean checkBalancePassword(int memberID, String password) {
        VipMember vipMember = getVipMember(memberID);
        if( vipMember == null ){
            log.error("vipMember null," + memberID);
            return false;
        }
        return vipMember.getPwd().equals(password);
    }

    @Override
    public int getBalance(int memberID) {
        VipMember vipMember = getVipMember(memberID);
        if( vipMember == null ){
            log.error("vipMember null," + memberID);
            return -1;
        }
        return vipMember.getMoneyInput() - vipMember.getMoneyOutput() - vipMember.getMoneyLock();
    }

    @Override
    public boolean balanceEnough(int memberID, int fee) {
        return getBalance(memberID) >= fee;
    }

    @Override
    public int costBalance(int memberID, int fee) {
        if( balanceEnough(memberID, fee) == false ){
            log.error("balance not enough,"+memberID+","+fee);
            return -1;
        }
        incVipMoneyOutput(memberID, fee);
        VipMember vipMember = getVipMember(memberID);
        //生成并插入订单
        VipPayOrder vipPayOrder = new VipPayOrder();
        vipPayOrder.setPayType(2);
        vipPayOrder.setCreateTime(new Date());
        vipPayOrder.setFinishTime(new Date());
        vipPayOrder.setStatus((short) 1);
        vipPayOrder.setFee(fee);
        vipPayOrder.setBusinessId(vipMember.getBusinessId());
        insertVipPayOrder(vipPayOrder);
        //发送通知
        String tailTel = vipMember.getTel().substring(vipMember.getTel().length()-4);
        DateTime date = new DateTime();
        String dateStr = date.toString("yyyy年MM月dd日HH点mm分");
        float balance = (float)getBalance(vipMember.getId())/100;
        float fFee = (float)fee/100;
        String content = "您的尾号"+tailTel+"会员卡于"+dateStr+"支出"+fFee+"元，当前余额"+balance+"元";
        sendMessage(vipMember.getBusinessId(), vipMember.getId(), "会员卡余额消费", content, 3);

        log.info("costBalance,"+memberID+","+fee+","+balance);
        return 0;
    }

    @Override
    public VipPayOrder getVipPayOrder(String outTradeNo) {
        VipPayOrderExample vipPayOrderExample = new VipPayOrderExample();
        vipPayOrderExample.createCriteria().andOutTradeNoEqualTo(outTradeNo);
        List<VipPayOrder> vipPayOrders = vipPayOrderMapper.selectByExample(vipPayOrderExample);
        if( vipPayOrders.size() == 1 ){
            return vipPayOrders.get(0);
        }
        return null;
    }

    @Override
    public VipPayOrder getVipPayOrder(int vipPayOrderID) {
        return vipPayOrderMapper.selectByPrimaryKey(vipPayOrderID);
    }

    @Override
    public List<VipPayOrder> getVipPayOrders(VipPayOrderExample vipPayOrderExample) {
        return vipPayOrderMapper.selectByExample(vipPayOrderExample);
    }
//    @Override
//    public List<VipPayOrder> getSuccessVipPayOrders(int memberID) {
//        VipPayOrderExample vipPayOrderExample = new VipPayOrderExample();
//        vipPayOrderExample.createCriteria().andMemberIdEqualTo(memberID).andStatusEqualTo((short) 1);
//
//        return vipPayOrderMapper.selectByExample(vipPayOrderExample);
//    }
    @Override
    public List<VipMessage> getVipMessages(int memberID) {
        VipMessageExample vipMessageExample = new VipMessageExample();
        vipMessageExample.createCriteria().andMemberIdEqualTo(memberID);
        return vipMessageMapper.selectByExample(vipMessageExample);
    }

    @Override
    public int insertVipMember(VipMember vipMember) {
        //如果已经有会员报错
        VipMemberExample vipMemberExample = new VipMemberExample();
        vipMemberExample.createCriteria().andOpenIdEqualTo(vipMember.getOpenId());
        if( vipMemberMapper.countByExample(vipMemberExample) > 0 ){
            log.error("vip member exist,"+ JSON.toJSONString(vipMember));
            return -1;
        }
        //没有插入
        vipMemberMapper.insertSelective(vipMember);
        return vipMember.getId();
    }

    @Override
    public int updateVipMember(VipMember vipMember) {
        //没有报错
        if( vipMemberMapper.selectByPrimaryKey(vipMember.getId()) == null ){
            log.error("vip member null,"+ JSON.toJSONString(vipMember));
            return -1;
        }
        vipMemberMapper.updateByPrimaryKeySelective(vipMember);
        return 0;
    }

    @Override
    public int changeVipPassword(int memberID, String newPassword) {
        VipMember vipMember = new VipMember();
        vipMember.setId(memberID);
        vipMember.setPwd(newPassword);
        updateVipMember(vipMember);
        return 0;
    }

    @Override
    public int insertVipRechargeOrder(VipRechargeOrder vipRechargeOrder) {
        vipRechargeOrderMapper.insert(vipRechargeOrder);
        return vipRechargeOrder.getId();
    }

    @Override
    public int updateVipRechargeOrder(VipRechargeOrder vipRechargeOrder) {
        vipRechargeOrderMapper.updateByPrimaryKeySelective(vipRechargeOrder);
        return 0;
    }

//    @Override
//    public int updateVipRechargeOrderSuccess(int rechargeOrderID) {
//        VipRechargeOrder vipRechargeOrder = new VipRechargeOrder();
//        vipRechargeOrder.setId(rechargeOrderID);
//        vipRechargeOrder.setStatus((short) 1);
//        updateVipRechargeOrder(vipRechargeOrder);
//        return 0;
//    }

    @Override
    public int vipRechargeOrderSuccess(String outTradeNo) {
        //更新状态，用户余额增加
        VipRechargeOrder vipRechargeOrder = getVipRechargeOrder(outTradeNo);
        vipRechargeOrder.setStatus((short) 1);
        vipRechargeOrder.setFinishTime(new Date());
        updateVipRechargeOrder(vipRechargeOrder);
        // 余额增加
        //update set
        incVipMoneyInput(vipRechargeOrder.getMemberId(), vipRechargeOrder.getFee());

        // 发送系统消息
        //发送通知
        VipMember vipMember = getVipMember(vipRechargeOrder.getMemberId());
        String tailTel = vipMember.getTel().substring(vipMember.getTel().length()-4);
        DateTime date = new DateTime();
        String dateStr = date.toString("yyyy年MM月dd日HH点mm分");
        float balance = (float)getBalance(vipMember.getId())/100;
        float fee = (float)vipRechargeOrder.getFee()/100;
        String content = "您的尾号"+tailTel+"会员卡于"+dateStr+"存入"+fee+"元，当前余额"+balance+"元";
        sendMessage(vipMember.getBusinessId(), vipMember.getId(), "微支付转入", content, 2);

        log.info("vipRechargeOrderSuccess,"+outTradeNo+","+vipMember.getId()+","+fee+","+balance);
       return 0;
    }

//    @Override
//    public int updateVipRechargeOrderFail(int rechargeOrderID) {
//        VipRechargeOrder vipRechargeOrder = new VipRechargeOrder();
//        vipRechargeOrder.setId(rechargeOrderID);
//
//        vipRechargeOrder.setStatus((short) 2);
//        updateVipRechargeOrder(vipRechargeOrder);
//        return 0;
//    }

    @Override
    public int vipRechargeOrderFail(String outTradeNo) {
        VipRechargeOrderExample vipRechargeOrderExample = new VipRechargeOrderExample();
        vipRechargeOrderExample.createCriteria().andOutTradeNoEqualTo(outTradeNo);
        VipRechargeOrder vipRechargeOrder = new VipRechargeOrder();
        // vipRechargeOrder.setId(rechargeOrderID);
        vipRechargeOrder.setFinishTime(new Date());
        vipRechargeOrder.setStatus((short) 2);
        vipRechargeOrderMapper.updateByExampleSelective(vipRechargeOrder, vipRechargeOrderExample);
        return 0;
    }

    @Override
    public int vipRecharge(int memberID, int num) {
        return 0;
    }

    @Override
    public int insertVipPayOrder(VipPayOrder vipPayOrder) {
        vipPayOrderMapper.insert(vipPayOrder);
        return vipPayOrder.getId();
    }

    @Override
    public int updateVipPayOrder(VipPayOrder vipPayOrder) {
        vipPayOrderMapper.updateByPrimaryKeySelective(vipPayOrder);
        return 0;
    }

//    @Override
//    public int updateVipPayOrderSuccess(int payOrderID) {
//        VipPayOrder vipPayOrder = new VipPayOrder();
//        vipPayOrder.setId(payOrderID);
//        vipPayOrder.setStatus((short) 1);
//        updateVipPayOrder(vipPayOrder);
//        return 0;
//    }
//
//    @Override
//    public int updateVipPayOrderFail(int payOrderID) {
//        VipPayOrder vipPayOrder = new VipPayOrder();
//        vipPayOrder.setId(payOrderID);
//        vipPayOrder.setStatus((short) 2);
//        updateVipPayOrder(vipPayOrder);
//        return 0;
//    }

    @Override
    public int vipPayOrderSuccess(String outTradeNo) {
        VipPayOrder vipPayOrder = getVipPayOrder(outTradeNo);
        vipPayOrder.setStatus((short) 1);
        vipPayOrder.setFinishTime(new Date());
        updateVipPayOrder(vipPayOrder);
        //发送通知
        VipMember vipMember = getVipMember(vipPayOrder.getMemberId());
        String tailTel = vipMember.getTel().substring(vipMember.getTel().length()-4);
        DateTime date = new DateTime();
        String dateStr = date.toString("yyyy年MM月dd日HH点mm分");
        float balance = (float)getBalance(vipMember.getId())/100;
        float fee = (float)vipPayOrder.getFee()/100;
        String content = "您的尾号"+tailTel+"会员卡于"+dateStr+"支出"+fee+"元，当前余额"+balance+"元";
        sendMessage(vipMember.getBusinessId(), vipMember.getId(), "微支付消费", content, 3);

        log.info("vipPayOrderSuccess,"+outTradeNo+","+vipMember.getId()+","+fee+","+balance);
        return 0;
    }

    @Override
    public int vipPayOrderFail(String outTradeNo) {
        VipPayOrder vipPayOrder = getVipPayOrder(outTradeNo);
        vipPayOrder.setStatus((short) 2);
        vipPayOrder.setFinishTime(new Date());

        updateVipPayOrder(vipPayOrder);
        return 0;
    }

    @Override
    public int vipPay(int memberID, int num, int type) {
        return 0;
    }

    @Override
    public int sendMessage(int businessID, int memberID, String title, String content, int type) {
        VipMessage vipMessage = new VipMessage();
        vipMessage.setMemberId(memberID);
        vipMessage.setTitle(title);
        vipMessage.setContent(content);
        vipMessage.setType(type);
        vipMessage.setCreateTime(new Date());
        vipMessageMapper.insertSelective(vipMessage);

        log.info("sendMessage,"+businessID+","+memberID+","+title+","+content);
        return 0;
    }

    @Override
    public int sendMessage(int businessID, List<Integer> memberIDs, String title, String content) {
        for(Integer memberID : memberIDs){
            sendMessage(businessID, memberID, title, content, 1);
        }
        return 0;
    }

    @Override
    public int broadCastMessage(int businessID, String title, String content) {
        //查找所有成員
        //List<>
        return 0;
    }

    @Override
    public int getBusinnessID(int weixinGzhID) {

        WeixinGzh weixinGzh = weixinGzhMapper.selectByPrimaryKey(weixinGzhID);
        int businessId = weixinGzh.getBusinessId();

        VipBusiness vipBusiness = vipBusinessMapper.selectByPrimaryKey(businessId);
        return vipBusiness.getId();
    }

    @Override
    public int getMemberID(String openID) {
        VipMemberExample vipMemberExample = new VipMemberExample();
        vipMemberExample.createCriteria().andOpenIdEqualTo(openID);
        List<VipMember> vipMembers = vipMemberMapper.selectByExample(vipMemberExample);
        if( vipMembers.size() == 1 ){
            return vipMembers.get(0).getId();
        }
        return -1;
    }
}
