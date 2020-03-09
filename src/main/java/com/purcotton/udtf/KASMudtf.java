package com.purcotton.udtf;

import com.aliyun.odps.udf.ExecutionContext;
import com.aliyun.odps.udf.UDFException;
import com.aliyun.odps.udf.UDTF;
import com.aliyun.odps.udf.annotation.Resolve;

// TODO define input and output types, e.g. "string,string->string,bigint".
@Resolve({"string,string->string,*"})
public class KASMudtf extends UDTF {

    @Override
    public void setup(ExecutionContext ctx) throws UDFException {

    }

    @Override
    public void process(Object[] args) throws UDFException {
        String rowid = (String) args[0];
        String salemans = (String)args[1];
        if(salemans != null){
            String saleid = null;
            String[] split = salemans.split(",");
            if(split.length > 1){
                forward(rowid,split[0],split[1]);
            }else if(split.length == 1){
                saleid = split[0];
                int length = saleid.length();
                if (length > 32){
                    //没有逗号分隔符的id处理，暂时处理两个id
                    forward(rowid,saleid.substring(0,32),saleid.substring(32,64));
                }else{
                    forward(rowid,split[0],null);
                }
            }else{
                forward(rowid,null,null);
            }
        }else{
            forward(rowid,null,null);
        }

    }

    @Override
    public void close() throws UDFException {

    }

}