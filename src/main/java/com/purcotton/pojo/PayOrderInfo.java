package com.purcotton.pojo;

import com.aliyun.odps.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class PayOrderInfo implements Writable {
    private double totalPayAmount;
    private long totalPayCount;
    private String firstPayDate;
    private double firstPayAmount;
    private String firstPayOrderNo;
    private String lastPayDate;
    private double lastPayAmount;
    private String lastPayOrderNo;

    public double getTotalPayAmount() {
        return totalPayAmount;
    }

    public void setTotalPayAmount(double totalPayAmount) {
        this.totalPayAmount = totalPayAmount;
    }

    public long getTotalPayCount() {
        return totalPayCount;
    }

    public void setTotalPayCount(long totalPayCount) {
        this.totalPayCount = totalPayCount;
    }

    public String getFirstPayDate() {
        return firstPayDate;
    }

    public void setFirstPayDate(String firstPayDate) {
        this.firstPayDate = firstPayDate;
    }

    public double getFirstPayAmount() {
        return firstPayAmount;
    }

    public void setFirstPayAmount(double firstPayAmount) {
        this.firstPayAmount = firstPayAmount;
    }

    public String getFirstPayOrderNo() {
        return firstPayOrderNo;
    }

    public void setFirstPayOrderNo(String firstPayOrderNo) {
        this.firstPayOrderNo = firstPayOrderNo;
    }

    public String getLastPayDate() {
        return lastPayDate;
    }

    public void setLastPayDate(String lastPayDate) {
        this.lastPayDate = lastPayDate;
    }

    public double getLastPayAmount() {
        return lastPayAmount;
    }

    public void setLastPayAmount(double lastPayAmount) {
        this.lastPayAmount = lastPayAmount;
    }

    public String getLastPayOrderNo() {
        return lastPayOrderNo;
    }

    public void setLastPayOrderNo(String lastPayOrderNo) {
        this.lastPayOrderNo = lastPayOrderNo;
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(totalPayAmount);
        dataOutput.writeLong(totalPayCount);
        dataOutput.writeBytes(firstPayDate);
        dataOutput.writeDouble(firstPayAmount);
        dataOutput.writeBytes(firstPayOrderNo);
        dataOutput.writeBytes(lastPayDate);
        dataOutput.writeDouble(lastPayAmount);
        dataOutput.writeBytes(lastPayOrderNo);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        totalPayAmount = dataInput.readDouble();
        totalPayCount = dataInput.readLong();
        firstPayDate = dataInput.readLine();
        firstPayAmount = dataInput.readDouble();
        firstPayOrderNo = dataInput.readLine();
        lastPayDate = dataInput.readLine();
        lastPayAmount = dataInput.readDouble();
        lastPayOrderNo = dataInput.readLine();
    }
}
