package weixin.assistant.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weixin.assistant.dao.WeixinGzhPayMapper;
import weixin.assistant.model.WeixinGzhPay;
import weixin.assistant.model.WeixinGzhPayExample;
import weixin.assistant.service.WeixinPayService;

import java.util.List;

/**
 * Created by worgen on 2015/9/10.
 */
@Service
public class WeixinPayServiceImpl implements WeixinPayService{
    @Autowired
    private WeixinGzhPayMapper weixinGzhPayMapper;

    @Override
    public WeixinGzhPay getWeixinGzhPay(int weixinGzhID) {
        WeixinGzhPayExample weixinGzhPayExample = new WeixinGzhPayExample();
        weixinGzhPayExample.createCriteria().andWeixinGzhIdEqualTo(weixinGzhID);
        List<WeixinGzhPay> weixinGzhPays = weixinGzhPayMapper.selectByExample(weixinGzhPayExample);
        if( weixinGzhPays.size() == 1 ){
            return weixinGzhPays.get(0);
        }
        //默认
        //WeixinGzhPay weixinGzhPay =
        return null;
    }
}
