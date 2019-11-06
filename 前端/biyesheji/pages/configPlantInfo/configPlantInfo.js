var app = getApp();



Page({
data:{
    plantid:'',
    plants:'',
    edit:'',
    hidden:true,
    waterparam:'',
    lightparam:'',
    water:'开始浇水',
    temperature:"",
    plantdes:"",
    soil:0,
    deviceid:''
},
onLoad(){

    var that=this
    this.setData({
        plantid:app.globalData.configmyplantid,
        edit:app.globalData.ngnixhttp+"edit.png"
    }),
        wx.request({
            url: app.globalData.localhttp + "plant/getplantInfoById",
            //定义传到后台的数据.
            data: {
                plantid: app.globalData.configmyplantid,
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res) {
                console.log("根据植物id获得植物信息成功")
                console.log(res.data)
                that.setData({
                    plants: res.data,
                    deviceid:res.data[0].deviceid
                })
                console.log("该植物对应的deviceid"+res.data[0].deviceid)
            }, fail(res) {
                console.log("没有得到信息");
            }
        })
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
    editbaseplant() {
        app.globalData.editplantid = this.data.plantid
        this.setData({
            hidden:!this.data.hidden
        })
    },
    getwp(e){
        var Iwp = e.detail.value;
        if (Iwp != '') {
            this.setData({waterparam: Iwp})
        }

    },
    // getgz(e){
    //   var Igz=e.detail.value;
    //   if(Igz!=''){
    //       this.setData({
    //           lightparam:Igz
    //       })
    //   }
    // },
    getwd(e){
        var Iwd=e.detail.value;
        if(Iwd!=''){
            this.setData({
                temperature:Iwd
            })
        }
    },
    getms(e){
        var Ims=e.detail.value;
        if(Ims!=''){
            this.setData({
                plantdes:Ims
            })
        }
    },
    water(){

        var that=this
        if(this.data.soil==1){
            this.setData({
                soil:0,
                water:'开始浇水'
            })
        }else{
            this.setData({
                soil:1,
                water:'停止浇水'
            })
        }
        wx.request({
            url:app.globalData.localhttp+'mqtt/soil',
            data:{
                soil:this.data.soil,
                deviceid:this.data.deviceid
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
                if(that.data.soil==1)
                    console.log("正在浇水")
                else
                    console.log("停止浇水")
            },
            fail(res){
              console.log('调用浇水接口失败')
            }
        })
    },
    updatewp(){
        var that=this
        wx.request({
            url:app.globalData.localhttp+'plant/setplantWP',
            data:{
                plantid:app.globalData.editplantid,
                waterparam:this.data.waterparam
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
                console.log("更新植物的浇水阈值成功！")
                wx.showToast({
                   title:'修改成功！'
                })
                that.onLoad()
            },
            fail(res){
                console.log("更新植物的浇水阈值失败！")
            }
        }),
            wx.request({
                url:app.globalData.localhttp+'mqtt/publishsoilParam',
                data:{
                    deviceid:this.data.deviceid,
                    waterparam:this.data.waterparam
                },
                method: 'get',//定义传到后台接收的方法是post还是get
                header: {
                    'content-type': 'application/json' // 默认值
                },
                success(res){
                    console.log("发布植物的浇水阈值成功！")
                    that.onLoad()
                },
                fail(res){
                    console.log("发布植物的浇水阈值失败！")
                }
            })
    },
    // updategz(){
    //     var that=this
    //     wx.request({
    //         url: app.globalData.localhttp + 'mqtt/lightParam',
    //         //定义传到后台的数据
    //         data: {
    //             //从全局变量data中获取数据
    //             lightparam: this.data.lightparam,
    //             deviceid:this.data.deviceid
    //         },
    //         method: 'get',//定义传到后台接受的是post方法还是get方法
    //         header: {
    //             'content-type': 'application/json' // 默认值
    //         },
    //         success(res) {
    //             console.log("发布植物光照成功！")
    //         }, fail(res) {
    //             console.log("发布植物光照失败！")
    //         }
    //     }),
    //     wx.request({
    //         url:app.globalData.localhttp+'plant/setplantGZ',
    //         data:{
    //             plantid:app.globalData.editplantid,
    //             lightparam:this.data.lightparam
    //         },
    //         method: 'get',//定义传到后台接收的方法是post还是get
    //         header: {
    //             'content-type': 'application/json' // 默认值
    //         },
    //         success(res){
    //             console.log("更新植物的光照阈值成功！")
    //             that.onLoad()
    //         },
    //         fail(res){
    //             console.log("更新植物的光照阈值失败！")
    //         }
    //     })
    // },
    updatewd(){
        var that=this
        wx.request({
            url:app.globalData.localhttp+'plant/setplantWD',
            data:{
                plantid:app.globalData.editplantid,
                temperature:this.data.temperature
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
                console.log("更新植物的温度阈值成功！")
                wx.showToast({
                    title:'修改成功！'
                })
                that.onLoad()
            },
            fail(res){
                console.log("更新植物的温度阈值失败！")
            }
        })
    },
    updatems(){
        var that=this
        wx.request({
            url:app.globalData.localhttp+'plant/setplantMS',
            data:{
                plantid:app.globalData.editplantid,
                plantdes:this.data.plantdes
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
                console.log("更新植物的描述成功！")
                wx.showToast({
                    title:'修改成功！'
                })
                that.onLoad()
            },
            fail(res){
                console.log("更新植物的描述失败！")
            }
        })
    }
})