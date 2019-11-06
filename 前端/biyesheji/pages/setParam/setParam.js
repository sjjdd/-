var app = getApp()
var plantpicsrc = ""
Page({
    data: {
        filename: '',
        tip: '',
        buttonDisabled: false,
        modalHidden: true,
        show: false,
        images: [],
        photos: "",
        plantname: "",
        plantpic: "",
        waterparam: '',temperature: '', userid: '', plantdes: '',deviceid:'',roomid:'',
        modalHidden:true

    },
    onLoad(options) {
        this.setData({
            userid: app.globalData.userid
        })
        // $init(this)
    },
    getDeviceid(e){
        var Idid=e.detail.value;
        if(Idid !=''){
            this.setData({deviceid:Idid})
        }
    },
    getDataname(e) {
        var Iplantname = e.detail.value;
        if (Iplantname != '') {
            this.setData({plantname: Iplantname})
        }
    },
    getDataBindTap(e) {
        var IBindTap = e.detail.value;
        if (IBindTap != '') {
            this.setData({waterparam: IBindTap})
        }
    },
    getRoomid(e) {
        var IRoomid = e.detail.value;
        if (IRoomid != '') {
            this.setData({roomid: IRoomid})
        }
    },
    getDatatemp(e) {
        var Itemp = e.detail.value;
        if (Itemp != '') {
            this.setData({temperature: Itemp})
        }
    },
    getDatades(e) {
        var Ides = e.detail.value;
        if (Ides != '') {
            this.setData({plantdes: Ides})
        }
    },

    chooseImg() {
        var that = this
        wx.chooseImage({
            count: 1,//默认9
            sizeType: ['origin', 'compressed'],// 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'],// 可以指定来源是相册还是相机，默认二者都有
            success(res) {
                // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
                var tempFilePaths = res.tempFilePaths
                that.setData({
                    photos: tempFilePaths
                })
                console.log(("上传的图片临时地址是"+that.data.photos))
            }
        })
    },
    uploadImg() {
        var that = this
        // setTimeout(function () {
        wx.uploadFile({
            url: app.globalData.localhttp + "user/upload",
            filePath: that.data.photos[0],
            name: 'file',
            fromData: {
                'user': 'sjj'
            },
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res) {
                plantpicsrc = app.globalData.ngnixhttp + res.data
                //图片上传正确的话
                if (res.data != "error") {
                    that.setData({
                        plantpic: app.globalData.ngnixhttp + res.data,
                        photos:app.globalData.ngnixhttp + res.data
                    })
                }
                wx.showToast({
                    title: "图片已上传"
                })
                console.log("打印出要保存的图片地址" + app.globalData.ngnixhttp + res.data);
            }, fail(res) {
                wx.showToast({
                    title: "图片上传失败"
                })
                console.log("上传图片失败")
            }
        })
        // },3000)
    },
    submitdata() {
        this.setData({
            modalHidden:!this.data.modalHidden
        })

    },
    //确定按钮点击事件
    modalBindconfirm(){
        if(this.data.plantname===''||this.data.waterparam===''||this.data.temperature===''
            ||this.data.plantdes===''||this.data.deviceid===''){
            wx.showToast({
                title:"主人请填写完全部数据在上传"
            })
        }else if(this.data.plantpic===''){
            wx.showToast({
                title:"主人请先上传图片再说"
            })
        }else{
            wx.request({
                url: app.globalData.localhttp + 'mqtt/publishAll',
                //定义传到后台的数据
                data: {
                    //从全局变量data中获取数据
                    deviceid:this.data.deviceid,
                    waterparam:this.data.waterparam,
                    temperature:this.data.temperature
                },
                method: 'post',//定义传到后台接受的是post方法还是get方法
                header: {
                    "Content-Type": "application/x-www-form-urlencoded" // 默认值
                },
                success(res) {
                    console.log("发布植物浇水和温度成功！")
                }, fail(res) {
                    console.log("发布植物浇水和温度失败！")
                }
            }),
                this.setData({
                    modalHidden:!this.data.modalHidden
                }),
                wx.request({
                    url: app.globalData.localhttp + 'plant/setPlant',
                    //定义传到后台的数据
                    data: {
                        //从全局变量data中获取数据
                        plantname: this.data.plantname,
                        plantpic: plantpicsrc,
                        waterparam: this.data.waterparam,
                        roomid: this.data.roomid,
                        temperature: this.data.temperature,
                        userid: this.data.userid,
                        plantdes: this.data.plantdes,
                        deviceid:this.data.deviceid
                    },
                    method: 'post',//定义传到后台接收的方法是post还是get
                    header: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    success(res) {
                        if(res.data.setPlantParam=="yes"){
                            wx.showToast(
                                {
                                    title:"设置成功！"
                                }
                            )
                        }else if(res.data.setPlantParam=="null"){
                            wx.showToast(
                                {
                                    title:"图片上传失败！"
                                }
                            )
                        }else{
                            wx.showToast(
                                {
                                    title:"设置失败！"
                                }
                            )
                        }
                        console.log("设置植物信息成功！")
                    }, fail(res) {
                        console.log("设置植物信息失败！")
                    }
                })
        }

    },
    //取消按钮点击事件
    modalBindcancel(){
        this.setData({
            modalHidden:!this.data.modalHidden

        }),
            console.log("主人取消了对植物的设置")
    }
})