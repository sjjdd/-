var app = getApp();
var ceshi=require('../../utils/ceshi');
Page({
    data: {
        username: "",
        userpwd: "",
        message: ""
    },
    onLoad() {
    },
    accountInput(e) {
        var Iusername = e.detail.value;//从页面获取到用户输入的用户名
        if (Iusername != "") {
            this.setData({
                username: Iusername//把获取到的密码赋值给全局变量Data中去
            });
        }
    },
    //处理pwdblur事件
    pwdBlur(e) {
        var pwd = e.detail.value;
        if (pwd != "") {
            this.setData({
                userpwd: pwd
            })

        }
    },
    login(e) {
        //用户名或者密码不能为空
        if(this.data.username==''||this.data.password==''){
            wx.showModal({
                title: '提示',
                content: '用户名或者密码不能为空',
                showCancel: false
            })
        }else{
            wx.request({
                url: app.globalData.localhttp + "user/login",
                //定义传到后台的数据.
                data: {
                    username: this.data.username,
                    userpwd: this.data.userpwd
                },
                method: 'post',//定义传到后台接收的方法是post还是get
                header: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                success(res) {
                    app.globalData.Gusername = res.data.username,//登录成功则同时更新全局变量中的用户名为登录名
                         console.log("调用登录接口成功");
                    console.log(res.data);
                    console.log(res.data.message);
                    if (res.data.message == "ok") {
                        wx.showToast({
                            title: "登录成功"
                        })
                        setTimeout(function () {
                            wx.switchTab({//我不知道为什么用navigateTo方法不行用这个就可以
                                url: "/pages/index/index"
                            })
                        }, 2000)

                    } else {
                        wx.showModal({
                            title: '提示',
                            content: '用户名或者密码错误',
                            showCancel: false
                        })
                    }
                },
                fail(res) {
                    console.log('调用API失败')
                    wx.showToast({
                        title:'没有网络'
                    })
                }
            })
        }

    },
    register(e) {
        wx.navigateTo({
            url: "/pages/Register/Register"
        })
    }
})