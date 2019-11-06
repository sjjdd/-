// pages/register/register.js
var app = getApp();
Page({

    /**
     * 页面的初始数据
     * data为全局变量
     */
    data: {
        username: "",
        userpwd: "",
        Register: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {

    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady: function () {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide: function () {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload: function () {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage: function () {

    },

    //处理accountInput的触发事件
    accountInput: function (e) {
        var Iusername = e.detail.value;//从页面获取到用户输入的用户名/邮箱/手机号
        if (Iusername != '') {
            this.setData({username: Iusername});//把获取到的密码赋值给date中的password
        }
    },
    //处理pwdBlur的触发事件
    pwdBlur: function (e) {
        var pwd = e.detail.value;//从页面获取到用户输入的密码
        if (pwd != '') {
            this.setData({userpwd: pwd});//把获取到的密码赋值给date中的password
        }
    },
    //处理register的触发事件
    register: function (e) {
        if(this.data.username==''||this.data.password==''){
            wx.showModal({
                title: '提示',
                content: '用户名或者密码不能为空',
                showCancel: false
            })
        }else{
            wx.request({
                url: app.globalData.localhttp + 'user/register',
                //定义传到后台的数据
                data: {
                    //从全局变量data中获取数据
                    username: this.data.username,
                    userpwd: this.data.userpwd
                },
                method: 'post',//定义传到后台接收的方法是post还是get
                header: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                success: function (res) {
                    console.log("调用注册API成功！")
                    console.log(res.data.Register)
                    if (res.data.Register == "ok") {
                        wx.showToast({
                            title: "注册成功!"
                        })
                        setTimeout(function () {
                            wx.redirectTo({
                                url: "/pages/LinReg/LinReg"
                            })
                        }, 2000)
                    } else {
                        wx.showModal({
                            title: '提示',
                            content: '注册失败了',
                            showCancel: false
                        })
                    }

                },
                fail: function (res) {
                    console.log("调用注册API失败！");
                }
            })
        }

    }
})
