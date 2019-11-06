var app = getApp()
Page({
    data:{
        articles:'',
    },
    onShow(){
        var that = this
        wx.request({
            url: app.globalData.localhttp + 'article/getAllArticle',
            //定义传到后台的数据
            data: {
               //没有数据要传送
            },
            method: 'get',//定义传到后台接受的是post方法还是get方法
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res) {
                console.log("获得关于植物知识推送文章成功！")
                console.log(res.data)
                that.setData({
                    articles: res.data
                })
            }, fail(res) {
                console.log("获得关于植物知识推送文章失败！")
            }
        })
    },
    gotarticleinfo(e){
        var index = parseInt(e.currentTarget.dataset.index);
        console.log("您点击了"+this.data.articles[index].articlecontent)
        app.globalData.showarticle=this.data.articles[index].articlecontent
        wx.navigateTo({
            url:"/pages/showarticlePage/showarticlePage"
        })
    }
})