package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsFootprintMapper;
import cn.cqut.lgqs.db.domain.LgqsFootprint;
import cn.cqut.lgqs.db.domain.LgqsFootprintExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsFootprintService {
    @Resource
    private LgqsFootprintMapper footprintMapper;

    public List<LgqsFootprint> queryByAddTime(Integer userId, Integer page, Integer size) {
        LgqsFootprintExample example = new LgqsFootprintExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        example.setOrderByClause(LgqsFootprint.Column.addTime.desc());
        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }

    public LgqsFootprint findById(Integer id) {
        return footprintMapper.selectByPrimaryKey(id);
    }

    public LgqsFootprint findById(Integer userId, Integer id) {
        LgqsFootprintExample example = new LgqsFootprintExample();
        example.or().andIdEqualTo(id).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return footprintMapper.selectOneByExample(example);
    }

    public void deleteById(Integer id) {
        footprintMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsFootprint footprint) {
        footprint.setAddTime(LocalDateTime.now());
        footprint.setUpdateTime(LocalDateTime.now());
        footprintMapper.insertSelective(footprint);
    }

    public List<LgqsFootprint> querySelective(String userId, String goodsId, Integer page, Integer size, String sort, String order) {
        LgqsFootprintExample example = new LgqsFootprintExample();
        LgqsFootprintExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.valueOf(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }
}
