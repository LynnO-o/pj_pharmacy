package com.example.entity;

import java.util.List;

/**
 * 药店店主
 */
public class DrugShop extends Account{
    /**ID */
    private Integer id;
    /**账号 */
    private String username;
    /**名称 */
    private String name;
    /**密码 */
    private String password;
    /**头像 */
    private String avatar;
    /**手机号 */
    private String phone;
    /**销售药品 */
    private String sellDrug;
    /**地址 */
    private String address;
    /**介绍 */
    private String introduce;
    /**邮件 */
    private String email;
    /**角色 */
    private String role;
    /**认证图片 */
    private String practiceLicense;
    /**负责人名称 */
    private String principalName;
    /**负责人身份证号 */
    private String principalNo;
    /**认证状态 */
    private String status;
    private Double sum;

    public Double getSum() {
        return sum;
    }

    public void setSum(Double sum) {
        this.sum = sum;
    }

    private List<Drug> drugList;

    public List<Drug> getDrugList() {
        return drugList;
    }

    public void setDrugList(List<Drug> drugList) {
        this.drugList = drugList;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSellDrug() {
        return sellDrug;
    }

    public void setSellDrug(String sellDrug) {
        this.sellDrug = sellDrug;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public void setRole(String role) {
        this.role = role;
    }

    public String getPracticeLicense() {
        return practiceLicense;
    }

    public void setPracticeLicense(String practiceLicense) {
        this.practiceLicense = practiceLicense;
    }

    public String getPrincipalName() {
        return principalName;
    }

    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }

    public String getPrincipalNo() {
        return principalNo;
    }

    public void setPrincipalNo(String principalNo) {
        this.principalNo = principalNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
