package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsPermissionMapper;
import cn.cqut.lgqs.db.domain.LgqsPermission;
import cn.cqut.lgqs.db.domain.LgqsPermissionExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class LgqsPermissionService {
    @Resource
    private LgqsPermissionMapper permissionMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        LgqsPermissionExample example = new LgqsPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<LgqsPermission> permissionList = permissionMapper.selectByExample(example);

        for(LgqsPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }


    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        LgqsPermissionExample example = new LgqsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<LgqsPermission> permissionList = permissionMapper.selectByExample(example);

        for(LgqsPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public Set<String> queryByRoleId(List<Integer> roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds == null || roleIds.isEmpty()){
            return permissions;
        }

        LgqsPermissionExample example = new LgqsPermissionExample();
        example.or().andRoleIdIn(roleIds).andDeletedEqualTo(false);
        List<LgqsPermission> permissionList = permissionMapper.selectByExample(example);

        for(LgqsPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        LgqsPermissionExample example = new LgqsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public boolean checkSuperPermission(List<Integer> roleIds) {
        if(roleIds == null || roleIds.isEmpty()){
            return false;
        }

        LgqsPermissionExample example = new LgqsPermissionExample();
        example.or().andRoleIdIn(roleIds).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        LgqsPermissionExample example = new LgqsPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    public void add(LgqsPermission lgqsPermission) {
        lgqsPermission.setAddTime(LocalDateTime.now());
        lgqsPermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(lgqsPermission);
    }
}
