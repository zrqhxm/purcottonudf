package com.purcotton.udf;

import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.annotation.Resolve;
import com.google.gson.Gson;
import com.purcotton.pojo.PayOrderInfo;

/**
 * 支付信息不为空处理
 */
@Resolve("string -> string")
public class PayInfoNotNullUDF extends UDF {

public String evaluate(String payinfo) {
    if(payinfo == null){
        PayOrderInfo payOrderInfo = new PayOrderInfo();
        payOrderInfo.setTotalPayAmount(0.0);
        payOrderInfo.setTotalPayCount(0);
        payOrderInfo.setFirstPayDate("");
        payOrderInfo.setFirstPayAmount(0.0);
        payOrderInfo.setFirstPayOrderNo("");
        payOrderInfo.setLastPayDate("");
        payOrderInfo.setLastPayAmount(0.0);
        payOrderInfo.setLastPayOrderNo("");
        Gson gson = new Gson();
        String s = gson.toJson(payOrderInfo);
        return s;
    }else{
       return payinfo;
    }
}
}