package weixin.vip.service.Impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.assistant.dao.VipBusinessMapper;
import weixin.assistant.dao.VipMemberExtendMapper;
import weixin.assistant.dao.VipMemberMapper;
import weixin.assistant.dao.VipRechargeOrderMapper;
import weixin.vip.model.Page;
import weixin.vip.service.AdminService;

import java.util.*;

/**
 * Created by worgen on 2015/9/10.
 */
@Service
public class AdminServiceImpl implements AdminService{
    private static final Logger log = Logger.getLogger("BussinessLog");


    @Autowired
    private VipMemberMapper vipMemberMapper;
    @Autowired
    private VipBusinessMapper vipBusinessMapper;
    @Autowired
    private VipRechargeOrderMapper vipRechargeOrderMapper;
    @Autowired
    private VipMemberExtendMapper vipMemberExtendMapper;




    @Override
    public Page queryForList(Map<String, Object> map) {
        Integer pageNo = (Integer) map.get("pageNo");
        String choseType = (String) map.get("choseType");
        Integer totalCount = 0;
        List<Map<String,Object>> mapList = new ArrayList<Map<String, Object>>();
        if(choseType.equals("1")){
            totalCount = vipMemberExtendMapper.businessTotalCount(map);
        }
        if (choseType.equals("2")) {
            totalCount = vipMemberExtendMapper.czSearchTotalCount(map);
        }
        if (choseType.equals("3")) {
            totalCount = vipMemberExtendMapper.xfSearchTotalCount(map);
        }
        /*int totalCount = totalCountList.size();*/
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setTotalCount(totalCount);
        Integer pageSize = page.getPageSize();
        Integer start = page.getStartNum();
        map.put("pageSize",pageSize);
        map.put("start",start);
        if(choseType.equals("1")){
            mapList = vipMemberExtendMapper.businessList(map);
        }
        if(choseType.equals("2")){
            mapList = vipMemberExtendMapper.czSearchForList(map);
            for (int i=0;i<mapList.size();i++){
                //System.out.println("~~~~~~~~~~~~~~~~~~"+mapList.get(i));
                Map listMap=(Map)mapList.get(i);
                Iterator iterator = listMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    if(key.equals("czMoneyMapKey")){
                        String xfMoney = listMap.get(key).toString();
                        float fFee = Float.parseFloat(xfMoney)/100;
                        // xfMoneyFloat = fFee;
                        listMap.put("czMoneyMapKey",fFee);
                        // System.out.println("object mapKey:"+fFee+"~~"+key);
                        log.debug("czMoney mapKey:"+fFee+"~~"+key);
                    }
                }
            }
        }
        if(choseType.equals("3")){
            mapList = vipMemberExtendMapper.xfSearchForList(map);
            for (int i=0;i<mapList.size();i++){
                //System.out.println("~~~~~~~~~~~~~~~~~~"+mapList.get(i));
                Map listMap=(Map)mapList.get(i);
                Iterator iterator = listMap.keySet().iterator();
                while (iterator.hasNext()){
                    String key = (String) iterator.next();
                    if(key.equals("xfMoneyMapKey")){
                        String xfMoney = listMap.get(key).toString();
                        float fFee = Float.parseFloat(xfMoney)/100;
                       // xfMoneyFloat = fFee;
                        listMap.put("xfMoneyMapKey",fFee);
                       // System.out.println("object mapKey:"+fFee+"~~"+key);
                        log.debug("xfMoney mapKey:"+fFee+"~~"+key);
                    }
                }
            }
        }
        page.setList(mapList);
        return page;
    }

    @Override
    public Page vipQueryForList(Map<String, Object> map) {
        Integer pageNo = (Integer) map.get("pageNo");
        Integer totalCount = vipMemberExtendMapper.vipTotalCount(map);
        /*int totalCount = totalCountList.size();*/
        Page page = new Page();
        page.setPageNo(pageNo);
        page.setTotalCount(totalCount);
        Integer pageSize = page.getPageSize();
        Integer start = page.getStartNum();
        map.put("pageSize",pageSize);
        map.put("start",start);
        List<Map<String,Object>> mapList = vipMemberExtendMapper.vipQueryForList(map);
        for (int i=0;i<mapList.size();i++){
            //System.out.println("~~~~~~~~~~~~~~~~~~"+mapList.get(i));
            Map listMap=(Map)mapList.get(i);
            Iterator iterator = listMap.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                if(key.equals("feeMapKey")){
                    String vipMoney = listMap.get(key).toString();
                    float fFee = Float.parseFloat(vipMoney)/100;
                    listMap.put("feeMapKey",fFee);
                    log.debug("vipMoney---yu e"+fFee);
                }
            }
        }
        page.setList(mapList);
        return page;
    }


}
