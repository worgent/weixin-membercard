package weixin.vip.controller;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import weixin.assistant.dao.*;
import weixin.assistant.model.*;
import weixin.assistant.util.MD5;
import weixin.vip.model.Page;
import weixin.vip.service.AdminService;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by worgen on 2015/9/9.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
    private static final Log log = LogFactory.getLog(AdminController.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private VipMemberExtendMapper vipMemberExtendMapper;
    @Autowired
    private VipMemberMapper vipMemberMapper;
    @Autowired
    private AccountMapper accountMapper;
    @Autowired
    private VipMemberExtendMapper getVipMemberExtendMapper;
    @Autowired
    private VipBusinessMapper vipBusinessMapper;

    @RequestMapping("toIndex")
    public String toIndexPage(HttpServletRequest request,Model model) {
        String userName = (String) request.getSession().getAttribute("userName");
        if(userName == null || userName.equals("")){
            return "vip/admin/login";
        }
        Integer accountId = (Integer) request.getSession().getAttribute("accountId");
        model.addAttribute("userName",userName);
        model.addAttribute("accountId",accountId);
        return "vip/admin/index";
    }

    @RequestMapping("toLogin")
    public String toLoginPage(HttpServletRequest request,Model model) {
        Enumeration em = request.getSession().getAttributeNames();
        while(em.hasMoreElements()){
            request.getSession().removeAttribute(em.nextElement().toString());
        }
        return "vip/admin/login";
    }
    @RequestMapping("toRegisterPage")
    public String toRegisterPage(Model model) {

        return "vip/admin/register";
    }
    @RequestMapping("toHomePage")
    public String toHomePage(Model model) {

        return "vip/admin/home";
    }

    @RequestMapping("toCzSearch")
    public String toCzSearchPage(String account,Model model) {
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID",account);
        return "vip/admin/cz-search";
    }
    @RequestMapping("toXfSearch")
    public String toXfSearchPage(String account,Model model) {
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID",account);
        return "vip/admin/xf-search";
    }
    @RequestMapping("toUserCzStatistics")
    public String toUserCzStatisticsPage(String account,Model model) {
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID",account);
        return "vip/admin/user-cz-statistics";
    }

    @RequestMapping("toUserXfStatistics")
    public String toUserXfStatisticsPage(String account,Model model) {
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID",account);
        return "vip/admin/user-xf-statistics";
    }

    @RequestMapping("toVipDataStatistics")
    public String toVipDataStatisticsPage(String account,Model model) {
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID",account);
        return "vip/admin/vip-data-statistics";
    }

    //会员列表中 会员总数和 当天新加会员数
    @RequestMapping("toVipList")
    public String toVipListPage(String account,Model model) {
        Map<String, Object> searchMap = new HashMap<String, Object>();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String createTime = formatDate.format(date);
        searchMap.put("createTime",createTime);
        Integer todayCount = vipMemberExtendMapper.vipTotalCount(searchMap);

        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);

        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID",account);
        model.addAttribute("todayCount",todayCount);
        return "vip/admin/vip-list";
    }

    private List getBusinessInfo(String account){
        int accountID = Integer.parseInt(account);
        VipBusinessExample vipBusinessExample = new VipBusinessExample();
        vipBusinessExample.createCriteria().andAccountIdEqualTo(accountID);

        List<VipBusiness> vipBusinessList = vipBusinessMapper.selectByExample(vipBusinessExample);
        return vipBusinessList;
    }

    //充值查询
    @RequestMapping("czSearch")
    public String czSearch(HttpServletRequest request, Model model) throws IOException {

        Map<String, Object> searchMap = new HashMap<String, Object>();
        String vipName = request.getParameter("vipName");
        String mendian = request.getParameter("mendian");
        String vipPhone = request.getParameter("vipPhone");
        String czTime = request.getParameter("czTime");
        String czMoneyMinStr = request.getParameter("czMoneyMin");
        String czMoneyMaxStr = request.getParameter("czMoneyMax");
        String sPageNo = request.getParameter("pageNo");
        String choseType = request.getParameter("choseType");
        String account = request.getParameter("account");
        //int accountID = Integer.parseInt(account);
        searchMap.put("choseType",choseType);
        searchMap.put("accountID",account);
        if (sPageNo == null || sPageNo.equals("")) {
            sPageNo = "1";
        }
        Integer pageNo = Integer.parseInt(sPageNo);

        int czMoneyMin;
        int czMoneyMax;
        short status = 1;

        searchMap.put("status", status);
        if (vipName != null && vipName.isEmpty() == false) {
            searchMap.put("vipName", vipName);
            model.addAttribute("vipName", vipName);
        }
        if (!mendian.equals("0") && !mendian.isEmpty()) {
            int businessID = Integer.parseInt(mendian);
            VipBusiness vipBusiness = vipBusinessMapper.selectByPrimaryKey(businessID);
            String businessName = vipBusiness.getName();
            searchMap.put("businessName", businessName);
            model.addAttribute("businessID", businessID);
            model.addAttribute("businessName",businessName);
        }
        if (czTime != null && !czTime.isEmpty()) {
            searchMap.put("czFinishTime", czTime);
            model.addAttribute("czTime", czTime);
        }

        if (vipPhone != null && !vipPhone.isEmpty()) {
            searchMap.put("tel", vipPhone);
            model.addAttribute("vipPhone", vipPhone);
        }

        searchMap.put("pageNo", pageNo);

        log.info("searchMap==println" + JSONObject.toJSONString(searchMap));
        Page page = adminService.queryForList(searchMap);

        if (czMoneyMinStr != "" || czMoneyMaxStr != "") {
            List newList = new ArrayList();
            List czSearchList = page.getList();
            czMoneyMin = Integer.parseInt(czMoneyMinStr);
            czMoneyMax = Integer.parseInt(czMoneyMaxStr);
            for(int i=0;i<czSearchList.size();i++){
                Map listMap = (Map) czSearchList.get(i);
                Iterator iterator = listMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    if (key.equals("czMoneyMapKey")) {
                        Float czMoneyMapValue = (Float) listMap.get(key);
                        if(czMoneyMapValue >= czMoneyMin && czMoneyMapValue<=czMoneyMax){
                            newList.add(listMap);
                        }
                    }
                }
            }
            page.setList(newList);
            page.setTotalCount(newList.size());
            //model.addAttribute("page", page);
            model.addAttribute("czMoneyMin", czMoneyMinStr);
            model.addAttribute("czMoneyMax", czMoneyMaxStr);
        }

        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("page", page);
        model.addAttribute("accountID", account);
        return "vip/admin/cz-search";
    }

    //消费信息查询
    @RequestMapping("xfSearch")
    public String xfSearch(HttpServletRequest request,Model model) {
        Map<String, Object> searchMap = new HashMap<String, Object>();
        String vipName = request.getParameter("vipName");
        String mendian = request.getParameter("mendian");
        String vipPhone = request.getParameter("vipPhone");
        String xfTime = request.getParameter("xfTime");
        String payType = request.getParameter("payType");
        String xfMoneyMinStr = request.getParameter("xfMoneyMin");
        String xfMoneyMaxStr = request.getParameter("xfMoneyMax");
        String sPageNo = request.getParameter("pageNo");
        String choseType = request.getParameter("choseType");
        String account = request.getParameter("account");
        searchMap.put("accountID",account);
        searchMap.put("choseType",choseType);
        if (sPageNo == null || sPageNo.equals("")) {
            sPageNo = "1";
        }
        Integer pageNo = Integer.parseInt(sPageNo);
        int xfMoneyMin;
        int xfMoneyMax;
        short status = 1;
        searchMap.put("status", status);
        if (vipName != null && vipName.isEmpty() == false) {
            searchMap.put("vipName", vipName);
            model.addAttribute("vipName", vipName);
        }
        if (!mendian.equals("0") && !mendian.isEmpty()) {
            int businessID = Integer.parseInt(mendian);
            VipBusiness vipBusiness = vipBusinessMapper.selectByPrimaryKey(businessID);
            String businessName = vipBusiness.getName();
            searchMap.put("businessName", businessName);
            model.addAttribute("businessID", businessID);
            model.addAttribute("businessName", businessName);
        }

        if (xfTime != null && !xfTime.isEmpty()) {
            searchMap.put("xfFinishTime", xfTime);
            model.addAttribute("xfTime", xfTime);
        }

        if (vipPhone != null && !vipPhone.isEmpty()) {
            searchMap.put("tel", vipPhone);
            model.addAttribute("vipPhone", vipPhone);
        }
        if (payType != null && !payType.isEmpty()) {
            searchMap.put("payType", payType);
            model.addAttribute("payType", payType);
        }
        searchMap.put("pageNo", pageNo);
        log.info("println" + JSONObject.toJSONString(searchMap));
        Page page = adminService.queryForList(searchMap);

        if (xfMoneyMinStr != "" || xfMoneyMaxStr != "") {
            List newList = new ArrayList();
            List xfSearchList = page.getList();
            xfMoneyMin = Integer.parseInt(xfMoneyMinStr);
            xfMoneyMax = Integer.parseInt(xfMoneyMaxStr);
            for(int i=0;i<xfSearchList.size();i++){
                Map listMap = (Map) xfSearchList.get(i);
                Iterator iterator = listMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    if (key.equals("xfMoneyMapKey")) {
                        Float xfMoneyMapValue = (Float) listMap.get(key);
                        if(xfMoneyMapValue >= xfMoneyMin && xfMoneyMapValue<=xfMoneyMax){
                            newList.add(listMap);
                        }
                    }
                }
            }
            page.setList(newList);
            page.setTotalCount(newList.size());
            model.addAttribute("xfMoneyMin", xfMoneyMin);
            model.addAttribute("xfMoneyMax", xfMoneyMax);
        }
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("page", page);
        model.addAttribute("accountID", account);
        return "vip/admin/xf-search";
    }

    //查询会员信息
    @RequestMapping("searchVipInfo")
    public String searchVipInfo(HttpServletRequest request,Model model,String mendian,String account) throws Exception {

        Map<String, Object> searchMap = new HashMap<String, Object>();
        Map<String, Object> countMap = new HashMap<String, Object>();

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String createTime = formatDate.format(date);

        String searchName = request.getParameter("searchName");
        Integer searchType = Integer.parseInt(request.getParameter("searchType"));
        String sPageNo = request.getParameter("pageNo");
        if (sPageNo == null || sPageNo.equals("")) {
            sPageNo = "1";
        }
        Integer pageNo = Integer.parseInt(sPageNo);
        searchMap.put("searchType",searchType);
        searchMap.put("searchName",searchName);
        searchMap.put("pageNo",pageNo);
        Integer memberCount=0;
        if(!mendian.equals("0")){
            int businessID = Integer.parseInt(mendian);
            searchMap.put("businessID",businessID);
            model.addAttribute("businessID", businessID);

        }else{
            searchMap.put("businessID",0);
        }
        countMap.put("createTime",createTime);
        //新增会员数
        Integer todayCount = vipMemberExtendMapper.vipTotalCount(countMap);

        Page page = adminService.vipQueryForList(searchMap);

        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("page", page);
        model.addAttribute("accountID", account);
        //查询条件回显
        model.addAttribute("searchName", searchName);
        model.addAttribute("searchType", searchType);
        model.addAttribute("memberCount",memberCount);
        model.addAttribute("todayCount",todayCount);
        return "vip/admin/vip-list";
    }

    //修改会员信息
    @RequestMapping("updateVipInfo")
    public String updateVipInfo(HttpServletRequest request) throws Exception {

        int vipId = Integer.parseInt(request.getParameter("vipNumWin"));
        String vipName = request.getParameter("vipNameWin");
        String status = request.getParameter("vipStatusWin");
        String account = request.getParameter("account");

        short vipStatus = Short.parseShort(status);

        String searchName = request.getParameter("searchName");
        Integer searchType = Integer.parseInt(request.getParameter("searchType"));

        VipMember vipMember = new VipMember();
        vipMember.setName(vipName);
        vipMember.setStatus(vipStatus);

        VipMemberExample vipMemberExample = new VipMemberExample();
        vipMemberExample.createCriteria().andIdEqualTo(vipId);

        vipMemberMapper.updateByExampleSelective(vipMember, vipMemberExample);

        String sName = new String(searchName.getBytes("UTF-8"),"iso-8859-1");
        return "redirect:/admin/searchVipInfo.do?searchName="+sName+"&searchType="+searchType+"&mendian="+"0"+"&account="+account;
    }


    //删除会员 暂时未使用
    @RequestMapping("deleteVip")
    public @ResponseBody int deleteVip(HttpServletRequest request){

        int id = Integer.parseInt(request.getParameter("vipId"));
        int responseText = vipMemberMapper.deleteByPrimaryKey(id);

        return responseText;
    }

    //统计。。。。目前三个统计，用一个controller方法，未写接口，通过统计类型statisticsType，进行判断区分
    @RequestMapping("statics")
    public @ResponseBody List statics(String yearVal,String monthVal,String statisticsType,String mendian,String account,Model model){

        String h = "-";
        String finishTime = yearVal + h + monthVal;
        Map<String,Object> maps = new HashMap<String, Object>();
        maps.put("finishTime",finishTime);
        List<Map<String,Object>> staticsList = new ArrayList<Map<String, Object>>();
        int businessID = Integer.parseInt(mendian);
        maps.put("businessID",businessID);
        model.addAttribute("businessID", businessID);
        //"1" 为用户充值统计
        if(statisticsType.equals("1")){
            staticsList = vipMemberExtendMapper.userRechargeStatics(maps);
        }
        //"2" 为用户消费统计
        if(statisticsType.equals("2")){
            staticsList = vipMemberExtendMapper.userPayStatics(maps);
        }
        //"3" 为会员数据统计
        if(statisticsType.equals("3")){
            staticsList = vipMemberExtendMapper.vipDataStatics(maps);
        }
        List<VipBusiness> vipBusinessList = this.getBusinessInfo(account);
        model.addAttribute("vipBusinessList",vipBusinessList);
        model.addAttribute("accountID", account);

        return staticsList;
    }


    //注册
    @RequestMapping("register")
    public @ResponseBody int register(HttpServletRequest request,String userName,String password,String phoneNum,String email,Model model){

        MD5 md5 = new MD5();
        String str = "iwpoo";

        String pwd = str + password;
        String targPass = md5.GetMD5Code(pwd);
        log.info("加密后的密码："+targPass);

        Account account = new Account();
        account.setAccountName(userName);
        account.setEmail(email);
        account.setPassword(targPass);
        account.setPhone(phoneNum);

        int i = accountMapper.insert(account);
        int accountId = account.getId();
        request.getSession().setAttribute("userName",userName);
        request.getSession().setAttribute("accountId",accountId);
        //model.addAttribute("userName", userName);

        return i;
    }

    //注册时鼠标离开验证 用户名是否可用
    @RequestMapping("chickRegisterUserName")
    public @ResponseBody List<Account> chickRegisterUserName(String userNameVal){

        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountNameEqualTo(userNameVal);

        List<Account> accountList = accountMapper.selectByExample(accountExample);

        return accountList;
    }

    //登录
    @RequestMapping("login")
    public String login(HttpServletRequest request,String userName,String password,Model model){
        if(userName == null || userName.isEmpty() && password == null || password.isEmpty()){
            String errMsg = "请填写相应信息";
            model.addAttribute("errMsg",errMsg);
            return "vip/admin/login";
        }
        MD5 md5 = new MD5();
        String str = "iwpoo";
        String pwd = str + password;
        String targPass = md5.GetMD5Code(pwd);
        log.info("接收的密码MD5值----拼接后的------"+targPass+"===="+pwd);

        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountNameEqualTo(userName);

        List<Account> accountList =  accountMapper.selectByExample(accountExample);
        if(accountList.size() == 0){
            String errMsg = "请检查账号是否正确";
            model.addAttribute("errMsg",errMsg);
            model.addAttribute("userName",userName);
            model.addAttribute("password",password);
            return "vip/admin/login";
        }

        String md5PassWord = accountList.get(0).getPassword();
        int accountId = accountList.get(0).getId();

        if(targPass.equals(md5PassWord)){
            //model.addAttribute("userName",userName);
            request.getSession().setAttribute("userName",userName);
            request.getSession().setAttribute("accountId",accountId);
            return "redirect:/admin/toIndex.do";
        }else{
            String errMsg = "密码错误";
            model.addAttribute("errMsg",errMsg);
            model.addAttribute("userName",userName);
            model.addAttribute("password",password);
            return "vip/admin/login";
        }


    }

    //商户管理--查询商户信息
    @RequestMapping("gzhManageSerach")
    public String gzhManageSerach(Model model,String account,String choseType,String pageNo) {

        Map<String, Object> searchMap = new HashMap<String, Object>();
        if (pageNo == null || pageNo.equals("")) {
            pageNo = "1";
        }
        Integer pageNos = Integer.parseInt(pageNo);
        searchMap.put("pageNo",pageNos);
        searchMap.put("accountID",account);
        searchMap.put("choseType",choseType);
        Page page = adminService.queryForList(searchMap);
        model.addAttribute("page",page);

        model.addAttribute("accountId",account);
        return "vip/admin/gzh-manage";
    }

    //根据商户ID 查询公众号详情
    @RequestMapping("getGzhInfo")
    public @ResponseBody List<Map<String,Object>> searchGzhIfo(Integer businessID){

        Map<String,Object> map = new HashMap<String, Object>();
        map.put("businessID",businessID);

        List<Map<String,Object>> weixinGzhList = getVipMemberExtendMapper.gzhInfoList(map);

        return weixinGzhList;
    }

    //添加商户
    @RequestMapping("addBusiness")
    public @ResponseBody Integer insertBusiness(Integer accountId,String businessName,
                                               String businessTel,String businessCity,String businessAddr){

        VipBusinessExample vipBusinessExample = new VipBusinessExample();
        vipBusinessExample.createCriteria().andNameEqualTo(businessName);
        List<VipBusiness> vipBusinessList = vipBusinessMapper.selectByExample(vipBusinessExample);

        if(vipBusinessList.size()!=0){
            return 0;
        }else{
            VipBusiness vipBusiness = new VipBusiness();
            vipBusiness.setAccountId(accountId);
            vipBusiness.setName(businessName);
            vipBusiness.setTel(businessTel);
            vipBusiness.setCity(businessCity);
            vipBusiness.setAddr(businessAddr);
            int i = vipBusinessMapper.insertSelective(vipBusiness);
            return i;
        }
    }

    //根据商户ID 和 状态为0（未授权绑定公众号） 删除商户
    @RequestMapping("deleteBusiness")
    public @ResponseBody Integer deleteBusiness(Integer businessID){
        Integer businessType = 0;
        VipBusinessExample vipBusinessExample = new VipBusinessExample();
        vipBusinessExample.createCriteria().andIdEqualTo(businessID).andTypeEqualTo(businessType);

        int i = vipBusinessMapper.deleteByExample(vipBusinessExample);

        return i;
    }
}
