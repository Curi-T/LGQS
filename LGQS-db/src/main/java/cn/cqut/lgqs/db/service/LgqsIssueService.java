package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsIssueMapper;
import cn.cqut.lgqs.db.domain.LgqsIssue;
import cn.cqut.lgqs.db.domain.LgqsIssueExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsIssueService {
    @Resource
    private LgqsIssueMapper issueMapper;

    public void deleteById(Integer id) {
        issueMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsIssue issue) {
        issue.setAddTime(LocalDateTime.now());
        issue.setUpdateTime(LocalDateTime.now());
        issueMapper.insertSelective(issue);
    }

    public List<LgqsIssue> querySelective(String question, Integer page, Integer limit, String sort, String order) {
        LgqsIssueExample example = new LgqsIssueExample();
        LgqsIssueExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(question)) {
            criteria.andQuestionLike("%" + question + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return issueMapper.selectByExample(example);
    }

    public int updateById(LgqsIssue issue) {
        issue.setUpdateTime(LocalDateTime.now());
        return issueMapper.updateByPrimaryKeySelective(issue);
    }

    public LgqsIssue findById(Integer id) {
        return issueMapper.selectByPrimaryKey(id);
    }
}
