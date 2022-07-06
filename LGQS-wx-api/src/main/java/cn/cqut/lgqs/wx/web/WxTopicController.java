package cn.cqut.lgqs.wx.web;

import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.db.domain.LgqsGoods;
import cn.cqut.lgqs.db.domain.LgqsTopic;
import cn.cqut.lgqs.db.service.LgqsCollectService;
import cn.cqut.lgqs.db.service.LgqsGoodsService;
import cn.cqut.lgqs.db.service.LgqsTopicService;
import cn.cqut.lgqs.wx.annotation.LoginUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专题服务
 */
@RestController
@RequestMapping("/wx/topic")
@Validated
public class WxTopicController {
    private final Log logger = LogFactory.getLog(WxTopicController.class);

    @Autowired
    private LgqsTopicService topicService;
    @Autowired
    private LgqsGoodsService goodsService;
	@Autowired
	private LgqsCollectService collectService;

    /**
     * 专题列表
     *
     * @param page 分页页数
     * @param limit 分页大小
     * @return 专题列表
     */
    @GetMapping("list")
    public Object list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LgqsTopic> topicList = topicService.queryList(page, limit, sort, order);
        return ResponseUtil.okList(topicList);
    }

    /**
     * 专题详情
     *
     * @param id 专题ID
     * @return 专题详情
     */
    @GetMapping("detail")
    public Object detail(@LoginUser Integer userId, @NotNull Integer id) {
        LgqsTopic topic = topicService.findById(id);
        List<LgqsGoods> goods = new ArrayList<>();
        for (Integer i : topic.getGoods()) {
            LgqsGoods good = goodsService.findByIdVO(i);
            if (null != good)
                goods.add(good);
        }
        
		// 用户收藏
		int userHasCollect = 0;
		if (userId != null) {
			userHasCollect = collectService.count(userId, (byte)1, id);
		}

        Map<String, Object> entity = new HashMap<String, Object>();
        entity.put("topic", topic);
        entity.put("goods", goods);
        entity.put("userHasCollect", userHasCollect);
        return ResponseUtil.ok(entity);
    }

    /**
     * 相关专题
     *
     * @param id 专题ID
     * @return 相关专题
     */
    @GetMapping("related")
    public Object related(@NotNull Integer id) {
        List<LgqsTopic> topicRelatedList = topicService.queryRelatedList(id, 0, 4);
        return ResponseUtil.okList(topicRelatedList);
    }
}