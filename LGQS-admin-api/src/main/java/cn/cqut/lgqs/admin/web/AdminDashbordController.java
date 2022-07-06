package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.db.service.LgqsGoodsProductService;
import cn.cqut.lgqs.db.service.LgqsGoodsService;
import cn.cqut.lgqs.db.service.LgqsOrderService;
import cn.cqut.lgqs.db.service.LgqsUserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/dashboard")
@Validated
public class AdminDashbordController {
    private final Log logger = LogFactory.getLog(AdminDashbordController.class);

    @Autowired
    private LgqsUserService userService;
    @Autowired
    private LgqsGoodsService goodsService;
    @Autowired
    private LgqsGoodsProductService productService;
    @Autowired
    private LgqsOrderService orderService;

    @GetMapping("")
    public Object info() {
        int userTotal = userService.count();
        int goodsTotal = goodsService.count();
        int productTotal = productService.count();
        int orderTotal = orderService.count();
        Map<String, Integer> data = new HashMap<>();
        data.put("userTotal", userTotal);
        data.put("goodsTotal", goodsTotal);
        data.put("productTotal", productTotal);
        data.put("orderTotal", orderTotal);

        return ResponseUtil.ok(data);
    }

}
