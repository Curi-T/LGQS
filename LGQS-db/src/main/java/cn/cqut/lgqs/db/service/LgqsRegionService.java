package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsRegionMapper;
import cn.cqut.lgqs.db.domain.LgqsRegion;
import cn.cqut.lgqs.db.domain.LgqsRegionExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author CuriT
 */
@Service
public class LgqsRegionService {

    @Resource
    private LgqsRegionMapper regionMapper;

    public List<LgqsRegion> getAll(){
        LgqsRegionExample example = new LgqsRegionExample();
        byte b = 4;
        example.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(example);
    }

    public List<LgqsRegion> queryByPid(Integer parentId) {
        LgqsRegionExample example = new LgqsRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public LgqsRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    public List<LgqsRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        LgqsRegionExample example = new LgqsRegionExample();
        LgqsRegionExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return regionMapper.selectByExample(example);
    }

}
