package cn.cqut.lgqs.wx.web;

import cn.cqut.lgqs.core.util.JacksonUtil;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.db.domain.LgqsCollect;
import cn.cqut.lgqs.db.domain.LgqsGoods;
import cn.cqut.lgqs.db.domain.LgqsTopic;
import cn.cqut.lgqs.db.service.LgqsCollectService;
import cn.cqut.lgqs.db.service.LgqsGoodsService;
import cn.cqut.lgqs.db.service.LgqsTopicService;
import cn.cqut.lgqs.wx.annotation.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户收藏服务
 */
@RestController
@RequestMapping("/wx/collect")
@Validated
public class WxCollectController {
    private final Log logger = LogFactory.getLog(WxCollectController.class);

    @Autowired
    private LgqsCollectService collectService;
    @Autowired
    private LgqsGoodsService goodsService;
    @Autowired
    private LgqsTopicService topicService;

    /**
     * 用户收藏列表
     *
     * @param userId 用户ID
     * @param type   类型，如果是0则是餐品收藏，如果是1则是专题收藏
     * @param page   分页页数
     * @param limit   分页大小
     * @return 用户收藏列表
     */
    @GetMapping("list")
    public Object list(@LoginUser Integer userId,
                       @NotNull Byte type,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        List<LgqsCollect> collectList = collectService.queryByType(userId, type, page, limit, sort, order);

        List<Object> collects = new ArrayList<>(collectList.size());
        for (LgqsCollect collect : collectList) {
            Map<String, Object> c = new HashMap<String, Object>();
            c.put("id", collect.getId());
            c.put("type", collect.getType());
            c.put("valueId", collect.getValueId());
            if (type == (byte)0){
            	//查询餐品信息
                LgqsGoods goods = goodsService.findById(collect.getValueId());
                c.put("name", goods.getName());
                c.put("brief", goods.getBrief());
                c.put("picUrl", goods.getPicUrl());
                c.put("retailPrice", goods.getRetailPrice());
            } else {
            	//查询专题信息
            	LgqsTopic topic = topicService.findById(collect.getValueId());
	            c.put("title", topic.getTitle());
	            c.put("subtitle", topic.getTitle());
	            c.put("price", topic.getPrice());
	            c.put("picUrl", topic.getPicUrl());
            }
            collects.add(c);
        }

        return ResponseUtil.okList(collects, collectList);
    }

    /**
     * 用户收藏添加或删除
     * <p>
     * 如果餐品没有收藏，则添加收藏；如果餐品已经收藏，则删除收藏状态。
     *
     * @param userId 用户ID
     * @param body   请求内容，{ type: xxx, valueId: xxx }
     * @return 操作结果
     */
    @PostMapping("addordelete")
    public Object addordelete(@LoginUser Integer userId, @RequestBody String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        Byte type = JacksonUtil.parseByte(body, "type");
        Integer valueId = JacksonUtil.parseInteger(body, "valueId");
        if (!ObjectUtils.allNotNull(type, valueId)) {
            return ResponseUtil.badArgument();
        }

        LgqsCollect collect = collectService.queryByTypeAndValue(userId, type, valueId);

        if (collect != null) {
            collectService.deleteById(collect.getId());
        } else {
            collect = new LgqsCollect();
            collect.setUserId(userId);
            collect.setValueId(valueId);
            collect.setType(type);
            collectService.add(collect);
        }

        return ResponseUtil.ok();
    }
}