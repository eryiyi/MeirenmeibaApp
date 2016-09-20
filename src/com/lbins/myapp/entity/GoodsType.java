package com.lbins.myapp.entity;

/**
 * Created by zhl on 2016/9/12.
 */
public class GoodsType {
    private String typeId;//分类ID
    private String typeName;//分类名称
    private String typeContent;//分类介绍
    private String typeIsUse;//是否使用
    private String typeCover;//分类图标
    private String type_num;//排序

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeContent() {
        return typeContent;
    }

    public void setTypeContent(String typeContent) {
        this.typeContent = typeContent;
    }

    public String getTypeIsUse() {
        return typeIsUse;
    }

    public void setTypeIsUse(String typeIsUse) {
        this.typeIsUse = typeIsUse;
    }

    public String getTypeCover() {
        return typeCover;
    }

    public void setTypeCover(String typeCover) {
        this.typeCover = typeCover;
    }

    public String getType_num() {
        return type_num;
    }

    public void setType_num(String type_num) {
        this.type_num = type_num;
    }
}
