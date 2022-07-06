package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.GoodsProductMapper;
import cn.cqut.lgqs.db.dao.LgqsGoodsProductMapper;
import cn.cqut.lgqs.db.domain.LgqsGoodsProduct;
import cn.cqut.lgqs.db.domain.LgqsGoodsProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsGoodsProductService {
    @Resource
    private LgqsGoodsProductMapper lgqsGoodsProductMapper;
    @Resource
    private GoodsProductMapper goodsProductMapper;

    public List<LgqsGoodsProduct> queryByGid(Integer gid) {
        LgqsGoodsProductExample example = new LgqsGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid).andDeletedEqualTo(false);
        return lgqsGoodsProductMapper.selectByExample(example);
    }

    public LgqsGoodsProduct findById(Integer id) {
        return lgqsGoodsProductMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        lgqsGoodsProductMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsGoodsProduct goodsProduct) {
        goodsProduct.setAddTime(LocalDateTime.now());
        goodsProduct.setUpdateTime(LocalDateTime.now());
        lgqsGoodsProductMapper.insertSelective(goodsProduct);
    }

    public int count() {
        LgqsGoodsProductExample example = new LgqsGoodsProductExample();
        example.or().andDeletedEqualTo(false);
        return (int) lgqsGoodsProductMapper.countByExample(example);
    }

    public void deleteByGid(Integer gid) {
        LgqsGoodsProductExample example = new LgqsGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid);
        lgqsGoodsProductMapper.logicalDeleteByExample(example);
    }

    public int addStock(Integer id, Short num){
        return goodsProductMapper.addStock(id, num);
    }

    public int reduceStock(Integer id, Short num){
        return goodsProductMapper.reduceStock(id, num);
    }

    public void updateById(LgqsGoodsProduct product) {
        product.setUpdateTime(LocalDateTime.now());
        lgqsGoodsProductMapper.updateByPrimaryKeySelective(product);
    }
}