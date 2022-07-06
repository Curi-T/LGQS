package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsAddressMapper;
import cn.cqut.lgqs.db.domain.LgqsAddress;
import cn.cqut.lgqs.db.domain.LgqsAddressExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsAddressService {
    @Resource
    private LgqsAddressMapper addressMapper;

    public List<LgqsAddress> queryByUid(Integer uid) {
        LgqsAddressExample example = new LgqsAddressExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        return addressMapper.selectByExample(example);
    }

    public LgqsAddress query(Integer userId, Integer id) {
        LgqsAddressExample example = new LgqsAddressExample();
        example.or().andIdEqualTo(id).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public int add(LgqsAddress address) {
        address.setAddTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.insertSelective(address);
    }

    public int update(LgqsAddress address) {
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.updateByPrimaryKeySelective(address);
    }

    public void delete(Integer id) {
        addressMapper.logicalDeleteByPrimaryKey(id);
    }

    public LgqsAddress findDefault(Integer userId) {
        LgqsAddressExample example = new LgqsAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public void resetDefault(Integer userId) {
        LgqsAddress address = new LgqsAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        LgqsAddressExample example = new LgqsAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    public List<LgqsAddress> querySelective(Integer userId, String name, Integer page, Integer limit, String sort, String order) {
        LgqsAddressExample example = new LgqsAddressExample();
        LgqsAddressExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return addressMapper.selectByExample(example);
    }
}
