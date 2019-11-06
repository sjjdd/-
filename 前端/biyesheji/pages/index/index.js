//index.js
//获取应用实例
import WxCanvas from "../../ec-canvas/wx-canvas";

var _wxcharts=require('../../utils/wxcharts')
var util=require('../../utils/util')
//连接服务器的域名，注意格式！！！
const app = getApp()
//定义几个画图所需的参数数组
var category=[];//存储时间的数组
var soilhum=[];//存储土壤湿度数组
Page({
    data: {
        time:'',//当前时间
        client:null,//记录重连的次数
        reconnectCounts: 0,
        rooms:[],
        indicatorDots: true,
        username: "",
        autoplay: true,
        interval: 5000,
        duration: 1000,
        imgurls: [
            app.globalData.ngnixhttp+'t1.jpg',
            app.globalData.ngnixhttp+'t2.jpg',
            app.globalData.ngnixhttp+'t3.jpg'
        ],
        select: false,
        plant_name: '--请选择--',
        plants: [],
        canvasId: 'mychart-line',
        // ec: {
        //     onInit: initChart
        // },
        array:[],
        index:'',
        index2:'',
        beijingtime:'',
        roomtem:'温度采集中...',
        roomhum:'湿度采集中...',
        myTimer1:'',
        myTimer2:'',
        myTimer3:'',
        myTimer4:'',
        myTimer5:'',
        timer:'',
        msgList:[]
    },
    //监听页面加载
    onLoad(options) {
        var that=this
        this.lineShow1(['数据每隔开一分钟采集'],[0])
        //this.lineShow1()
       // this.requestTanH();
        //this.lineShow1()
        // this.lineShow2()
        // this.lineShow3()
    },
    onReady() {
        },
    //监听页面初次渲染完成
    onShow() {
        var that = this
        this.getCurrentTime()
        //测试定时发送请求的函数
        this.setData({
            myTimer1:
                setInterval( function() {
                    // var TIME2=util.formatTime(new Date())
                    // that.setData({
                    //     beijingtime:TIME2
                    // })
                    // console.log("当前时间"+that.data.time)
                    // console.log("显示北京时间请求1分钟触发一次");

                    that.getCurrentTime()
                }, 1000*60)    //代表1秒钟发送一次请求
        })



        this.getDeviceInfo()
        wx.request({
            url: app.globalData.localhttp + "user/getUidByName",
            //定义传到后台的数据.
            data: {
                username: app.globalData.Gusername,
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res) {
                console.log("通过用户名" + res.data[0].username + ":" + res.data[0].userid);
                app.globalData.userid = res.data[0].userid;
                // //每间隔五秒刷新一下温度和湿度
                // that.requestTanH();
                // that.setData({
                //     myTimer2:  setInterval(that.requestTanH,1000*60)
                // })

                wx.request({
                    url: app.globalData.localhttp + "plant/getPlantnameByuid",
                    //定义传到后台的数据.
                    data: {
                        userid: app.globalData.userid,
                    },
                    method: 'get',//定义传到后台接收的方法是post还是get
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res) {
                        console.log("根据用户id获得植物信息成功")
                        console.log(res.data)
                        that.setData({
                            plants: res.data
                        })

                    }, fail(res) {
                        console.log("没有得到信息");
                    }
                }),
                    wx.request({
                        url:app.globalData.localhttp+"room/findroomByuid",
                        data:{
                            userid: app.globalData.userid,
                        },
                        method: 'get',//定义传到后台接收的方法是post还是get
                        header: {
                            'content-type': 'application/json' // 默认值
                        },
                        success(res){
                            console.log("获得房间信息成功")
                            that.setData({
                                rooms:res.data
                            })
                        },
                        fail(res){
                            console.log("获得房间信息失败")
                        }
                    }),
                  //去获得植物的温度信息
                wx.request({
                    url:app.globalData.localhttp+'plant/noticeTem',
                    data:{
                        userid:app.globalData.userid
                    },
                    method: 'get',//定义传到后台接收的方法是post还是get
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res){
                        console.log('调用公告接口成功')
                        console.log('获得公告'+res.data[0].plantname)
                            that.setData({
                                msgList:res.data
                            })

                    },
                    fail(res){
                        console.log('调用公告接口失败')
                    }
                })
            }, fail(res) {
                console.log("没有得到信息");
            }
        })
        this.setData({
            myTimer5:setInterval(function () {
                //去获得植物的温度信息
                wx.request({
                    url:app.globalData.localhttp+'plant/noticeTem',
                    data:{
                        userid:app.globalData.userid
                    },
                    method: 'get',//定义传到后台接收的方法是post还是get
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res){
                        console.log('调用公告接口成功')
                        if(res.data[0].plantname!='')
                        console.log('获得公告'+res.data[0].plantname)
                            that.setData({
                                msgList:res.data
                            })

                    },
                    fail(res){
                        console.log('调用公告接口失败')
                    }
                })
            },60*1000)
        })

    },
    onUnload(){
     //别忘了清除定时器
     clearInterval(this.data.myTimer1)
     clearInterval(this.data.myTimer2)
        clearInterval(this.data.myTimer3)
        clearInterval(this.data.myTimer4)

    },
    bindShowMsg: function () {
        wx.showModal({
            title: '提示',
            content: '模态弹窗',
            success: function (res) {
                if (res.confirm) {
                    console.log('用户点击确定')
                }else{
                    console.log('用户点击取消')
                }

            }
        }),
        this.setData({
            select: !this.data.select,

        })

    },

    mySelect(e) {
        console.log(e)
        var name = e.currentTarget.dataset.name.plantname
        this.setData({
            plant_name: name,
            select: false,
        })

    },
    bindPickerChange(e){
        var that=this
        console.log('picker发送选择改变，携带值为',e.detail.value)
        this.setData({
            index:e.detail.value
        })
        //选择好植物名字之后则要根据植物名以及用户id去查找得到所绑定的设备id
        //然后则根据设备id得到土壤湿度值
        wx.request({
            url:app.globalData.localhttp+'plant/getdeviceid',
            data:{
                plantname:this.data.plants[e.detail.value],
                userid:app.globalData.userid
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
                console.log("打印出设备id"+res.data[0].deviceid);
                //先去订阅土壤湿度主题
                wx.request({
                    url:app.globalData.localhttp+'mqtt/SoilHum',
                    data:{
                        deviceid:res.data[0].deviceid
                    },
                    method: 'get',//定义传到后台接收的方法是post还是get
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res){
                        console.log('订阅土壤湿度值成功')
                    },fail(res){
                        console.log('订阅土壤湿度值失败')
                    }
                })
                wx.request({
                    url:app.globalData.localhttp+'plant/listsoilhum',
                    data:{
                       deviceid:res.data[0].deviceid
                    },
                    method: 'get',//定义传到后台接收的方法是post还是get
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res){
                        console.log("取第一个土壤湿度值"+res.data[0].soil);
                        soilhum=[];category=[];
                        for(var i=0;i<5;i++) {
                            soilhum.push(res.data[i].soil)
                            category.push(res.data[i].time)
                        }
                        that.lineShow1(category,soilhum)
                    }
                })
            },
            fail(res){
               console.log("访问点击选择植物接口失败");
            }
            })
        //再来一次定时请求
         this.setData({
             myTimer3:setInterval(function () {
                 soilhum=[],
                 category=[],//别忘了清空
                 wx.request({
                     url:app.globalData.localhttp+'plant/getdeviceid',
                     data:{
                         plantname:that.data.plants[e.detail.value],
                         userid:app.globalData.userid
                     },
                     method: 'get',//定义传到后台接收的方法是post还是get
                     header: {
                         'content-type': 'application/json' // 默认值
                     },
                     success(res){
                         console.log("打印出设备id"+res.data[0].deviceid);
                         wx.request({
                             url:app.globalData.localhttp+'plant/listsoilhum',
                             data:{
                                 deviceid:res.data[0].deviceid
                             },
                             method: 'get',//定义传到后台接收的方法是post还是get
                             header: {
                                 'content-type': 'application/json' // 默认值
                             },
                             success(res){
                                 console.log("取第一个土壤湿度值"+res.data[0].soil);
                                 soilhum=[];category=[];
                                 for(var i=0;i<5;i++) {
                                     soilhum.push(res.data[i].soil)
                                     category.push(res.data[i].time)
                                 }
                                 that.lineShow1(category,soilhum)
                             }
                         })
                     },
                     fail(res){
                         console.log("访问点击选择植物接口失败");
                     }
                 })
             },60*1000)
         })
    },
    bindPickerChange2(e){
        var that=this
        console.log('picker发送选择改变，携带值为',e.detail.value)
        this.setData({
            index2:e.detail.value
        })
        //根据用户id以及房间名字来查找对应房间的温湿度
        wx.request({
            url:app.globalData.localhttp+'room/getTHByUR',
            data:{
                userid:app.globalData.userid,
                roomname:that.data.rooms[e.detail.value]
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
               console.log("获得房间"+res.data[0].roomname+"的温度"+res.data[0].temperature+
               " 湿度"+res.data[0].humidity);
               that.setData({
                   roomtem:res.data[0].temperature,
                   roomhum:res.data[0].humidity
               })

            },
            fail(res){
                console.log("访问点击选择植物接口失败");
            }
        })
        ,
        this.setData({
            myTimer4:setInterval(function () {
                //根据用户id以及房间名字来查找对应房间的温湿度
                wx.request({
                    url:app.globalData.localhttp+'room/getTHByUR',
                    data:{
                        userid:app.globalData.userid,
                        roomname:that.data.rooms[e.detail.value]
                    },
                    method: 'get',//定义传到后台接收的方法是post还是get
                    header: {
                        'content-type': 'application/json' // 默认值
                    },
                    success(res){
                        console.log("获得房间"+res.data[0].roomname+"的温度"+res.data[0].temperature+
                            " 湿度"+res.data[0].humidity);
                        that.setData({
                            roomtem:res.data[0].temperature,
                            roomhum:res.data[0].humidity
                        })

                    },
                    fail(res){
                        console.log("访问点击选择植物接口失败");
                    }
                })
            },60*1000)
        })

    },
    //获取设备信息
    getDeviceInfo() {
        var that = this
        wx.getSystemInfo({
            success(res) {
               that.setData({
                   deviceW:res.windowWidth,
                   deviceH:res.windowHeight
               })
            }
        })
    },
    //折线图
    lineShow1(m,n) {
        // console.log('打印画图'+n)
        // console.log('打印温度'+m);
        var line = {
            canvasId: 'lineGraph1',
            type: 'line',
            categories: m,
            series: [{
                name:'植物土壤湿度值',
                data:n,
                format(val){
                    return val;
                }
            }],
            xAxis:{
                fontColor:'#000'
            },
            yAxis:{
                title:'土壤湿度值',
                fontColor:'#000',
                format(val){
                    return val;
                },
                min:0
            },
            width:320,
            height:200
        }
        new _wxcharts(line)
    },
    requestTanH(){
        var that=this
        var TIME2=util.formatTime(new Date())
        that.setData({
            beijingtime:TIME2
        })
        console.log("当前时间"+that.data.time)
        wx.request({
            url:app.globalData.localhttp+'room/getHandT',
            data:{
                userid:app.globalData.userid,
                roomid:190418101
            },
            method: 'get',//定义传到后台接收的方法是post还是get
            header: {
                'content-type': 'application/json' // 默认值
            },
            success(res){
                console.log("访问获取温湿度的接口成功")
                console.log("获得的温度是"+res.data.Temperature190418101+" "+"湿度是"
                    +res.data.Humidity190418101)
               that.setData({
                   roomtem:res.data.Temperature190418101,
                   roomhum:res.data.Humidity190418101
               })

            },
            fail(res){
                console.log("访问获取温湿度的接口失败")
            }
        })
    },
    getCurrentTime(){
        var that=this
        var TIME2=util.formatTime(new Date())
        that.setData({
            beijingtime:TIME2
        })
        console.log("当前时间"+that.data.time)
        console.log("显示北京时间请求1分钟触发一次");
    }
})