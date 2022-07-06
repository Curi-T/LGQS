package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.admin.annotation.RequiresPermissionsDesc;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.db.domain.LgqsIssue;
import cn.cqut.lgqs.db.service.LgqsIssueService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/admin/issue")
@Validated
public class AdminIssueController {
    private final Log logger = LogFactory.getLog(AdminIssueController.class);

    @Autowired
    private LgqsIssueService issueService;

    @RequiresPermissions("admin:issue:list")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "通用问题"}, button = "查询")
    @GetMapping("/list")
    public Object list(String question,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LgqsIssue> issueList = issueService.querySelective(question, page, limit, sort, order);
        return ResponseUtil.okList(issueList);
    }

    private Object validate(LgqsIssue issue) {
        String question = issue.getQuestion();
        if (StringUtils.isEmpty(question)) {
            return ResponseUtil.badArgument();
        }
        String answer = issue.getAnswer();
        if (StringUtils.isEmpty(answer)) {
            return ResponseUtil.badArgument();
        }
        return null;
    }

    @RequiresPermissions("admin:issue:create")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "通用问题"}, button = "添加")
    @PostMapping("/create")
    public Object create(@RequestBody LgqsIssue issue) {
        Object error = validate(issue);
        if (error != null) {
            return error;
        }
        issueService.add(issue);
        return ResponseUtil.ok(issue);
    }

    @RequiresPermissions("admin:issue:read")
    @GetMapping("/read")
    public Object read(@NotNull Integer id) {
        LgqsIssue issue = issueService.findById(id);
        return ResponseUtil.ok(issue);
    }

    @RequiresPermissions("admin:issue:update")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "通用问题"}, button = "编辑")
    @PostMapping("/update")
    public Object update(@RequestBody LgqsIssue issue) {
        Object error = validate(issue);
        if (error != null) {
            return error;
        }
        if (issueService.updateById(issue) == 0) {
            return ResponseUtil.updatedDataFailed();
        }

        return ResponseUtil.ok(issue);
    }

    @RequiresPermissions("admin:issue:delete")
    @RequiresPermissionsDesc(menu = {"餐厅管理", "通用问题"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody LgqsIssue issue) {
        Integer id = issue.getId();
        if (id == null) {
            return ResponseUtil.badArgument();
        }
        issueService.deleteById(id);
        return ResponseUtil.ok();
    }

}
