var app=getApp()
Page({
    data:{
      articleurl:''
    },
    onShow(){
        this.setData({
            articleurl:app.globalData.showarticle
        })
    }
})