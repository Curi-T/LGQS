package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsNoticeMapper;
import cn.cqut.lgqs.db.domain.LgqsNotice;
import cn.cqut.lgqs.db.domain.LgqsNoticeExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsNoticeService {
    @Resource
    private LgqsNoticeMapper noticeMapper;


    public List<LgqsNotice> querySelective(String title, String content, Integer page, Integer limit, String sort, String order) {
        LgqsNoticeExample example = new LgqsNoticeExample();
        LgqsNoticeExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        if (!StringUtils.isEmpty(content)) {
            criteria.andContentLike("%" + content + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return noticeMapper.selectByExample(example);
    }

    public int updateById(LgqsNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        return noticeMapper.updateByPrimaryKeySelective(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsNotice notice) {
        notice.setAddTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        noticeMapper.insertSelective(notice);
    }

    public LgqsNotice findById(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    public void deleteByIds(List<Integer> ids) {
        LgqsNoticeExample example = new LgqsNoticeExample();
        example.or().andIdIn(ids).andDeletedEqualTo(false);
        LgqsNotice notice = new LgqsNotice();
        notice.setUpdateTime(LocalDateTime.now());
        notice.setDeleted(true);
        noticeMapper.updateByExampleSelective(notice, example);
    }
}
