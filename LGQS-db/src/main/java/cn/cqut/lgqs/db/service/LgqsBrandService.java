package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsBrandMapper;
import cn.cqut.lgqs.db.domain.LgqsBrand;
import cn.cqut.lgqs.db.domain.LgqsBrandExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsBrandService {
    @Resource
    private LgqsBrandMapper brandMapper;
    private LgqsBrand.Column[] columns = new LgqsBrand.Column[]{LgqsBrand.Column.id, LgqsBrand.Column.name, LgqsBrand.Column.desc, LgqsBrand.Column.picUrl, LgqsBrand.Column.floorPrice};







    public void deleteById(Integer id) {
        brandMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsBrand brand) {
        brand.setAddTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        brandMapper.insertSelective(brand);
    }

}
