package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsSearchHistoryMapper;
import cn.cqut.lgqs.db.domain.LgqsSearchHistory;
import cn.cqut.lgqs.db.domain.LgqsSearchHistoryExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsSearchHistoryService {
    @Resource
    private LgqsSearchHistoryMapper searchHistoryMapper;

    public void save(LgqsSearchHistory searchHistory) {
        searchHistory.setAddTime(LocalDateTime.now());
        searchHistory.setUpdateTime(LocalDateTime.now());
        searchHistoryMapper.insertSelective(searchHistory);
    }

    public List<LgqsSearchHistory> queryByUid(int uid) {
        LgqsSearchHistoryExample example = new LgqsSearchHistoryExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        example.setDistinct(true);
        return searchHistoryMapper.selectByExampleSelective(example, LgqsSearchHistory.Column.keyword);
    }

    public void deleteByUid(int uid) {
        LgqsSearchHistoryExample example = new LgqsSearchHistoryExample();
        example.or().andUserIdEqualTo(uid);
        searchHistoryMapper.logicalDeleteByExample(example);
    }

    public List<LgqsSearchHistory> querySelective(String userId, String keyword, Integer page, Integer size, String sort, String order) {
        LgqsSearchHistoryExample example = new LgqsSearchHistoryExample();
        LgqsSearchHistoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andKeywordLike("%" + keyword + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return searchHistoryMapper.selectByExample(example);
    }
}
