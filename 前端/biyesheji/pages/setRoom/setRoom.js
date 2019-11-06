var app=getApp()
Page({
data:{
    modalHidden:true,
    roomid:'',
    roomname:'',
    userid:''
},
    getRoomid(e){
        var IRoomid = e.detail.value;
        if (IRoomid != '') {
            this.setData({roomid:IRoomid})
        }
    },
    getRoomname(e){
    var IRoomname=e.detail.value;
    if(IRoomname!=''){
       this.setData({roomname:IRoomname})
    }
    },
    submitdata() {
        this.setData({
            modalHidden:!this.data.modalHidden
        })

    },
    //确定按钮点击事件
    modalBindconfirm(){
        if(this.data.roomid==''||this.data.roomname==''){
            wx.showToast({
                title:"主人请填写完全部数据在上传"
            })
        }else{
            wx.request({
                url: app.globalData.localhttp + 'room/setRoom',
                //定义传到后台的数据
                data: {
                    //从全局变量data中获取数据
                    roomid:this.data.roomid,
                    roomname:this.data.roomname,
                    userid:app.globalData.userid
                },
                method: 'post',//定义传到后台接受的是post方法还是get方法
                header: {
                    "Content-Type": "application/x-www-form-urlencoded" // 默认值
                },
                success(res) {
                    if(res.data[0].roomname==="success"){
                        wx.showToast(
                            {
                                title:"设置成功"
                            }
                        )
                    }else{
                        wx.showToast({
                            title:'设置失败'
                        })
                    }
                    console.log("发布房间消息成功！")
                }, fail(res) {
                    console.log("发布房间消息失败！")
                }
            }),
                this.setData({
                    modalHidden:!this.data.modalHidden
                })
        }

    },
    //取消按钮点击事件
    modalBindcancel(){
        this.setData({
            modalHidden:!this.data.modalHidden

        }),
            console.log("主人取消了对房间的设置")
    }
})