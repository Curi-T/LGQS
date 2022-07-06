package cn.cqut.lgqs.db.service;

import cn.cqut.lgqs.db.dao.LgqsCartMapper;
import cn.cqut.lgqs.db.domain.LgqsCart;
import cn.cqut.lgqs.db.domain.LgqsCartExample;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LgqsCartService {
    @Resource
    private LgqsCartMapper cartMapper;

    public LgqsCart queryExist(Integer goodsId, Integer productId, Integer userId) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andProductIdEqualTo(productId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(example);
    }

    public void add(LgqsCart cart) {
        cart.setAddTime(LocalDateTime.now());
        cart.setUpdateTime(LocalDateTime.now());
        cartMapper.insertSelective(cart);
    }

    public int updateById(LgqsCart cart) {
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByPrimaryKeySelective(cart);
    }

    public List<LgqsCart> queryByUid(int userId) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }


    public List<LgqsCart> queryByUidAndChecked(Integer userId) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.selectByExample(example);
    }

    public int delete(List<Integer> productIdList, int userId) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(productIdList);
        return cartMapper.logicalDeleteByExample(example);
    }

    public LgqsCart findById(Integer id) {
        return cartMapper.selectByPrimaryKey(id);
    }

    public LgqsCart findById(Integer userId, Integer id) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andUserIdEqualTo(userId).andIdEqualTo(id).andDeletedEqualTo(false);
        return cartMapper.selectOneByExample(example);
    }

    public int updateCheck(Integer userId, List<Integer> idsList, Boolean checked) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andUserIdEqualTo(userId).andProductIdIn(idsList).andDeletedEqualTo(false);
        LgqsCart cart = new LgqsCart();
        cart.setChecked(checked);
        cart.setUpdateTime(LocalDateTime.now());
        return cartMapper.updateByExampleSelective(cart, example);
    }

    public void clearGoods(Integer userId) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andUserIdEqualTo(userId).andCheckedEqualTo(true);
        LgqsCart cart = new LgqsCart();
        cart.setDeleted(true);
        cartMapper.updateByExampleSelective(cart, example);
    }

    public List<LgqsCart> querySelective(Integer userId, Integer goodsId, Integer page, Integer limit, String sort, String order) {
        LgqsCartExample example = new LgqsCartExample();
        LgqsCartExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(goodsId);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return cartMapper.selectByExample(example);
    }

    public void deleteById(Integer id) {
        cartMapper.logicalDeleteByPrimaryKey(id);
    }

    public boolean checkExist(Integer goodsId) {
        LgqsCartExample example = new LgqsCartExample();
        example.or().andGoodsIdEqualTo(goodsId).andCheckedEqualTo(true).andDeletedEqualTo(false);
        return cartMapper.countByExample(example) != 0;
    }

    public void updateProduct(Integer id, String goodsSn, String goodsName, BigDecimal price, String url) {
        LgqsCart cart = new LgqsCart();
        cart.setPrice(price);
        cart.setPicUrl(url);
        cart.setGoodsSn(goodsSn);
        cart.setGoodsName(goodsName);
        LgqsCartExample example = new LgqsCartExample();
        example.or().andProductIdEqualTo(id);
        cartMapper.updateByExampleSelective(cart, example);
    }
}
