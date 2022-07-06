package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsFeedbackMapper;
import cn.cqut.lgqs.db.domain.LgqsFeedback;
import cn.cqut.lgqs.db.domain.LgqsFeedbackExample;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Yogeek
 * @date 2018/8/27 11:39
 */
@Service
public class LgqsFeedbackService {
    @Autowired
    private LgqsFeedbackMapper feedbackMapper;

    public Integer add(LgqsFeedback feedback) {
        feedback.setAddTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.insertSelective(feedback);
    }

    public List<LgqsFeedback> querySelective(Integer userId, String username, Integer page, Integer limit, String sort, String order) {
        LgqsFeedbackExample example = new LgqsFeedbackExample();
        LgqsFeedbackExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return feedbackMapper.selectByExample(example);
    }
}
