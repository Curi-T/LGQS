package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsRoleMapper;
import cn.cqut.lgqs.db.domain.LgqsRole;
import cn.cqut.lgqs.db.domain.LgqsRoleExample;
import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LgqsRoleService {
    @Resource
    private LgqsRoleMapper roleMapper;


    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        LgqsRoleExample example = new LgqsRoleExample();
        example.or().andIdIn(Arrays.asList(roleIds)).andEnabledEqualTo(true).andDeletedEqualTo(false);
        List<LgqsRole> roleList = roleMapper.selectByExample(example);

        for(LgqsRole role : roleList){
            roles.add(role.getName());
        }

        return roles;

    }

    public List<LgqsRole> querySelective(String name, Integer page, Integer limit, String sort, String order) {
        LgqsRoleExample example = new LgqsRoleExample();
        LgqsRoleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return roleMapper.selectByExample(example);
    }

    public LgqsRole findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public void add(LgqsRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insertSelective(role);
    }

    public void deleteById(Integer id) {
        roleMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(LgqsRole role) {
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    public boolean checkExist(String name) {
        LgqsRoleExample example = new LgqsRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return roleMapper.countByExample(example) != 0;
    }

    public List<LgqsRole> queryAll() {
        LgqsRoleExample example = new LgqsRoleExample();
        example.or().andDeletedEqualTo(false);
        return roleMapper.selectByExample(example);
    }
}
