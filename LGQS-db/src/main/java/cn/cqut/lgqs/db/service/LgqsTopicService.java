package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsTopicMapper;
import cn.cqut.lgqs.db.domain.LgqsTopic;
import cn.cqut.lgqs.db.domain.LgqsTopic.Column;
import cn.cqut.lgqs.db.domain.LgqsTopicExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsTopicService {
    @Resource
    private LgqsTopicMapper topicMapper;
    private Column[] columns = new Column[]{Column.id, Column.title, Column.subtitle, Column.price, Column.picUrl, Column.readCount};

    public List<LgqsTopic> queryList(int offset, int limit) {
        return queryList(offset, limit, "add_time", "desc");
    }

    public List<LgqsTopic> queryList(int offset, int limit, String sort, String order) {
        LgqsTopicExample example = new LgqsTopicExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(offset, limit);
        return topicMapper.selectByExampleSelective(example, columns);
    }

    public int queryTotal() {
        LgqsTopicExample example = new LgqsTopicExample();
        example.or().andDeletedEqualTo(false);
        return (int) topicMapper.countByExample(example);
    }

    public LgqsTopic findById(Integer id) {
        LgqsTopicExample example = new LgqsTopicExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return topicMapper.selectOneByExampleWithBLOBs(example);
    }

    public List<LgqsTopic> queryRelatedList(Integer id, int offset, int limit) {
        LgqsTopicExample example = new LgqsTopicExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        List<LgqsTopic> topics = topicMapper.selectByExample(example);
        if (topics.size() == 0) {
            return queryList(offset, limit, "add_time", "desc");
        }
        LgqsTopic topic = topics.get(0);

        example = new LgqsTopicExample();
        example.or().andIdNotEqualTo(topic.getId()).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        List<LgqsTopic> relateds = topicMapper.selectByExampleWithBLOBs(example);
        if (relateds.size() != 0) {
            return relateds;
        }

        return queryList(offset, limit, "add_time", "desc");
    }

    public List<LgqsTopic> querySelective(String title, String subtitle, Integer page, Integer limit, String sort, String order) {
        LgqsTopicExample example = new LgqsTopicExample();
        LgqsTopicExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        if (!StringUtils.isEmpty(subtitle)) {
            criteria.andSubtitleLike("%" + subtitle + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return topicMapper.selectByExampleWithBLOBs(example);
    }

    public int updateById(LgqsTopic topic) {
        topic.setUpdateTime(LocalDateTime.now());
        LgqsTopicExample example = new LgqsTopicExample();
        example.or().andIdEqualTo(topic.getId());
        return topicMapper.updateByExampleSelective(topic, example);
    }

    public void deleteById(Integer id) {
        topicMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsTopic topic) {
        topic.setAddTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topicMapper.insertSelective(topic);
    }


    public void deleteByIds(List<Integer> ids) {
        LgqsTopicExample example = new LgqsTopicExample();
        example.or().andIdIn(ids).andDeletedEqualTo(false);
        LgqsTopic topic = new LgqsTopic();
        topic.setUpdateTime(LocalDateTime.now());
        topic.setDeleted(true);
        topicMapper.updateByExampleSelective(topic, example);
    }
}
