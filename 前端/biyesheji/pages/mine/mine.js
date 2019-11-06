var app = getApp()
Page({
    data: {
        canIUse:wx.canIUse('button.open-type.getUserInfo'),//判断小程序获取用户信息在当前版本是否可用
        userInfo: null,
        hasUserInfo:false
    },
    onLoad: function () {
        var that=this
        this.getUserInfoData()
    },
    setParam() {
        wx.navigateTo({
            url: "/pages/setParam/setParam"
        })
    },
    plantKnowledge() {
        wx.navigateTo({
            url: "/pages/plantKnowledge/plantKnowledge"
        })
    },
    Myplant() {
        wx.navigateTo({
            url: "/pages/Myplant/Myplant"
        })
    },
    setroomParam(){
        wx.navigateTo({
            url: "/pages/setRoom/setRoom"
        })
    },
    publishidea(){
      wx.navigateTo({
          url: "/pages/publishfeelings/publishfeelings"
      })
    },
    plantsuqare(){
        wx.navigateTo({
            url: "/pages/plantsquare/plantsquare"
        })
    },
    Logout() {
        wx.redirectTo({
            url: "/pages/LinReg/LinReg"
        })
    },
    getUserInfoData(){
        if(app.globalData.userInfo){
            this.setData({
                userInfo:app.globalData.userInfo
            })
        }else if(this.data.canIUse){
            app.userInfoReadyCallback=res=>{
            this.setData({
                userInfo:res.userInfo
            })
            }
        }else{//没有one-type=getUserInfo版本的兼容处理
            wx.getUserInfo({
                success:res=>{
                    app.globalData.userInfo=res.userInfo
                    this.setData({
                        userInfo:res.userInfo
                    })
                }
            })
        }

    },
    bindGetUserInfo(e){//点击按钮弹出授权框用户点击确认后才会获取到用户信息
        console.log(e)
        app.globalData.userInfo=e.detail.userInfo
        this.setData({
            userInfo:e.detail.userInfo
        })

    }
})