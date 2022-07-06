package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.admin.annotation.RequiresPermissionsDesc;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.db.domain.LgqsSearchHistory;
import cn.cqut.lgqs.db.service.LgqsSearchHistoryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/history")
public class AdminHistoryController {
    private final Log logger = LogFactory.getLog(AdminHistoryController.class);

    @Autowired
    private LgqsSearchHistoryService searchHistoryService;

    @RequiresPermissions("admin:history:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "搜索历史"}, button = "查询")
    @GetMapping("/list")
    public Object list(String userId, String keyword,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LgqsSearchHistory> historyList = searchHistoryService.querySelective(userId, keyword, page, limit,
                sort, order);
        return ResponseUtil.okList(historyList);
    }
}
