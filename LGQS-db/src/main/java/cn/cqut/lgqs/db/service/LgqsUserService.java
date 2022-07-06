package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsUserMapper;
import cn.cqut.lgqs.db.domain.LgqsUser;
import cn.cqut.lgqs.db.domain.LgqsUserExample;
import cn.cqut.lgqs.db.domain.UserVo;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsUserService {
    @Resource
    private LgqsUserMapper userMapper;

    public LgqsUser findById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
    }

    public UserVo findUserVoById(Integer userId) {
        LgqsUser user = findById(userId);
        UserVo userVo = new UserVo();
        userVo.setNickname(user.getNickname());
        userVo.setAvatar(user.getAvatar());
        return userVo;
    }

    public LgqsUser queryByOid(String openId) {
        LgqsUserExample example = new LgqsUserExample();
        example.or().andWeixinOpenidEqualTo(openId).andDeletedEqualTo(false);
        return userMapper.selectOneByExample(example);
    }

    public void add(LgqsUser user) {
        user.setAddTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.insertSelective(user);
    }

    public int updateById(LgqsUser user) {
        user.setUpdateTime(LocalDateTime.now());
        return userMapper.updateByPrimaryKeySelective(user);
    }

    public List<LgqsUser> querySelective(String username, String mobile, Integer page, Integer size, String sort, String order) {
        LgqsUserExample example = new LgqsUserExample();
        LgqsUserExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        if (!StringUtils.isEmpty(mobile)) {
            criteria.andMobileEqualTo(mobile);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return userMapper.selectByExample(example);
    }

    public int count() {
        LgqsUserExample example = new LgqsUserExample();
        example.or().andDeletedEqualTo(false);

        return (int) userMapper.countByExample(example);
    }

    public List<LgqsUser> queryByUsername(String username) {
        LgqsUserExample example = new LgqsUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public boolean checkByUsername(String username) {
        LgqsUserExample example = new LgqsUserExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return userMapper.countByExample(example) != 0;
    }

    public List<LgqsUser> queryByMobile(String mobile) {
        LgqsUserExample example = new LgqsUserExample();
        example.or().andMobileEqualTo(mobile).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public List<LgqsUser> queryByOpenid(String openid) {
        LgqsUserExample example = new LgqsUserExample();
        example.or().andWeixinOpenidEqualTo(openid).andDeletedEqualTo(false);
        return userMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        userMapper.logicalDeleteByPrimaryKey(id);
    }
}
