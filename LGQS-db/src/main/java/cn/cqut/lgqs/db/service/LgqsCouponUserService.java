package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsCouponUserMapper;
import cn.cqut.lgqs.db.domain.LgqsCouponUser;
import cn.cqut.lgqs.db.domain.LgqsCouponUserExample;
import cn.cqut.lgqs.db.util.CouponUserConstant;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsCouponUserService {
    @Resource
    private LgqsCouponUserMapper couponUserMapper;

    public Integer countCoupon(Integer couponId) {
        LgqsCouponUserExample example = new LgqsCouponUserExample();
        example.or().andCouponIdEqualTo(couponId).andDeletedEqualTo(false);
        return (int)couponUserMapper.countByExample(example);
    }

    public Integer countUserAndCoupon(Integer userId, Integer couponId) {
        LgqsCouponUserExample example = new LgqsCouponUserExample();
        example.or().andUserIdEqualTo(userId).andCouponIdEqualTo(couponId).andDeletedEqualTo(false);
        return (int)couponUserMapper.countByExample(example);
    }

    public void add(LgqsCouponUser couponUser) {
        couponUser.setAddTime(LocalDateTime.now());
        couponUser.setUpdateTime(LocalDateTime.now());
        couponUserMapper.insertSelective(couponUser);
    }

    public List<LgqsCouponUser> queryList(Integer userId, Integer couponId, Short status, Integer page, Integer size, String sort, String order) {
        LgqsCouponUserExample example = new LgqsCouponUserExample();
        LgqsCouponUserExample.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if(couponId != null){
            criteria.andCouponIdEqualTo(couponId);
        }
        if (status != null) {
            criteria.andStatusEqualTo(status);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        if (!StringUtils.isEmpty(page) && !StringUtils.isEmpty(size)) {
            PageHelper.startPage(page, size);
        }

        return couponUserMapper.selectByExample(example);
    }

    public List<LgqsCouponUser> queryAll(Integer userId, Integer couponId) {
        return queryList(userId, couponId, CouponUserConstant.STATUS_USABLE, null, null, "add_time", "desc");
    }

    public List<LgqsCouponUser> queryAll(Integer userId) {
        return queryList(userId, null, CouponUserConstant.STATUS_USABLE, null, null, "add_time", "desc");
    }

    public LgqsCouponUser queryOne(Integer userId, Integer couponId) {
        List<LgqsCouponUser> couponUserList = queryList(userId, couponId, CouponUserConstant.STATUS_USABLE, 1, 1, "add_time", "desc");
        if(couponUserList.size() == 0){
            return null;
        }
        return couponUserList.get(0);
    }

    public LgqsCouponUser findById(Integer id) {
        return couponUserMapper.selectByPrimaryKey(id);
    }


    public int update(LgqsCouponUser couponUser) {
        couponUser.setUpdateTime(LocalDateTime.now());
        return couponUserMapper.updateByPrimaryKeySelective(couponUser);
    }

    public List<LgqsCouponUser> queryExpired() {
        LgqsCouponUserExample example = new LgqsCouponUserExample();
        example.or().andStatusEqualTo(CouponUserConstant.STATUS_USABLE).andEndTimeLessThan(LocalDateTime.now()).andDeletedEqualTo(false);
        return couponUserMapper.selectByExample(example);
    }

    public List<LgqsCouponUser> findByOid(Integer orderId) {
        LgqsCouponUserExample example = new LgqsCouponUserExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return couponUserMapper.selectByExample(example);
    }
}
