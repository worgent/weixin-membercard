package weixin.vip.controller;

import base.util.SendMsg_webchinese;
import com.alibaba.fastjson.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.assistant.model.VipBusiness;
import weixin.assistant.util.TimeUtil;
import weixin.vip.Util.MathUtil;
import weixin.vip.constants.VipConstants;
import weixin.vip.enums.SessionAttributeEnum;
import weixin.vip.service.MemberService;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Random;

/**
 * Created by worgen on 2015/9/10.
 */
@Controller
@RequestMapping("/check_code")
public class CheckCodeController {
    private static final Logger log = Logger.getLogger("BusinessLog");

    @Autowired
    private MemberService memberService;

    @RequestMapping("getSMsCode")
    public @ResponseBody
    JSONObject getSMsCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String phone = request.getParameter("phone");
        String type = request.getParameter("type");
        String paraBusinessID = request.getParameter("business_id");
        int businessID = Integer.parseInt(paraBusinessID);
        VipBusiness vipBusiness = memberService.getVipBusiness(businessID);
        JSONObject jsonRet = new JSONObject();
        jsonRet.put("errno", 0);
        if( vipBusiness == null ){
            log.error("vipBusiness null");
            jsonRet.put("errno", 1);
            jsonRet.put("error", "发生错误，请重试");
            return jsonRet;
        }
        //随机4位数
        int rand = (int) (Math.random()*1000)+1000;
        String msg = "";
        long lastSendTime = 0;
        String lastPhone = "";
        HttpSession session = request.getSession(true);
        log.debug("getSMsCode,"+type+","+rand+","
                + request.getRemoteHost() + "," + request.getRequestURL() + ","
                + request.getServerName() + "," + request.getRemoteAddr());
        if( type != null &&
                type.isEmpty() == false &&
                type.equals(SessionAttributeEnum.REGIST_VIP_CODE.getKey()) ){
            //判断短信发送间隔
            lastSendTime = MathUtil.parseLong((session.getAttribute(SessionAttributeEnum.REGIST_VIP_CODE_TIME.getKey())));
           // lastPhone = session.getAttribute(SessionAttributeEnum.REGIST_VIP_PHONE.getKey()).toString();
            msg += rand;
            msg += "（动态验证码），您于";
            msg += new Date().toString();
            msg += "申请"+ vipBusiness.getName() +"微信会员卡，";
            msg += "请在10分钟内输入该验证码。";
        }
        else if(type != null &&
                type.isEmpty() == false &&
                type.equals(SessionAttributeEnum.VIP_CHANGE_PASSWORD_CODE.getKey()) ){
            lastSendTime = MathUtil.parseLong(session.getAttribute(SessionAttributeEnum.VIP_CHANGE_PASSWORD_CODE_TIME.getKey()));
          //  lastPhone = session.getAttribute(SessionAttributeEnum.VIP_CHANGE_PASSWORD_PHONE.getKey()).toString();
            msg += rand;
            msg += "（动态验证码），您于";
            msg += new Date().toString();
            msg += "申请修改"+ vipBusiness.getName() +"微信会员卡支付密码，";
            msg += "请在10分钟内输入该验证码。";
        }
        else{
            lastSendTime = MathUtil.parseLong(session.getAttribute(SessionAttributeEnum.OTHER_CODE_TIME.getKey()));
            //lastPhone = session.getAttribute(SessionAttributeEnum.OTHER_PHONE.getKey()).toString();
            msg += rand;
            msg += "（动态验证码），您于";
            msg += new Date().toString();
            msg += "申请"+ vipBusiness.getName() +"动态验证码，";
            msg += "请在10分钟内输入该验证码。";
        }
        //i
        if( lastSendTime + VipConstants.sendMsgInterval > TimeUtil.now() ){
            log.error("sendMsg too quick");
            jsonRet.put("errno", 2);
            jsonRet.put("error", "发生错误，请重试");
            return jsonRet;
        }
        //发送短信
        int result = SendMsg_webchinese.sendMsgGBK(phone, msg);
        if( result <= 0 ){
            log.error("sendMsgGBK error,"+phone+","+msg+","+result);
            jsonRet.put("errno", 31);
            jsonRet.put("error", "发生错误，请重试");
            return jsonRet;
        }
        //存入session和发送延迟
        if( type != null &&
                type.isEmpty() == false &&
                type.equals(SessionAttributeEnum.REGIST_VIP_CODE.getKey()) ){
            session.setAttribute(SessionAttributeEnum.REGIST_VIP_CODE.getKey(),
                    rand);
            session.setAttribute(SessionAttributeEnum.REGIST_VIP_PHONE.getKey(),
                    phone);
            session.setAttribute(SessionAttributeEnum.REGIST_VIP_CODE_TIME.getKey(),
                    TimeUtil.now());
        }
        else if(type != null &&
                type.isEmpty() == false &&
                type.equals(SessionAttributeEnum.VIP_CHANGE_PASSWORD_CODE.getKey()) ){
            session.setAttribute(SessionAttributeEnum.VIP_CHANGE_PASSWORD_CODE.getKey(),
                    rand);
            session.setAttribute(SessionAttributeEnum.VIP_CHANGE_PASSWORD_PHONE.getKey(),
                    phone);
            session.setAttribute(SessionAttributeEnum.VIP_CHANGE_PASSWORD_CODE.getKey(),
                    TimeUtil.now());
        }else {
            session.setAttribute(SessionAttributeEnum.OTHER_CODE.getKey(), rand);
            session.setAttribute(SessionAttributeEnum.OTHER_PHONE.getKey(), phone);
            session.setAttribute(SessionAttributeEnum.OTHER_CODE_TIME.getKey(),
                    TimeUtil.now());
        }
        return jsonRet;
    }

    @RequestMapping("getPictureCode")
    public void getPictureCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        createCheckCode(request, response);
    }

    /*该方法主要作用是获得随机生成的颜色*/
    private Color getRandColor(int s,int e){
        Random random=new Random ();
        if(s>255) s=255;
        if(e>255) e=255;
        int r,g,b;
        r=s+random.nextInt(e-s);    //随机生成RGB颜色中的r值
        g=s+random.nextInt(e-s);    //随机生成RGB颜色中的g值
        b=s+random.nextInt(e-s);    //随机生成RGB颜色中的b值
        return new Color(r,g,b);
    }
    private void createCheckCode(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置不缓存图片
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "No-cache");
        response.setDateHeader("Expires", 0);

        String type = request.getParameter("type"); //用于区分不同的页面来源，如创建文章、创建活动、创建留言


        //指定生成的响应图片,一定不能缺少这句话,否则错误.
        response.setContentType("image/jpeg");
        int width=86,height=28;     //指定生成验证码的宽度和高度
        BufferedImage image=new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB); //创建BufferedImage对象,其作用相当于一图片
        Graphics g=image.getGraphics();     //创建Graphics对象,其作用相当于画笔
        Graphics2D g2d=(Graphics2D)g;       //创建Grapchics2D对象
        Random random=new Random();
        Font mfont=new Font("楷体",Font.BOLD,20); //定义字体样式
        g.setColor(getRandColor(200,250));
        g.fillRect(0, 0, width, height);    //绘制背景
        g.setFont(mfont);                   //设置字体
        g.setColor(getRandColor(180,200));

        //绘制100条颜色和位置全部为随机产生的线条,该线条为2f
        for(int i=0;i<100;i++){
            int x=random.nextInt(width-1);
            int y=random.nextInt(height-1);
            int x1=random.nextInt(6)+1;
            int y1=random.nextInt(12)+1;
            BasicStroke bs=new BasicStroke(2f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL); //定制线条样式
            Line2D line=new Line2D.Double(x,y,x+x1,y+y1);
            g2d.setStroke(bs);
            g2d.draw(line);     //绘制直线
        }
        //输出由英文，数字，和中文随机组成的验证文字，具体的组合方式根据生成随机数确定。
        String sRand="";
        String ctmp="";
        int itmp=0;
        //制定输出的验证码为四位
        for(int i=0;i<4;i++){
            switch(random.nextInt(3)){
                case 1:     //生成A-Z的字母
//                    itmp=random.nextInt(26)+65;
//                    ctmp=String.valueOf((char)itmp);
//                    break;
                case 2:     //生成汉字
//                    String[] rBase={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
//                    //生成第一位区码
//                    int r1=random.nextInt(3)+11;
//                    String str_r1=rBase[r1];
//                    //生成第二位区码
//                    int r2;
//                    if(r1==13){
//                        r2=random.nextInt(7);
//                    }else{
//                        r2=random.nextInt(16);
//                    }
//                    String str_r2=rBase[r2];
//                    //生成第一位位码
//                    int r3=random.nextInt(6)+10;
//                    String str_r3=rBase[r3];
//                    //生成第二位位码
//                    int r4;
//                    if(r3==10){
//                        r4=random.nextInt(15)+1;
//                    }else if(r3==15){
//                        r4=random.nextInt(15);
//                    }else{
//                        r4=random.nextInt(16);
//                    }
//                    String str_r4=rBase[r4];
//                    //将生成的机内码转换为汉字
//                    byte[] bytes=new byte[2];
//                    //将生成的区码保存到字节数组的第一个元素中
//                    String str_12=str_r1+str_r2;
//                    int tempLow=Integer.parseInt(str_12, 16);
//                    bytes[0]=(byte) tempLow;
//                    //将生成的位码保存到字节数组的第二个元素中
//                    String str_34=str_r3+str_r4;
//                    int tempHigh=Integer.parseInt(str_34, 16);
//                    bytes[1]=(byte)tempHigh;
//                    ctmp=new String(bytes);
//                    break;
                default:
                    itmp=random.nextInt(10)+48;
                    ctmp=String.valueOf((char)itmp);
                    break;
            }
            sRand+=ctmp;
            Color color=new Color(20+random.nextInt(110),20+random.nextInt(110),random.nextInt(110));
            g.setColor(color);
            //将生成的随机数进行随机缩放并旋转制定角度 PS.建议不要对文字进行缩放与旋转,因为这样图片可能不正常显示
            /*将文字旋转制定角度*/
            Graphics2D g2d_word=(Graphics2D)g;
            AffineTransform trans=new AffineTransform();
            trans.rotate((45)*3.14/180,15*i+8,7);
            /*缩放文字*/
            float scaleSize=random.nextFloat()+0.8f;
            if(scaleSize>1f) scaleSize=1f;
            trans.scale(scaleSize, scaleSize);
            g2d_word.setTransform(trans);
            g.drawString(ctmp, 15*i+18, 14);
        }
        HttpSession session = request.getSession(true);
        log.error("session key," + session.getAttribute(SessionAttributeEnum.REGIST_VIP_CODE.getKey()));
        log.info("请求路径："+request.getRemoteHost()+"  ||  "+request.getRequestURL()+"  ||  "+request.getServerName()+"  ||  "+request.getRemoteAddr());
//        if (type != null && !type.isEmpty()) {
//            log.info("获取验证码的来源是："+type+ "获取的验证码是："+sRand);
//            if (type.trim().equals(SessionAttributeEnum.REGIST_VIP_CODE.getKey())) {
//                log.info("获取验证码的来源是："+type);
//                session.setAttribute(SessionAttributeEnum.REGIST_VIP_CODE.getKey(), sRand);
//                session.setAttribute("jiangrushe", sRand);
//                session.setAttribute("wanglihuan", "jiangrushe");
//                log.info("从seesion中后去验证码"+request.getSession().getAttribute(SessionAttributeEnum.REGIST_VIP_CODE.getKey()));
//            } else {
//                session.setAttribute(SessionAttributeEnum.OTHER.getKey(), sRand);
//            }
//        } else {
//            session.setAttribute(SessionAttributeEnum.OTHER.getKey(), sRand);
//        }
        g.dispose();    //释放g所占用的系统资源
        ImageIO.write(image, "JPEG", response.getOutputStream()); //输出图片
    }
}
