package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsAdMapper;
import cn.cqut.lgqs.db.domain.LgqsAd;
import cn.cqut.lgqs.db.domain.LgqsAdExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsAdService {
    @Resource
    private LgqsAdMapper adMapper;

    public List<LgqsAd> queryIndex() {
        LgqsAdExample example = new LgqsAdExample();
        example.or().andPositionEqualTo((byte) 1).andDeletedEqualTo(false).andEnabledEqualTo(true);
        return adMapper.selectByExample(example);
    }

    public List<LgqsAd> querySelective(String name, String content, Integer page, Integer limit, String sort, String order) {
        LgqsAdExample example = new LgqsAdExample();
        LgqsAdExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(content)) {
            criteria.andContentLike("%" + content + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return adMapper.selectByExample(example);
    }

    public int updateById(LgqsAd ad) {
        ad.setUpdateTime(LocalDateTime.now());
        return adMapper.updateByPrimaryKeySelective(ad);
    }

    public void deleteById(Integer id) {
        adMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsAd ad) {
        ad.setAddTime(LocalDateTime.now());
        ad.setUpdateTime(LocalDateTime.now());
        adMapper.insertSelective(ad);
    }

    public LgqsAd findById(Integer id) {
        return adMapper.selectByPrimaryKey(id);
    }
}
