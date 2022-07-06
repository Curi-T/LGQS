package cn.cqut.lgqs.wx.service;

import cn.cqut.lgqs.db.domain.LgqsUser;
import cn.cqut.lgqs.db.service.LgqsUserService;
import cn.cqut.lgqs.wx.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserInfoService {
    @Autowired
    private LgqsUserService userService;


    public UserInfo getInfo(Integer userId) {
        LgqsUser user = userService.findById(userId);
        System.out.println("2222"+userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
