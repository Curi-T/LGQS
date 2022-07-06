package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsCommentMapper;
import cn.cqut.lgqs.db.domain.LgqsComment;
import cn.cqut.lgqs.db.domain.LgqsCommentExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsCommentService {
    @Resource
    private LgqsCommentMapper commentMapper;

    public List<LgqsComment> queryGoodsByGid(Integer id, int offset, int limit) {
        LgqsCommentExample example = new LgqsCommentExample();
        example.setOrderByClause(LgqsComment.Column.addTime.desc());
        example.or().andValueIdEqualTo(id).andTypeEqualTo((byte) 0).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    public List<LgqsComment> query(Byte type, Integer valueId, Integer showType, Integer offset, Integer limit) {
        LgqsCommentExample example = new LgqsCommentExample();
        example.setOrderByClause(LgqsComment.Column.addTime.desc());
        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        } else if (showType == 1) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        } else {
            throw new RuntimeException("showType不支持");
        }
        PageHelper.startPage(offset, limit);
        return commentMapper.selectByExample(example);
    }

    public int count(Byte type, Integer valueId, Integer showType) {
        LgqsCommentExample example = new LgqsCommentExample();
        if (showType == 0) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andDeletedEqualTo(false);
        } else if (showType == 1) {
            example.or().andValueIdEqualTo(valueId).andTypeEqualTo(type).andHasPictureEqualTo(true).andDeletedEqualTo(false);
        } else {
            throw new RuntimeException("showType不支持");
        }
        return (int) commentMapper.countByExample(example);
    }

    public int save(LgqsComment comment) {
        comment.setAddTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        return commentMapper.insertSelective(comment);
    }

    public List<LgqsComment> querySelective(String userId, String valueId, Integer page, Integer size, String sort, String order) {
        LgqsCommentExample example = new LgqsCommentExample();
        LgqsCommentExample.Criteria criteria = example.createCriteria();

        // type=2 是订单餐品回复，这里过滤
        criteria.andTypeNotEqualTo((byte) 2);

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(valueId)) {
            criteria.andValueIdEqualTo(Integer.valueOf(valueId)).andTypeEqualTo((byte) 0);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return commentMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        commentMapper.logicalDeleteByPrimaryKey(id);
    }

    public LgqsComment findById(Integer id) {
        return commentMapper.selectByPrimaryKey(id);
    }

    public int updateById(LgqsComment comment) {
        return commentMapper.updateByPrimaryKeySelective(comment);
    }
}
