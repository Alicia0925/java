package cn.auth.entity;

import java.util.Date;

//酒店表
public class Hotel {
    private Long id;//主键

    private String hotelName;//酒店名称

    private Long countryId;//所在国家id

    private Long provinceId;//所在省份id

    private Long cityId;//所在城市id

    private String address;//酒店所在地址

    private Integer hotelType;//酒店类型(1:国内酒店 2:国际酒店)

    private Integer hotelLevel;//(1:经济酒店  2:二星级酒店  3:三星级 4:四星酒店 5星酒店

    private Integer isGroupPurchase;//是否是团购酒店

    private String redundantCityName;//城市名称 冗余字段

    private String redundantProvinceName;//省名称 冗余字段

    private String redundantCountryName;//国家名称 冗余字段

    private Integer redundantHotelStore;//酒店库存（冗余，每天开定时任务的时候更新）

    private Date creationDate;

    private Long createdBy;

    private Date modifyDate;

    private Long modifiedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public Long getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(Long provinceId) {
        this.provinceId = provinceId;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getHotelType() {
        return hotelType;
    }

    public void setHotelType(Integer hotelType) {
        this.hotelType = hotelType;
    }

    public Integer getHotelLevel() {
        return hotelLevel;
    }

    public void setHotelLevel(Integer hotelLevel) {
        this.hotelLevel = hotelLevel;
    }

    public Integer getIsGroupPurchase() {
        return isGroupPurchase;
    }

    public void setIsGroupPurchase(Integer isGroupPurchase) {
        this.isGroupPurchase = isGroupPurchase;
    }

    public String getRedundantCityName() {
        return redundantCityName;
    }

    public void setRedundantCityName(String redundantCityName) {
        this.redundantCityName = redundantCityName;
    }

    public String getRedundantProvinceName() {
        return redundantProvinceName;
    }

    public void setRedundantProvinceName(String redundantProvinceName) {
        this.redundantProvinceName = redundantProvinceName;
    }

    public String getRedundantCountryName() {
        return redundantCountryName;
    }

    public void setRedundantCountryName(String redundantCountryName) {
        this.redundantCountryName = redundantCountryName;
    }

    public Integer getRedundantHotelStore() {
        return redundantHotelStore;
    }

    public void setRedundantHotelStore(Integer redundantHotelStore) {
        this.redundantHotelStore = redundantHotelStore;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }

    public Long getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(Long modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}