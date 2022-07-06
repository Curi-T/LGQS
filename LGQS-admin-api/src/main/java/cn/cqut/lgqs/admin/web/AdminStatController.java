package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.admin.vo.StatVo;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.db.service.StatService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/stat")
@Validated
public class AdminStatController {
    private final Log logger = LogFactory.getLog(AdminStatController.class);

    @Autowired
    private StatService statService;

    @GetMapping("/user")
    public Object statUser() {
        List<Map> rows = statService.statUser();
        String[] columns = new String[]{"day", "users"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);
        return ResponseUtil.ok(statVo);
    }

    @GetMapping("/order")
    public Object statOrder() {
        List<Map> rows = statService.statOrder();
        String[] columns = new String[]{"day", "orders", "customers", "amount", "pcr"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);

        return ResponseUtil.ok(statVo);
    }

    @GetMapping("/goods")
    public Object statGoods() {
        List<Map> rows = statService.statGoods();
        String[] columns = new String[]{"day", "orders", "products", "amount"};
        StatVo statVo = new StatVo();
        statVo.setColumns(columns);
        statVo.setRows(rows);
        return ResponseUtil.ok(statVo);
    }

}
