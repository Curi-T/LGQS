package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsAdminMapper;
import cn.cqut.lgqs.db.domain.LgqsAdmin;
import cn.cqut.lgqs.db.domain.LgqsAdminExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsAdminService {
    private final LgqsAdmin.Column[] result = new LgqsAdmin.Column[]{LgqsAdmin.Column.id, LgqsAdmin.Column.username, LgqsAdmin.Column.avatar, LgqsAdmin.Column.roleIds};
    @Resource
    private LgqsAdminMapper adminMapper;

    public List<LgqsAdmin> findAdmin(String username) {
        LgqsAdminExample example = new LgqsAdminExample();
        example.or().andUsernameEqualTo(username).andDeletedEqualTo(false);
        return adminMapper.selectByExample(example);
    }

    public LgqsAdmin findAdmin(Integer id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public List<LgqsAdmin> querySelective(String username, Integer page, Integer limit, String sort, String order) {
        LgqsAdminExample example = new LgqsAdminExample();
        LgqsAdminExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return adminMapper.selectByExampleSelective(example, result);
    }

    public int updateById(LgqsAdmin admin) {
        admin.setUpdateTime(LocalDateTime.now());
        return adminMapper.updateByPrimaryKeySelective(admin);
    }

    public void deleteById(Integer id) {
        adminMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsAdmin admin) {
        admin.setAddTime(LocalDateTime.now());
        admin.setUpdateTime(LocalDateTime.now());
        adminMapper.insertSelective(admin);
    }

    public LgqsAdmin findById(Integer id) {
        return adminMapper.selectByPrimaryKeySelective(id, result);
    }

    public List<LgqsAdmin> all() {
        LgqsAdminExample example = new LgqsAdminExample();
        example.or().andDeletedEqualTo(false);
        return adminMapper.selectByExample(example);
    }
}
