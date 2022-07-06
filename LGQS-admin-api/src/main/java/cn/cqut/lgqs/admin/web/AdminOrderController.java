package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.admin.annotation.RequiresPermissionsDesc;
import cn.cqut.lgqs.admin.service.AdminOrderService;
import cn.cqut.lgqs.core.express.ExpressService;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/order")
@Validated
public class AdminOrderController {
    private final Log logger = LogFactory.getLog(AdminOrderController.class);

    @Autowired
    private AdminOrderService adminOrderService;
    @Autowired
    private ExpressService expressService;

    /**
     * 查询订单
     *
     * @param orderSn
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequiresPermissions("admin:order:list")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String nickname, String consignee, String orderSn,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                       @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                       @RequestParam(required = false) List<Short> orderStatusArray,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        return adminOrderService.list(nickname, consignee, orderSn, start, end, orderStatusArray, page, limit, sort, order);
    }

    /**
     * 查询配送公司
     *
     * @return
     */
    @GetMapping("/channel")
    public Object channel() {
        List<Map<String, String>> vendors = new ArrayList<>();
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("code", "商家配送");
        stringStringHashMap.put("name", "商家配送");
        vendors.add(stringStringHashMap);

        HashMap<String, String> stringStringHashMap1 = new HashMap<>();
        stringStringHashMap1.put("code", "堂食");
        stringStringHashMap1.put("name", "堂食");
        vendors.add(stringStringHashMap1);
        return ResponseUtil.ok(vendors);
//        return ResponseUtil.ok(expressService.getVendors());
    }

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    @RequiresPermissions("admin:order:read")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "详情")
    @GetMapping("/detail")
    public Object detail(@NotNull Integer id) {
        return adminOrderService.detail(id);
    }

    /**
     * 订单退款
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @RequiresPermissions("admin:order:refund")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "订单退款")
    @PostMapping("/refund")
    public Object refund(@RequestBody String body) {
        return adminOrderService.refund(body);
    }

    /**
     * 发货
     *
     * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
     * @return 订单操作结果
     */
    @RequiresPermissions("admin:order:ship")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "订单发货")
    @PostMapping("/ship")
    public Object ship(@RequestBody String body) {
        return adminOrderService.ship(body);
    }

    @RequiresPermissions("admin:order:pay")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "订单收款")
    @PostMapping("/pay")
    public Object pay(@RequestBody String body) {
        return adminOrderService.pay(body);
    }

    /**
     * 删除订单
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @RequiresPermissions("admin:order:delete")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "订单删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody String body) {
        return adminOrderService.delete(body);
    }

    /**
     * 回复订单餐品
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @RequiresPermissions("admin:order:reply")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "订单管理"}, button = "订单餐品回复")
    @PostMapping("/reply")
    public Object reply(@RequestBody String body) {
        return adminOrderService.reply(body);
    }
}
