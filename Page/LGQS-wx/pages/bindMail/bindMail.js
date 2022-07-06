var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var user = require('../../../utils/user.js');
var app = getApp();

Page({
  bindMail: function(e) {
    
    // if (e.detail.errcode !== 0) {
    //   // 拒绝授权
    //   return;
    // }
  
    
    if (!this.data.hasLogin) {
      wx.showToast({
        title: '绑定失败：请先登录',
        icon: 'none',
        duration: 2000
      });
      return;
    }

  }
})