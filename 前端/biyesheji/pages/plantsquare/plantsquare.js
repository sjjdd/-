var app=getApp()
var temp=[]
Page({
    data:{
        feelings:'',
        username:[],
        index:0,
        arr:[]
    },
    onLoad(){

    },
    onShow(){
     var that = this
        that.data.username=new Array()
     wx.request({
         url:app.globalData.localhttp+'feelings/selectAllFeel',
         method: 'get',//定义传到后台接受的是post方法还是get方法
         header: {
             'content-type': 'application/json' // 默认值
         },
         success(res){
             console.log("获得所有用户发布感想成功！")
             that.setData({
                 feelings:res.data,
             })
           for(var i=0;i<res.data.length;i++){
                 console.log("用户"+res.data[i].username+"的图片地址"+res.data[i].plantpics)
                 temp=res.data[i].plantpics.split(";")
                 console.log("以;分割之后的图片地址是"+temp[0]+" "+temp[1])
                 // for(var j=0;j<res.data[i].picnum;j++){
                 //     res.data[i].
                 // }
               if(res.data[i].picnum===1)
               res.data[i].plantpic1=temp[0]
               else if(res.data[i].picnum===2){
                     res.data[i].plantpic1=temp[0]
                     res.data[i].plantpic2=temp[1]
               }else if(res.data[i].picnum===3){
                   res.data[i].plantpic1=temp[0]
                   res.data[i].plantpic2=temp[1]
                   res.data[i].plantpic3=temp[2]
               }
               that.setData({
                   feelings:res.data
               })
           }

         },
         fail(res){
             console.log('获得所有用户发布感想失败！')
         }
     })
    },
    onPullDownRefresh(){
        var that=this
        wx.showNavigationBarLoading() //在标题栏中显示加载
//模拟加载
        setTimeout(function()
        {
            // complete
            wx.hideNavigationBarLoading() //完成停止加载
            wx.stopPullDownRefresh() //停止下拉刷新
            that.onShow()
        },1500);
    }
})