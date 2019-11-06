var app=getApp()
var plantpicsrc = ""
var plantpictem=new Array()
Page({
data:{
    content:'',
    images:'',
    urls:'',
    current:''
},
//获取文本输入框内的信息
getTextAreaContent(event){
    // this.data.content=event.detail.value;
    this.setData({
        content:event.detail.value
    })
    console.log("取得用户刚刚输入的信息"+event.detail.value)
},
//选择图片信息
    chooseImage(e) {
        var that = this;
        var  pics=[];
        wx.chooseImage({
            count: 3,
            sizeType: ['origin', 'compressed'],// 可以指定是原图还是压缩图，默认二者都有
            sourceType: ['album', 'camera'],// 可以指定来源是相册还是相机，默认二者都有
            success(res) {
                // tempFilePath可以作为img标签的src属性显示图片
                const tempFilePaths = res.tempFilePaths
                // that.data.images = that.data.images.concat(tempFilePaths)
                console.log('图片临时地址是'+tempFilePaths)
                for(var i=0;i<tempFilePaths.length;i++){
                    pics.push(tempFilePaths[i]);
                }
                that.setData({
                    images:pics
                })
            },
        })
    },
    // 预览图片
    previewImg: function(e) {
        //获取当前图片的下标
        var index = e.currentTarget.dataset.index;

        wx.previewImage({
            //当前显示图片
            current: this.data.images[index],
            //所有图片
            urls: this.data.images
        })
    },
    /**
     * 删除图片
     */
    removeImg: function(event) {
        var position = event.currentTarget.dataset.index;
        this.data.images.splice(position, 1);
        // 渲染图片
        this.setData({
            images: this.data.images,
        })
    },
    //发布云端
    uploadImage(){
     //先上传图片
        if(this.data.content==='')
        {
            wx.showModal({
                title: '提示',
                content: '请用户填写发表内容',
                showCancel: false
            })
        }
       else if(this.data.images===''){
            wx.showModal({
                title: '提示',
                content: '请用户先选择图片',
                showCancel: false
            })
        }
        else{
            var that=this
            plantpictem=new Array()
            for(var i=0;i<this.data.images.length;i++){
                wx.uploadFile({
                    url: app.globalData.localhttp + "user/uploadfeelings",
                    filePath: that.data.images[i],
                    name: 'file',
                    fromData: {
                        'user': 'sjj'
                    },
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res) {

                        plantpicsrc = app.globalData.feelinghttp + res.data
                        plantpictem.push(plantpicsrc)
                        console.log("打印出要保存的图片地址" + app.globalData.feelinghttp + res.data);
                        wx.showToast({
                            title:'上传图片成功！'
                        })
                    }, fail(res) {
                        // wx.showToast({
                        //     title: "图片上传失败"
                        // })
                        console.log("上传图片失败")
                        wx.showToast({
                            title:"上传图片失败！"
                        })
                    }
                })
            }
        }


    },
    saveToHistoryServer(){
        var str=plantpictem.join(";")//得到的是图片地址以;拼接的
        console.log(str)
        if(this.data.content===''||str===''){
            wx.showToast({
                title: "主人请先填写完整数据"
            })
        }else{
            wx.request({
                url: app.globalData.localhttp + "feelings/publishfeelings",
                //定义传到后台的数据.
                data: {
                 userid:app.globalData.userid,
                 content:this.data.content,
                 plantpics:str,
                 picnum:this.data.images.length
                },
                method: 'post',//定义传到后台接收的方法是post还是get
                header: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                success(res) {
                    console.log("调用发布心得接口成功")
                    wx.showModal({
                            title: '发布',
                            content: '发布心得成功',
                            showCancel: false,
                        })

                },
                fail(res) {
                    console.log('调用API失败')
                }
            })
        }
    }
})