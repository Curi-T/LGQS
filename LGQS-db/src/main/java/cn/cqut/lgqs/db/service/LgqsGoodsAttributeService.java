package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsGoodsAttributeMapper;
import cn.cqut.lgqs.db.domain.LgqsGoodsAttribute;
import cn.cqut.lgqs.db.domain.LgqsGoodsAttributeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsGoodsAttributeService {
    @Resource
    private LgqsGoodsAttributeMapper goodsAttributeMapper;


    public void add(LgqsGoodsAttribute goodsAttribute) {
        goodsAttribute.setAddTime(LocalDateTime.now());
        goodsAttribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.insertSelective(goodsAttribute);
    }

    public LgqsGoodsAttribute findById(Integer id) {
        return goodsAttributeMapper.selectByPrimaryKey(id);
    }



    public void deleteById(Integer id) {
        goodsAttributeMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(LgqsGoodsAttribute attribute) {
        attribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.updateByPrimaryKeySelective(attribute);
    }
}
