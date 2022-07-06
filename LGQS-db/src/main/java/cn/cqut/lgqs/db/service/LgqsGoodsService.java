package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsGoodsMapper;
import cn.cqut.lgqs.db.domain.LgqsGoods;
import cn.cqut.lgqs.db.domain.LgqsGoodsExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LgqsGoodsService {
    LgqsGoods.Column[] columns = new LgqsGoods.Column[]{LgqsGoods.Column.id, LgqsGoods.Column.name, LgqsGoods.Column.brief, LgqsGoods.Column.picUrl, LgqsGoods.Column.isHot, LgqsGoods.Column.isNew, LgqsGoods.Column.counterPrice, LgqsGoods.Column.retailPrice};
    @Resource
    private LgqsGoodsMapper goodsMapper;

    /**
     * 获取热卖餐品
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<LgqsGoods> queryByHot(int offset, int limit) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andIsHotEqualTo(true).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    /**
     * 获取新品上市
     *
     * @param offset
     * @param limit
     * @return
     */
    public List<LgqsGoods> queryByNew(int offset, int limit) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andIsNewEqualTo(true).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    /**
     * 获取分类下的餐品
     *
     * @param catList
     * @param offset
     * @param limit
     * @return
     */
    public List<LgqsGoods> queryByCategory(List<Integer> catList, int offset, int limit) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andCategoryIdIn(catList).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time  desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }


    /**
     * 获取分类下的餐品
     *
     * @param catId
     * @param offset
     * @param limit
     * @return
     */
    public List<LgqsGoods> queryByCategory(Integer catId, int offset, int limit) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andCategoryIdEqualTo(catId).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        example.setOrderByClause("add_time desc");
        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }


    public List<LgqsGoods> querySelective(Integer catId, Integer brandId, String keywords, Boolean isHot, Boolean isNew, Integer offset, Integer limit, String sort, String order) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        LgqsGoodsExample.Criteria criteria1 = example.or();
        LgqsGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(catId) && catId != 0) {
            criteria1.andCategoryIdEqualTo(catId);
            criteria2.andCategoryIdEqualTo(catId);
        }
        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(isNew)) {
            criteria1.andIsNewEqualTo(isNew);
            criteria2.andIsNewEqualTo(isNew);
        }
        if (!StringUtils.isEmpty(isHot)) {
            criteria1.andIsHotEqualTo(isHot);
            criteria2.andIsHotEqualTo(isHot);
        }
        if (!StringUtils.isEmpty(keywords)) {
            criteria1.andKeywordsLike("%" + keywords + "%");
            criteria2.andNameLike("%" + keywords + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(offset, limit);

        return goodsMapper.selectByExampleSelective(example, columns);
    }

    public List<LgqsGoods> querySelective(Integer goodsId, String goodsSn, String name, Integer page, Integer size, String sort, String order) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        LgqsGoodsExample.Criteria criteria = example.createCriteria();

        if (goodsId != null) {
            criteria.andIdEqualTo(goodsId);
        }
        if (!StringUtils.isEmpty(goodsSn)) {
            criteria.andGoodsSnEqualTo(goodsSn);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return goodsMapper.selectByExampleWithBLOBs(example);
    }

    /**
     * 获取某个餐品信息,包含完整信息
     *
     * @param id
     * @return
     */
    public LgqsGoods findById(Integer id) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExampleWithBLOBs(example);
    }

    /**
     * 获取某个餐品信息，仅展示相关内容
     *
     * @param id
     * @return
     */
    public LgqsGoods findByIdVO(Integer id) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andIdEqualTo(id).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.selectOneByExampleSelective(example, columns);
    }


    /**
     * 获取所有在售物品总数
     *
     * @return
     */
    public Integer queryOnSale() {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return (int) goodsMapper.countByExample(example);
    }

    public int updateById(LgqsGoods goods) {
        goods.setUpdateTime(LocalDateTime.now());
        return goodsMapper.updateByPrimaryKeySelective(goods);
    }

    public void deleteById(Integer id) {
        goodsMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LgqsGoods goods) {
        goods.setAddTime(LocalDateTime.now());
        goods.setUpdateTime(LocalDateTime.now());
        goodsMapper.insertSelective(goods);
    }

    /**
     * 获取所有物品总数，包括在售的和下架的，但是不包括已删除的餐品
     *
     * @return
     */
    public int count() {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andDeletedEqualTo(false);
        return (int) goodsMapper.countByExample(example);
    }

    public List<Integer> getCatIds(Integer brandId, String keywords, Boolean isHot, Boolean isNew) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        LgqsGoodsExample.Criteria criteria1 = example.or();
        LgqsGoodsExample.Criteria criteria2 = example.or();

        if (!StringUtils.isEmpty(brandId)) {
            criteria1.andBrandIdEqualTo(brandId);
            criteria2.andBrandIdEqualTo(brandId);
        }
        if (!StringUtils.isEmpty(isNew)) {
            criteria1.andIsNewEqualTo(isNew);
            criteria2.andIsNewEqualTo(isNew);
        }
        if (!StringUtils.isEmpty(isHot)) {
            criteria1.andIsHotEqualTo(isHot);
            criteria2.andIsHotEqualTo(isHot);
        }
        if (!StringUtils.isEmpty(keywords)) {
            criteria1.andKeywordsLike("%" + keywords + "%");
            criteria2.andNameLike("%" + keywords + "%");
        }
        criteria1.andIsOnSaleEqualTo(true);
        criteria2.andIsOnSaleEqualTo(true);
        criteria1.andDeletedEqualTo(false);
        criteria2.andDeletedEqualTo(false);

        List<LgqsGoods> goodsList = goodsMapper.selectByExampleSelective(example, LgqsGoods.Column.categoryId);
        List<Integer> cats = new ArrayList<Integer>();
        for (LgqsGoods goods : goodsList) {
            cats.add(goods.getCategoryId());
        }
        return cats;
    }

    public boolean checkExistByName(String name) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andNameEqualTo(name).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.countByExample(example) != 0;
    }

    public List<LgqsGoods> queryByIds(Integer[] ids) {
        LgqsGoodsExample example = new LgqsGoodsExample();
        example.or().andIdIn(Arrays.asList(ids)).andIsOnSaleEqualTo(true).andDeletedEqualTo(false);
        return goodsMapper.selectByExampleSelective(example, columns);
    }
}
