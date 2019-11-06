var app = getApp()
Page({
    data: {
        plants: '',
        userid: '',
        delBtnWidth:180,//删除按钮宽度,
        modalHidden: true,
        ind:''
    },
    onLoad() {

        var that = this
        wx.request({
            url: app.globalData.localhttp + 'plant/getPlantByuid',
            //定义传到后台的数据
            data: {
                //从全局变量data中获取数据
                userid: app.globalData.userid
            },
            method: 'get',//定义传到后台接受的是post方法还是get方法
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res) {
                console.log("调用根据用户找植物信息接口成功！")
                console.log(res.data)
                that.setData({
                    plants: res.data
                })
            }, fail(res) {
                console.log("调用根据用户找植物信息接口失败！")
            }
        })
    },
    gotMyplant(e){
        var index=parseInt(e.currentTarget.dataset.index);
        console.log("您点击了"+this.data.plants[index].plantname);
        app.globalData.configmyplantid=this.data.plants[index].plantid;
        wx.navigateTo({
            url:"/pages/configPlantInfo/configPlantInfo"
        })
    },
    deleteplant(e){
        this.setData({
            modalHidden:!this.data.modalHidden,
            ind:e.currentTarget.dataset.index
        })
        // var that=this
        // var index=parseInt(e.currentTarget.dataset.index);
        // console.log("您点击了删除"+this.data.plants[index].plantname);
        // wx.request({
        //     url: app.globalData.localhttp + 'plant/deleteplant',
        //     //定义传到后台的数据
        //     data: {
        //         //从全局变量data中获取数据
        //         plantid: this.data.plants[index].plantid
        //     },
        //     method: 'post',//定义传到后台接受的是post方法还是get方法
        //     header: {
        //         "Content-Type": "application/x-www-form-urlencoded"
        //     },
        //     success(res) {
        //         console.log("调用删除植物信息接口成功！")
        //         console.log(res.data)
        //         that.onLoad();
        //     }, fail(res) {
        //         console.log("调用删除植物信息接口失败！")
        //     }
        // })
    },
    modalBindconfirm(){
        var that=this
        var index=parseInt(this.data.ind);
        console.log("您点击了删除"+this.data.plants[this.data.ind].plantname);
        wx.request({
            url: app.globalData.localhttp + 'plant/deleteplant',
            //定义传到后台的数据
            data: {
                //从全局变量data中获取数据
                plantid: this.data.plants[this.data.ind].plantid
            },
            method: 'post',//定义传到后台接受的是post方法还是get方法
            header: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            success(res) {
                console.log("调用删除植物信息接口成功！")
                console.log(res.data)
                that.onLoad();
            }, fail(res) {
                console.log("调用删除植物信息接口失败！")
            }
        })
        this.setData({
            modalHidden:!this.data.modalHidden

        })
    },
    modalBindcancel(){
        this.setData({
            modalHidden:!this.data.modalHidden

        }),
            console.log("主人取消了对该项的删除")
    }
})