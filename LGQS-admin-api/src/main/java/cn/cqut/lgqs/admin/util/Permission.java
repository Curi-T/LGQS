package cn.cqut.lgqs.admin.util;

import cn.cqut.lgqs.admin.annotation.RequiresPermissionsDesc;
import org.apache.shiro.authz.annotation.RequiresPermissions;

/**
 * @author CuriT
 */
public class Permission {
    private RequiresPermissions requiresPermissions;
    private RequiresPermissionsDesc requiresPermissionsDesc;
    private String api;

    public RequiresPermissions getRequiresPermissions() {
        return requiresPermissions;
    }

    public RequiresPermissionsDesc getRequiresPermissionsDesc() {
        return requiresPermissionsDesc;
    }

    public void setRequiresPermissions(RequiresPermissions requiresPermissions) {
        this.requiresPermissions = requiresPermissions;
    }

    public void setRequiresPermissionsDesc(RequiresPermissionsDesc requiresPermissionsDesc) {
        this.requiresPermissionsDesc = requiresPermissionsDesc;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
