package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsNoticeAdminMapper;
import cn.cqut.lgqs.db.domain.LgqsNoticeAdmin;
import cn.cqut.lgqs.db.domain.LgqsNoticeAdminExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsNoticeAdminService {
    @Resource
    private LgqsNoticeAdminMapper noticeAdminMapper;

    public List<LgqsNoticeAdmin> querySelective(String title, String type, Integer adminId, Integer page, Integer limit, String sort, String order) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        LgqsNoticeAdminExample.Criteria criteria = example.createCriteria();

        if(!StringUtils.isEmpty(title)){
            criteria.andNoticeTitleLike("%" + title + "%");
        }

        if(type.equals("read")){
         criteria.andReadTimeIsNotNull();
        }
        else if(type.equals("unread")){
            criteria.andReadTimeIsNull();
        }
        criteria.andAdminIdEqualTo(adminId);
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return noticeAdminMapper.selectByExample(example);
    }

    public LgqsNoticeAdmin find(Integer noticeId, Integer adminId) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andNoticeIdEqualTo(noticeId).andAdminIdEqualTo(adminId).andDeletedEqualTo(false);
        return noticeAdminMapper.selectOneByExample(example);
    }

    public void add(LgqsNoticeAdmin noticeAdmin) {
        noticeAdmin.setAddTime(LocalDateTime.now());
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdminMapper.insertSelective(noticeAdmin);
    }

    public void update(LgqsNoticeAdmin noticeAdmin) {
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdminMapper.updateByPrimaryKeySelective(noticeAdmin);
    }

    public void markReadByIds(List<Integer> ids, Integer adminId) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andIdIn(ids).andAdminIdEqualTo(adminId).andDeletedEqualTo(false);
        LgqsNoticeAdmin noticeAdmin = new LgqsNoticeAdmin();
        LocalDateTime now = LocalDateTime.now();
        noticeAdmin.setReadTime(now);
        noticeAdmin.setUpdateTime(now);
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);
    }

    public void deleteById(Integer id, Integer adminId) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andIdEqualTo(id).andAdminIdEqualTo(adminId).andDeletedEqualTo(false);
        LgqsNoticeAdmin noticeAdmin = new LgqsNoticeAdmin();
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdmin.setDeleted(true);
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);
    }

    public void deleteByIds(List<Integer> ids, Integer adminId) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andIdIn(ids).andAdminIdEqualTo(adminId).andDeletedEqualTo(false);
        LgqsNoticeAdmin noticeAdmin = new LgqsNoticeAdmin();
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdmin.setDeleted(true);
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);
    }





    public void deleteByNoticeIds(List<Integer> ids) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andNoticeIdIn(ids).andDeletedEqualTo(false);
        LgqsNoticeAdmin noticeAdmin = new LgqsNoticeAdmin();
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdmin.setDeleted(true);
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);
    }

    public int countReadByNoticeId(Integer noticeId) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andNoticeIdEqualTo(noticeId).andReadTimeIsNotNull().andDeletedEqualTo(false);
        return (int)noticeAdminMapper.countByExample(example);
    }

    public void updateByNoticeId(LgqsNoticeAdmin noticeAdmin, Integer noticeId) {
        LgqsNoticeAdminExample example = new LgqsNoticeAdminExample();
        example.or().andNoticeIdEqualTo(noticeId).andDeletedEqualTo(false);
        noticeAdmin.setUpdateTime(LocalDateTime.now());
        noticeAdminMapper.updateByExampleSelective(noticeAdmin, example);
    }
}
