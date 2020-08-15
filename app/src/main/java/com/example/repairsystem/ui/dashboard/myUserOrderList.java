package com.example.repairsystem.ui.dashboard;

public class myUserOrderList {

    /**
     * id : 0
     * username : 徐磊
     * communityName : 钱湖小区寝室楼
     * buildingNo : 53号楼
     * houseNumber : 525寝室
     * repairType : 电
     * repairItems : 饮水机插座
     * reportDescribes :
     * code : 1
     * orderTime : 2020-06-05 11:01:21
     * orderNumber : 1202006051101215521723
     * jobNumber : 徐磊
     * workerPhone : 156613123
     * receiptTime：接单时间
     * endTime：维修工结束时间
     * userPhone: 用户电话
     * workerPhone: 工人电话
     */

    private int id;
    private String username;
    private String communityName;
    private String buildingNo;
    private String houseNumber;
    private String repairType;
    private String repairItems;
    private String reportDescribes;
    private String code;
    private String orderTime;
    private String orderNumber;
    private String jobNumber;
    private String workerPhone;
    private String receiptTime;
    private String endTime;
    private String url;
    private String userPhone;

    @Override
    public String toString() {
        return "myUserOrderList{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", communityName='" + communityName + '\'' +
                ", buildingNo='" + buildingNo + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", repairType='" + repairType + '\'' +
                ", repairItems='" + repairItems + '\'' +
                ", reportDescribes='" + reportDescribes + '\'' +
                ", code='" + code + '\'' +
                ", orderTime='" + orderTime + '\'' +
                ", orderNumber='" + orderNumber + '\'' +
                ", jobNumber='" + jobNumber + '\'' +
                ", workerPhone='" + workerPhone + '\'' +
                ", receiptTime='" + receiptTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", url='" + url + '\'' +
                ", userPhone='" + userPhone + '\'' +
                '}';
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public String getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(String receiptTime) {
        this.receiptTime = receiptTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getBuildingNo() {
        return buildingNo;
    }

    public void setBuildingNo(String buildingNo) {
        this.buildingNo = buildingNo;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getRepairType() {
        return repairType;
    }

    public void setRepairType(String repairType) {
        this.repairType = repairType;
    }

    public String getRepairItems() {
        return repairItems;
    }

    public void setRepairItems(String repairItems) {
        this.repairItems = repairItems;
    }

    public String getReportDescribes() {
        return reportDescribes;
    }

    public void setReportDescribes(String reportDescribes) {
        this.reportDescribes = reportDescribes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public String getWorkerPhone() {
        return workerPhone;
    }

    public void setWorkerPhone(String workerPhone) {
        this.workerPhone = workerPhone;
    }
}
