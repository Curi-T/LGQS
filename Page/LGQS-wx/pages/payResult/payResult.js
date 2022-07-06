var util = require('../../utils/util.js');
var api = require('../../config/api.js');

var app = getApp();
Page({
  data: {
    status: false,
    orderId: 0
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.orderId,
      status: options.status === '1' ? true : false
    })
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  payOrder() {
    let that = this;
    util.request(api.OrderPrepay, {
      orderId: that.data.orderId
    }, 'POST').then(function(res) {
      if (res.errno === 0) {
        const payParam = res.data;
        console.log("支付过程开始22222")
        //////////////////////////////////////////////////////////////
        that.setData({
          status: true
        });
        /////////////////////////////////////////////////////////////
      }
    });
  }
})