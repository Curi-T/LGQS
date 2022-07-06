package cn.cqut.lgqs.admin.service;

import cn.cqut.lgqs.admin.dto.GoodsAllinone;
import cn.cqut.lgqs.admin.util.AdminResponseCode;
import cn.cqut.lgqs.admin.vo.CatVo;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.db.domain.*;
import cn.cqut.lgqs.db.service.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminGoodsService {
    private final Log logger = LogFactory.getLog(AdminGoodsService.class);

    @Autowired
    private LgqsGoodsService goodsService;
    @Autowired
    private LgqsGoodsSpecificationService specificationService;
    @Autowired
    private LgqsGoodsAttributeService attributeService;
    @Autowired
    private LgqsGoodsProductService productService;
    @Autowired
    private LgqsCategoryService categoryService;
    @Autowired
    private LgqsBrandService brandService;
    @Autowired
    private LgqsCartService cartService;


    public Object list(Integer goodsId, String goodsSn, String name,
                       Integer page, Integer limit, String sort, String order) {
        List<LgqsGoods> goodsList = goodsService.querySelective(goodsId, goodsSn, name, page, limit, sort, order);
        return ResponseUtil.okList(goodsList);
    }

    private Object validate(GoodsAllinone goodsAllinone) {
        LgqsGoods goods = goodsAllinone.getGoods();
        String name = goods.getName();
        if (StringUtils.isEmpty(name)) {
            return ResponseUtil.badArgument();
        }
        String goodsSn = goods.getGoodsSn();
        if (StringUtils.isEmpty(goodsSn)) {
            return ResponseUtil.badArgument();
        }
        // 品牌商可以不设置，如果设置则需要验证品牌商存在
        Integer brandId = goods.getBrandId();
        if (brandId != null && brandId != 0) {
                return ResponseUtil.badArgumentValue();
        }
        // 分类可以不设置，如果设置则需要验证分类存在
        Integer categoryId = goods.getCategoryId();
        if (categoryId != null && categoryId != 0) {
            if (categoryService.findById(categoryId) == null) {
                return ResponseUtil.badArgumentValue();
            }
        }
        System.out.println(goodsAllinone);
        LgqsGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        System.out.println("```"+attributes);
        for (LgqsGoodsAttribute attribute : attributes) {
            String attr = attribute.getAttribute();
            if (StringUtils.isEmpty(attr)) {
                return ResponseUtil.badArgument();
            }
            String value = attribute.getValue();
            if (StringUtils.isEmpty(value)) {
                return ResponseUtil.badArgument();
            }
        }

        LgqsGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        for (LgqsGoodsSpecification specification : specifications) {
            String spec = specification.getSpecification();
            if (StringUtils.isEmpty(spec)) {
                return ResponseUtil.badArgument();
            }
            String value = specification.getValue();
            if (StringUtils.isEmpty(value)) {
                return ResponseUtil.badArgument();
            }
        }

        LgqsGoodsProduct[] products = goodsAllinone.getProducts();
        for (LgqsGoodsProduct product : products) {
            Integer number = product.getNumber();
            if (number == null || number < 0) {
                return ResponseUtil.badArgument();
            }

            BigDecimal price = product.getPrice();
            if (price == null) {
                return ResponseUtil.badArgument();
            }

            String[] productSpecifications = product.getSpecifications();
            if (productSpecifications.length == 0) {
                return ResponseUtil.badArgument();
            }
        }

        return null;
    }

    /**
     * 编辑餐品
     *
     * NOTE：
     * 由于餐品涉及到四个表，特别是lgqs_goods_product表依赖lgqs_goods_specification表，
     * 这导致允许所有字段都是可编辑会带来一些问题，因此这里餐品编辑功能是受限制：
     * （1）lgqs_goods表可以编辑字段；
     * （2）lgqs_goods_specification表只能编辑pic_url字段，其他操作不支持；
     * （3）lgqs_goods_product表只能编辑price, number和url字段，其他操作不支持；
     * （4）lgqs_goods_attribute表支持编辑、添加和删除操作。
     *
     * NOTE2:
     * 前后端这里使用了一个小技巧：
     * 如果前端传来的update_time字段是空，则说明前端已经更新了某个记录，则这个记录会更新；
     * 否则说明这个记录没有编辑过，无需更新该记录。
     *
     * NOTE3:
     * （1）点餐车缓存了一些餐品信息，因此需要及时更新。
     * 目前这些字段是goods_sn, goods_name, price, pic_url。
     * （2）但是订单里面的餐品信息则是不会更新。
     * 如果订单是未支付订单，此时仍然以旧的价格支付。
     */
    @Transactional
    public Object update(GoodsAllinone goodsAllinone) {
        Object error = validate(goodsAllinone);
        if (error != null) {
            return error;
        }

        LgqsGoods goods = goodsAllinone.getGoods();
        LgqsGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        LgqsGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        LgqsGoodsProduct[] products = goodsAllinone.getProducts();


        // 餐品表里面有一个字段retailPrice记录当前餐品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (LgqsGoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if(retailPrice.compareTo(productPrice) == 1){
                retailPrice = productPrice;
            }
        }
        goods.setRetailPrice(retailPrice);
        
        // 餐品基本信息表lgqs_goods
        if (goodsService.updateById(goods) == 0) {
            throw new RuntimeException("更新数据失败");
        }

        Integer gid = goods.getId();

        // 餐品规格表lgqs_goods_specification
        for (LgqsGoodsSpecification specification : specifications) {
            // 目前只支持更新规格表的图片字段
            if(specification.getUpdateTime() == null){
                specification.setSpecification(null);
                specification.setValue(null);
                specificationService.updateById(specification);
            }
        }

        // 餐品餐品表lgqs_product
        for (LgqsGoodsProduct product : products) {
            if(product.getUpdateTime() == null) {
                productService.updateById(product);
            }
        }

        // 餐品参数表lgqs_goods_attribute
        for (LgqsGoodsAttribute attribute : attributes) {
            if (attribute.getId() == null || attribute.getId().equals(0)){
                attribute.setGoodsId(goods.getId());
                attributeService.add(attribute);
            }
            else if(attribute.getDeleted()){
                attributeService.deleteById(attribute.getId());
            }
            else if(attribute.getUpdateTime() == null){
                attributeService.updateById(attribute);
            }
        }

        // 这里需要注意的是点餐车lgqs_cart有些字段是拷贝餐品的一些字段，因此需要及时更新
        // 目前这些字段是goods_sn, goods_name, price, pic_url
        for (LgqsGoodsProduct product : products) {
            cartService.updateProduct(product.getId(), goods.getGoodsSn(), goods.getName(), product.getPrice(), product.getUrl());
        }

        return ResponseUtil.ok();
    }

    @Transactional
    public Object delete(LgqsGoods goods) {
        Integer id = goods.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }

        Integer gid = goods.getId();
        goodsService.deleteById(gid);
        specificationService.deleteByGid(gid);
        productService.deleteByGid(gid);
        return ResponseUtil.ok();
    }

    @Transactional
    public Object create(GoodsAllinone goodsAllinone) {
        Object error = validate(goodsAllinone);
        if (error != null) {
            return error;
        }

        LgqsGoods goods = goodsAllinone.getGoods();
        LgqsGoodsAttribute[] attributes = goodsAllinone.getAttributes();
        LgqsGoodsSpecification[] specifications = goodsAllinone.getSpecifications();
        LgqsGoodsProduct[] products = goodsAllinone.getProducts();

        String name = goods.getName();
        if (goodsService.checkExistByName(name)) {
            return ResponseUtil.fail(AdminResponseCode.GOODS_NAME_EXIST, "餐品名已经存在");
        }

        // 餐品表里面有一个字段retailPrice记录当前餐品的最低价
        BigDecimal retailPrice = new BigDecimal(Integer.MAX_VALUE);
        for (LgqsGoodsProduct product : products) {
            BigDecimal productPrice = product.getPrice();
            if(retailPrice.compareTo(productPrice) == 1){
                retailPrice = productPrice;
            }
        }
        goods.setRetailPrice(retailPrice);

        // 餐品基本信息表lgqs_goods
        goodsService.add(goods);


        // 餐品规格表lgqs_goods_specification
        for (LgqsGoodsSpecification specification : specifications) {
            specification.setGoodsId(goods.getId());
            specificationService.add(specification);
        }

        // 餐品参数表lgqs_goods_attribute
        for (LgqsGoodsAttribute attribute : attributes) {
            attribute.setGoodsId(goods.getId());
            attributeService.add(attribute);
        }

        // 餐品餐品表lgqs_product
        for (LgqsGoodsProduct product : products) {
            product.setGoodsId(goods.getId());
            productService.add(product);
        }
        return ResponseUtil.ok();
    }

    public Object list2() {
        // http://element-cn.eleme.io/#/zh-CN/component/cascader
        // 管理员设置“所属分类”
        List<LgqsCategory> l1CatList = categoryService.queryL1();
        List<CatVo> categoryList = new ArrayList<>(l1CatList.size());

        for (LgqsCategory l1 : l1CatList) {
            CatVo l1CatVo = new CatVo();
            l1CatVo.setValue(l1.getId());
            l1CatVo.setLabel(l1.getName());

            List<LgqsCategory> l2CatList = categoryService.queryByPid(l1.getId());
            List<CatVo> children = new ArrayList<>(l2CatList.size());
            for (LgqsCategory l2 : l2CatList) {
                CatVo l2CatVo = new CatVo();
                l2CatVo.setValue(l2.getId());
                l2CatVo.setLabel(l2.getName());
                children.add(l2CatVo);
            }
            l1CatVo.setChildren(children);

            categoryList.add(l1CatVo);
        }


        Map<String, Object> data = new HashMap<>();
        data.put("categoryList", categoryList);
        return ResponseUtil.ok(data);
    }

    public Object detail(Integer id) {
        LgqsGoods goods = goodsService.findById(id);
        List<LgqsGoodsProduct> products = productService.queryByGid(id);
        List<LgqsGoodsSpecification> specifications = specificationService.queryByGid(id);

        Integer categoryId = goods.getCategoryId();
        LgqsCategory category = categoryService.findById(categoryId);
        Integer[] categoryIds = new Integer[]{};
        if (category != null) {
            Integer parentCategoryId = category.getPid();
            categoryIds = new Integer[]{parentCategoryId, categoryId};
        }

        Map<String, Object> data = new HashMap<>();
        data.put("goods", goods);
        data.put("specifications", specifications);
        data.put("products", products);
        data.put("categoryIds", categoryIds);

        return ResponseUtil.ok(data);
    }

}
