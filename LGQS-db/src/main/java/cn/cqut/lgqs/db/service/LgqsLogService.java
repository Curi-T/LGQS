package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsLogMapper;
import cn.cqut.lgqs.db.domain.LgqsLog;
import cn.cqut.lgqs.db.domain.LgqsLogExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsLogService {
    @Resource
    private LgqsLogMapper logMapper;

    public void deleteById(Integer id) {
        logMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsLog log) {
        log.setAddTime(LocalDateTime.now());
        log.setUpdateTime(LocalDateTime.now());
        logMapper.insertSelective(log);
    }

    public List<LgqsLog> querySelective(String name, Integer page, Integer size, String sort, String order) {
        LgqsLogExample example = new LgqsLogExample();
        LgqsLogExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andAdminLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return logMapper.selectByExample(example);
    }
}
