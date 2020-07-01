package com.purcotton.pojo;

import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * @Author: rqzhang
 * @Date: 2020/5/7 13:25
 */
public class CombTag implements Writable {
    /*
        [
            {
                "operationType": "and",
                "operationTagId": [
                    "qmsd_1001",
                    "qmsd_1022"
                ]
            },
            {
                "operationType": "or",
                "operationTagId": [
                    "qmsd_1026"
                ]
            }
        ]
         */
    private String operationType;
    private List<String> operationTagId;

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public List<String> getOperationTagId() {
        return operationTagId;
    }

    public void setOperationTagId(List<String> operationTagId) {
        this.operationTagId = operationTagId;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {

    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {

    }
}
