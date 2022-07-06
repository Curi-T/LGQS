package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsCategoryMapper;
import cn.cqut.lgqs.db.domain.LgqsCategory;
import cn.cqut.lgqs.db.domain.LgqsCategoryExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsCategoryService {
    @Resource
    private LgqsCategoryMapper categoryMapper;
    private LgqsCategory.Column[] CHANNEL = {LgqsCategory.Column.id, LgqsCategory.Column.name, LgqsCategory.Column.iconUrl};

    public List<LgqsCategory> queryL1WithoutRecommend(int offset, int limit) {
        LgqsCategoryExample example = new LgqsCategoryExample();
        example.or().andLevelEqualTo("L1").andNameNotEqualTo("推荐").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<LgqsCategory> queryL1(int offset, int limit) {
        LgqsCategoryExample example = new LgqsCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<LgqsCategory> queryL1() {
        LgqsCategoryExample example = new LgqsCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<LgqsCategory> queryByPid(Integer pid) {
        LgqsCategoryExample example = new LgqsCategoryExample();
        example.or().andPidEqualTo(pid).andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<LgqsCategory> queryL2ByIds(List<Integer> ids) {
        LgqsCategoryExample example = new LgqsCategoryExample();
        example.or().andIdIn(ids).andLevelEqualTo("L2").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public LgqsCategory findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<LgqsCategory> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        LgqsCategoryExample example = new LgqsCategoryExample();
        LgqsCategoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return categoryMapper.selectByExample(example);
    }

    public int updateById(LgqsCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void deleteById(Integer id) {
        categoryMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsCategory category) {
        category.setAddTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertSelective(category);
    }

    public List<LgqsCategory> queryChannel() {
        LgqsCategoryExample example = new LgqsCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example, CHANNEL);
    }
}
