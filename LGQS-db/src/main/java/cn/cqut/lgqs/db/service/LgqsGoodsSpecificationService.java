package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsGoodsSpecificationMapper;
import cn.cqut.lgqs.db.domain.LgqsGoodsSpecification;
import cn.cqut.lgqs.db.domain.LgqsGoodsSpecificationExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LgqsGoodsSpecificationService {
    @Resource
    private LgqsGoodsSpecificationMapper goodsSpecificationMapper;

    public List<LgqsGoodsSpecification> queryByGid(Integer id) {
        LgqsGoodsSpecificationExample example = new LgqsGoodsSpecificationExample();
        example.or().andGoodsIdEqualTo(id).andDeletedEqualTo(false);
        return goodsSpecificationMapper.selectByExample(example);
    }

    public LgqsGoodsSpecification findById(Integer id) {
        return goodsSpecificationMapper.selectByPrimaryKey(id);
    }

    public void deleteByGid(Integer gid) {
        LgqsGoodsSpecificationExample example = new LgqsGoodsSpecificationExample();
        example.or().andGoodsIdEqualTo(gid);
        goodsSpecificationMapper.logicalDeleteByExample(example);
    }

    public void add(LgqsGoodsSpecification goodsSpecification) {
        goodsSpecification.setAddTime(LocalDateTime.now());
        goodsSpecification.setUpdateTime(LocalDateTime.now());
        goodsSpecificationMapper.insertSelective(goodsSpecification);
    }

    /**
     * [
     * {
     * name: '',
     * valueList: [ {}, {}]
     * },
     * {
     * name: '',
     * valueList: [ {}, {}]
     * }
     * ]
     *
     * @param id
     * @return
     */
    public Object getSpecificationVoList(Integer id) {
        List<LgqsGoodsSpecification> goodsSpecificationList = queryByGid(id);

        Map<String, VO> map = new HashMap<>();
        List<VO> specificationVoList = new ArrayList<>();

        for (LgqsGoodsSpecification goodsSpecification : goodsSpecificationList) {
            String specification = goodsSpecification.getSpecification();
            VO goodsSpecificationVo = map.get(specification);
            if (goodsSpecificationVo == null) {
                goodsSpecificationVo = new VO();
                goodsSpecificationVo.setName(specification);
                List<LgqsGoodsSpecification> valueList = new ArrayList<>();
                valueList.add(goodsSpecification);
                goodsSpecificationVo.setValueList(valueList);
                map.put(specification, goodsSpecificationVo);
                specificationVoList.add(goodsSpecificationVo);
            } else {
                List<LgqsGoodsSpecification> valueList = goodsSpecificationVo.getValueList();
                valueList.add(goodsSpecification);
            }
        }

        return specificationVoList;
    }

    public void updateById(LgqsGoodsSpecification specification) {
        specification.setUpdateTime(LocalDateTime.now());
        goodsSpecificationMapper.updateByPrimaryKeySelective(specification);
    }

    private class VO {
        private String name;
        private List<LgqsGoodsSpecification> valueList;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<LgqsGoodsSpecification> getValueList() {
            return valueList;
        }

        public void setValueList(List<LgqsGoodsSpecification> valueList) {
            this.valueList = valueList;
        }
    }

}
