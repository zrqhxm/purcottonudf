package com.purcotton.udf;

import com.aliyun.odps.udf.UDF;
import com.aliyun.odps.udf.annotation.Resolve;

@Resolve("string,string,string,string,string->string")
public class CouponCodeUDF extends UDF {
    public String evaluate(String memberCouponCodes,String couponType,String receiveCouponType,String couponCodes,String tagId) {
        String[] codes1 = memberCouponCodes.split(",");
        String[] codes2 = couponCodes.split(",");
        int count = 0;
        if("1".equals(couponType)){
            for (String s1:codes1){
                if(codes2[0].equals(s1)){
                    return tagId;
                }
            }
        }
        if("2".equals(couponType)){
            if("1".equals(receiveCouponType)){
                for (String s2:codes2){
                    for (String s3:codes1){
                        if(s2.equals(s3)){
                            return tagId;
                        }
                    }
                }
            }else {
                for (String s2:codes2){
                    for (String s3:codes1){
                        if(s2.equals(s3)){
                            count++;
                            break;
                        }
                    }
                }
                if(count == codes2.length){
                    return tagId;
                }
            }
        }
        return null;
    }
}