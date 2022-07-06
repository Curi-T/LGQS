package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsOrderGoodsMapper;
import cn.cqut.lgqs.db.domain.LgqsOrderGoods;
import cn.cqut.lgqs.db.domain.LgqsOrderGoodsExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsOrderGoodsService {
    @Resource
    private LgqsOrderGoodsMapper orderGoodsMapper;

    public int add(LgqsOrderGoods orderGoods) {
        orderGoods.setAddTime(LocalDateTime.now());
        orderGoods.setUpdateTime(LocalDateTime.now());
        return orderGoodsMapper.insertSelective(orderGoods);
    }

    public List<LgqsOrderGoods> queryByOid(Integer orderId) {
        LgqsOrderGoodsExample example = new LgqsOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public LgqsOrderGoods findById(Integer id) {
        return orderGoodsMapper.selectByPrimaryKey(id);
    }

    public void updateById(LgqsOrderGoods orderGoods) {
        orderGoods.setUpdateTime(LocalDateTime.now());
        orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
    }

    public Short getComments(Integer orderId) {
        LgqsOrderGoodsExample example = new LgqsOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        long count = orderGoodsMapper.countByExample(example);
        return (short) count;
    }

    public boolean checkExist(Integer goodsId) {
        LgqsOrderGoodsExample example = new LgqsOrderGoodsExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.countByExample(example) != 0;
    }

    public void deleteByOrderId(Integer orderId) {
        LgqsOrderGoodsExample example = new LgqsOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        orderGoodsMapper.logicalDeleteByExample(example);
    }
}
