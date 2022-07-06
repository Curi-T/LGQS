package cn.cqut.lgqs.admin.dto;

import cn.cqut.lgqs.db.domain.LgqsGoods;
import cn.cqut.lgqs.db.domain.LgqsGoodsAttribute;
import cn.cqut.lgqs.db.domain.LgqsGoodsProduct;
import cn.cqut.lgqs.db.domain.LgqsGoodsSpecification;

public class GoodsAllinone {
    LgqsGoods goods;
    LgqsGoodsSpecification[] specifications;
    LgqsGoodsAttribute[] attributes;
    LgqsGoodsProduct[] products;

    public LgqsGoods getGoods() {
        return goods;
    }

    public void setGoods(LgqsGoods goods) {
        this.goods = goods;
    }

    public LgqsGoodsProduct[] getProducts() {
        return products;
    }

    public void setProducts(LgqsGoodsProduct[] products) {
        this.products = products;
    }

    public LgqsGoodsSpecification[] getSpecifications() {
        return specifications;
    }

    public void setSpecifications(LgqsGoodsSpecification[] specifications) {
        this.specifications = specifications;
    }

    public LgqsGoodsAttribute[] getAttributes() {
        return attributes;
    }

    public void setAttributes(LgqsGoodsAttribute[] attributes) {
        this.attributes = attributes;
    }

}
