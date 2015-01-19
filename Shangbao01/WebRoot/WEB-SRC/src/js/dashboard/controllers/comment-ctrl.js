/**
 * Created by QK on 2014/12/4.
 */

angular.module("Dashboard").controller("commentCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.commentData);
    };

    $scope.addComment=function()
    {
        var testCommentData={
            commendId:"2",
            userName:"",
            userId:77,
            timeDate:new Date(),
            level:77,
            state:"published",
            from:"home",
            content:"hello this is a test comment",
            reply:"I get it"
        };
        var url=$scope.projectName+'/commend/1/'+'7/'+'news';
        console.log(url);
        var jsonString=JSON.stringify(testCommentData);
        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
        });
    };

    $scope.commentData=null;
    $scope.orderCondition="";

    //初始化页面，获取爬虫第一页的数据,返回的是一个titleList-------------------------------------------------------------------
    $scope.getCommentData=function(pageID)
    {
        var url=$scope.projectName+'/commend/'+pageID.toString()+$scope.orderCondition;
        console.log(url);
        $http.get(url).success(function(data){
            $scope.commentData=data;
            $scope.pageNums=getPageNums($scope.commentData.pageCount);
            console.log("成功获取数据");
        });
    };
    $scope.getCommentData(1);//会在生成页面的时候直接运行!

    $scope.refreshComment=function()
    {
        $scope.orderCondition="";
        $scope.getCommentData(1);
    };

    //检查表的内容 数据若是NULL则显示"无",状态转化成中文
    $scope.checkIfNull=function(str)
    {
        var checkedStr;
        if(str==null){
            checkedStr="无";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
    $scope.checkAndTranslate=function(str)
    {
        var checkedStr;
        if(str==null){
            checkedStr="无";
        }else if(str=="Temp"){
            checkedStr="草稿";
        }else if(str=="Deleted"){
            checkedStr="已删除";
        }else if(str=="Pending"){
            checkedStr="待审";
        }else if(str=="Crawler"){
            checkedStr="爬虫";
        }else if(str=="Published"){
            checkedStr="已发布";
        }else if(str=="Revocation"){
            checkedStr="已撤销"
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };

    //文章评论跳转------------------------------------------------------------------------------------------------------------

    $scope.goCommentDetails=function()
    {
        document.getElementById("comment").className="tab-pane";
        document.getElementById("commentDetails").className="tab-pane active";
    };

    //将文章评论数据传输给全局变量commentDetailData
    $scope.transDataToCommentDetailData=function(data)
    {
        for(p in $scope.commentDetailData){
            if(p=="commendList"){
                for(i in data[p]){
                    $scope.commentDetailData[p][i]=data[p][i];
                }
            }else{
                $scope.commentDetailData[p]=data[p];
            }
        }
    };

    //点击显示文章评论明细
    $scope.showComments=function(articleId,title,type)
    {
        commentDetailsUrl=$scope.projectName+'/commend/1/'+articleId+'/'+type;
        $scope.goCommentDetails();
        $scope.getCommentDetailData(1);
        $scope.getCommentDetailTitle(title,type);
    };

    //页面跳转----------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCommentData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.commentData.currentNo);
    };

    //得到页码数组的函数
    function getPageNums(pageCount)
    {
        if(pageCount==1||pageCount<1){
            return [1];
        }else{
            var arr=[];
            for(i=0;i<pageCount;i++){
                arr.push(i+1);
            }
            return arr;
        }
    }

    //排序---------------------------------------------------------------------------------------------------------------
    var orderState="none";
    $scope.orderByState=function(){
        if(orderState=="none"){
            $scope.orderCondition="/state";
            orderState="state";
            $scope.getCommentData(1);
        }else if(orderState=="state"){
            $scope.orderCondition="";
            orderState="none"
            $scope.getCommentData(1);
        }
    };
    var orderNewsCommends="none";
    $scope.orderByNewsCommends=function(){
        if(orderNewsCommends=="none"){
            $scope.orderCondition="/newsCommends";
            orderNewsCommends="newsCommends";
            $scope.getCommentData(1);
        }else if(orderNewsCommends=="newsCommends"){
            $scope.orderCondition="";
            orderNewsCommends="none";
            $scope.getCommentData(1);
        }
    };
    var orderCrawlerCommends="none";
    $scope.orderByCrawlerCommends=function(){
        if(orderCrawlerCommends=="none"){
            $scope.orderCondition="/crawlerCommends";
            orderCrawlerCommends="crawlerCommends";
            $scope.getCommentData(1);
        }else if(orderCrawlerCommends=="crawlerCommends"){
            $scope.orderCondition="";
            orderCrawlerCommends="none";
            $scope.getCommentData(1);
        }
    };

}]);


