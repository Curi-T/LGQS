package cn.cqut.lgqs.admin.web;

import cn.cqut.lgqs.admin.annotation.RequiresPermissionsDesc;
import cn.cqut.lgqs.core.util.ResponseUtil;
import cn.cqut.lgqs.core.validator.Order;
import cn.cqut.lgqs.core.validator.Sort;
import cn.cqut.lgqs.db.domain.LgqsFeedback;
import cn.cqut.lgqs.db.service.LgqsFeedbackService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yogeek
 * @date 2018/8/26 1:11
 */
@RestController
@RequestMapping("/admin/feedback")
@Validated
public class AdminFeedbackController {
    private final Log logger = LogFactory.getLog(AdminFeedbackController.class);

    @Autowired
    private LgqsFeedbackService feedbackService;

    @RequiresPermissions("admin:feedback:list")
    @RequiresPermissionsDesc(menu = {"用户管理", "意见反馈"}, button = "查询")
    @GetMapping("/list")
    public Object list(Integer userId, String username,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LgqsFeedback> feedbackList = feedbackService.querySelective(userId, username, page, limit, sort,
                order);
        return ResponseUtil.okList(feedbackList);
    }
}
