//package member.card;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//
//import vip.card.dao.SignMapper;
//import vip.card.model.*;
//import vip.card.service.SignService;
//import org.junit.Before;
//import org.junit.Test;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.support.ClassPathXmlApplicationContext;
//
///**
// * Created by tianbaochao on 2015/5/13.
// */
//public class IndexTest {
//	ApplicationContext context;
//    @Before
//    public void before(){
//        //@SuppressWarnings("resource")
//        context = new ClassPathXmlApplicationContext(new String[]{"classpath:conf/spring.xml"
//                ,"classpath:conf/spring-mybatis.xml"});
//        //userService = (UserService) context.getBean("userServiceImpl");
//    }
//
//    @Test
//    public void toIndex(){
//    	SignService signService = (SignService) context.getBean("signServiceImpl");
//
//
//		short status = 1;
//		int count = 5;
//		Date createTime = new Date();
//		String integralDetails = "签到所得积分";
//
//		Sign sign = new Sign();
//
//
//		sign.setStatus(status);
//		sign.setSignTime(createTime);
//		sign.setIntegralDetails(integralDetails);
//		sign.setCount(count);
//
//
//		int s = signService.insertSign(sign);
//		System.out.println("-------"+s+"---------");
//
//
//    }
//
//	@Test
//	public void test2() throws ParseException {
//		SignService signService = (SignService) context.getBean("signServiceImpl");
//
//		int memberId = 1;
//		int businessId = 1;
//
//		//根据会员id 和 商户id 查 会员信息
//		/*MemberExample memberExample = new MemberExample();
//		memberExample.createCriteria().andIdEqualTo(memberId).andBusinessIdEqualTo(businessId);
//
//		List<Member> memberList = signService.getMember(memberExample);*/
//
//
//		//根据会员id 和 商户id 更新会员信息
//		/*Member vip = new Member();
//		vip.setAddr("哇哈哈~~");
//
//		MemberExample memberExample = new MemberExample();
//		memberExample.createCriteria().andIdEqualTo(memberId).andBusinessIdEqualTo(businessId);
//
//		int i = signService.updateMember(vip, memberExample);*/
//
//
//		//根据日期范围查询 充值和付款订单信息 （我的账单）
//		/*SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
//		Date dateMin = formatDate.parse("2015-08-01");
//		Date dateMax = formatDate.parse("2015-08-31");
//		PayOrderExample payOrderExample = new PayOrderExample();
//		payOrderExample.createCriteria().andMemberIdEqualTo(memberId).andBusinessIdEqualTo(businessId).andCreateTimeGreaterThan(dateMin).andCreateTimeLessThan(dateMax);
//
//		RechargeOrderExample rechargeOrderExample = new RechargeOrderExample();
//		rechargeOrderExample.createCriteria().andMemberIdEqualTo(memberId).andBusinessIdEqualTo(businessId).andCreateTimeGreaterThan(dateMin).andCreateTimeLessThan(dateMax);
//
//		List<PayOrder> payOrderList = signService.getPayOrder(payOrderExample);
//		List<RechargeOrder> rechargeOrderList = signService.getRechargeOrder(rechargeOrderExample);*/
//
//
//
//
//
//		//System.out.println(payOrderList);
//
//
//	}
//}
