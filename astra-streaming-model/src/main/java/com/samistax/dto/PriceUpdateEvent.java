package com.samistax.dto;

public class PriceUpdateEvent extends BaseEvent{

    private String itemName; // The name of the item (One of itemId or itemName is required).
    private String affiliation; // A product affiliation to designate a supplier/location.
    private String coupon; // The coupon name/code associated with the item.
    private Double discount; // The unit monetary discount value associated with the item.
    private Integer idx; // The index/position of the item in a list.
    private String itemBrand; // The brand of the item.
    private String itemCategory; // First category of the item.
    private String itemCategory2; // Second category/ hierarchy of the item.
    private String itemCategory3; // Third category/ hierarchy of the item.
    private String itemCategory4; // Fourth category/ hierarchy of the item.
    private String itemCategory5; // Fifth category/ hierarchy of the item.
    private String itemListId; // The ID of the list in which the item was presented.
    private String itemListName; // The name of the list in which the item was presented.
    private String itemVariant; // The item variant.
    private String locationId; // The physical location associated with the item.
    private Double price; // The monetary unit price of the item.
    private Integer quantity; // Item quantity (defaults to 1 if not set).

    public PriceUpdateEvent() {
        super();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public String getItemCategory2() {
        return itemCategory2;
    }

    public void setItemCategory2(String itemCategory2) {
        this.itemCategory2 = itemCategory2;
    }

    public String getItemCategory3() {
        return itemCategory3;
    }

    public void setItemCategory3(String itemCategory3) {
        this.itemCategory3 = itemCategory3;
    }

    public String getItemCategory4() {
        return itemCategory4;
    }

    public void setItemCategory4(String itemCategory4) {
        this.itemCategory4 = itemCategory4;
    }

    public String getItemCategory5() {
        return itemCategory5;
    }

    public void setItemCategory5(String itemCategory5) {
        this.itemCategory5 = itemCategory5;
    }

    public String getItemListId() {
        return itemListId;
    }

    public void setItemListId(String itemListId) {
        this.itemListId = itemListId;
    }

    public String getItemListName() {
        return itemListName;
    }

    public void setItemListName(String itemListName) {
        this.itemListName = itemListName;
    }

    public String getItemVariant() {
        return itemVariant;
    }

    public void setItemVariant(String itemVariant) {
        this.itemVariant = itemVariant;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}