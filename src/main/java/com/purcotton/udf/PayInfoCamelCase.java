package com.purcotton.udf;

import com.aliyun.odps.data.Struct;
import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.Gson;
import com.purcotton.pojo.PayOrderInfo;

import java.util.List;

/**
 * 订单集合信息驼峰命名
 * com.purcotton.udf.PayInfoCamelCase
 */
@Resolve("array<struct<totalPayAmount:double,totalPayCount:bigint,firstPayDate:string,firstPayAmount:double,firstPayOrderNo:string,lastPayDate:string,lastPayAmount:double,lastPayOrderNo:string>> -> string")
public class PayInfoCamelCase extends UDF {

    public String evaluate(List<Struct> infoList) {
        if (infoList.size() > 0){
            PayOrderInfo payOrderInfo = new PayOrderInfo();
            Struct info = infoList.get(0);
            Double totalPayAmount = (Double) info.getFieldValue("totalPayAmount");
            long totalPayCount = (long) info.getFieldValue("totalPayCount");
            String firstPayDate = (String) info.getFieldValue("firstPayDate");
            Double firstPayAmount = (Double) info.getFieldValue("firstPayAmount");
            String firstPayOrderNo = (String) info.getFieldValue("firstPayOrderNo");
            String lastPayDate = (String) info.getFieldValue("lastPayDate");
            Double lastPayAmount = (Double) info.getFieldValue("lastPayAmount");
            String lastPayOrderNo = (String) info.getFieldValue("lastPayOrderNo");
            payOrderInfo.setTotalPayAmount(totalPayAmount);
            payOrderInfo.setTotalPayCount(totalPayCount);
            payOrderInfo.setFirstPayDate(firstPayDate);
            payOrderInfo.setFirstPayAmount(firstPayAmount);
            payOrderInfo.setFirstPayOrderNo(firstPayOrderNo);
            payOrderInfo.setLastPayDate(lastPayDate);
            payOrderInfo.setLastPayAmount(lastPayAmount);
            payOrderInfo.setLastPayOrderNo(lastPayOrderNo);
            Gson gson = new Gson();
            String s = gson.toJson(payOrderInfo);
            return s;
        }else {
            PayOrderInfo payOrderInfo = new PayOrderInfo();
            payOrderInfo.setTotalPayAmount(0.0);
            payOrderInfo.setTotalPayCount(0);
            payOrderInfo.setFirstPayDate(null);
            payOrderInfo.setFirstPayAmount(0.0);
            payOrderInfo.setFirstPayOrderNo("");
            payOrderInfo.setLastPayDate(null);
            payOrderInfo.setLastPayAmount(0.0);
            payOrderInfo.setLastPayOrderNo("");
            Gson gson = new Gson();
            String s = gson.toJson(payOrderInfo);
            return s;
        }
    }
}