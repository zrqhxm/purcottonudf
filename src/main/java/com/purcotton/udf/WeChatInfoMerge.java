package com.purcotton.udf;

import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.annotation.Resolve;

@Resolve("string,string,string,string->string")
public class WeChatInfoMerge extends UDF {
    /**
     *
     [{"subjectCompany":"purcotton_subject","mpOpenIdList":[{subscription:"purcotton_subscription",mpOpenId:"XXXXXXXXXXXXXXXXXX"}],"miniOpenIdList":[{"program":"purcotton_program","miniOpenId":"XXXXXXXXXXXXXXXXX"}],"unionId":""}]
     M+年+月+累加数    12位
     */
//    private String mp = "";
//    private String wechat = "";
//    private String app = "";
//    private String pc = "";
    /*
    * mpOpenId 小程序
    * wechatOpenId 公众号
    * appOpenId 微信APP
    * pcOpenId 微信PC
    * */
    public String evaluate(String mpOpenId,String wechatOpenId,String appOpenId,String pcOpenId) {
        String mp = "";
        String wechat = "";
        String app = "";
        String pc = "";
        if(null != mpOpenId && !"".equals(mpOpenId)){
            mp = mpOpenId;
        }
        if(null != wechatOpenId && !"".equals(wechatOpenId)){
            wechat = wechatOpenId;
        }
        if(null != appOpenId && !"".equals(appOpenId)){
            app = appOpenId;
        }
        if(null != pcOpenId && !"".equals(pcOpenId)){
            pc = pcOpenId;
        }
        //String json = "[{\"subjectCompany\":\"purcotton_subject\",\"mpOpenIdList\":[{\"subscription\":\"purcotton_subscription\",\"mpOpenId\":\""+mp+"\"}],\"miniOpenIdList\":[{\"program\":\"purcotton_program\",\"miniOpenId\":\""+mini+"\"}],\"unionId\":\"\"}]";
        String json = "[{\"subjectCompany\" : \"purcotton_subject\",\"mpOpenIdList\" : " +
                "[{\"subject\" : \"purcotton1_mp\",\"openId\" : \""+mp+"\"}]," +
                "\"wechatOpenIdList\" : [{\"subject\" : \"purcotton1_wechat\", \"openId\" : \""+wechat+"\"}]," +
                "\"appOpenIdList\":[{\"subject\" : \"purcotton1_app\",\"openId\" : \""+app+"\"}]," +
                "\"pcOpenIdList\":[{\"subject\" : \"purcotton1_pc\",\"openId\" : \""+pc+"\"}" +
                "],\"unionId\" : \"\"}]";

        return json;
    }
}