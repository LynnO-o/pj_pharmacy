package com.example.entity;

import java.math.BigDecimal;

public class Drug {

    /**ID */
    private Integer id;
    /**药品店 */
    private Integer shopId;
    /**药品类型 */
    private Integer typeId;
    /**药品名称 */
    private String name;
    /**药品图片 */
    private String img;
    /**药品价格 */
    private BigDecimal price;
    /**药品规格 */
    private String spec;
//    /**药品有效日期 */
//    private String expiredate;
    /**药品功能主治 */
    private String drugfunction;
    /**药品用法 */
    private String drugusage;
    /**药品禁忌 */
    private String taboo;
    /**药品详情 */
    private String content;
    /**药品状态 */
    private String status;
    /**是否推荐 */
    private String recommend;
    private String shopName;
    private String typeName;
    private String shopAvatar;
    private Integer store;
    private Boolean hasCollect;

    public Boolean getHasCollect() {
        return hasCollect;
    }

    public void setHasCollect(Boolean hasCollect) {
        this.hasCollect = hasCollect;
    }

    public Integer getStore() {
        return store;
    }

    public void setStore(Integer store) {
        this.store = store;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }


    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

//    public String getExpiredate() {
//        return expiredate;
//    }
//
//    public void setExpiredate(String expiredate) {
//        this.expiredate = expiredate;
//    }

    public String getDrugfunction() {
        return drugfunction;
    }

    public void setDrugfunction(String drugfunction) {
        this.drugfunction = drugfunction;
    }

    public String getDrugusage() {
        return drugusage;
    }

    public void setDrugusage(String drugusage) {
        this.drugusage = drugusage;
    }

    public String getTaboo() {
        return taboo;
    }

    public void setTaboo(String taboo) {
        this.taboo = taboo;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }
}