/**
 pendingPictureCtrl
 **/
angular.module("Dashboard").controller("pendingPictureCtrl",["$scope","$http",function($scope,$http){

    $scope.pendingPictureTestLog=function() {
        console.log($scope.pendingPictureData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

    $scope.pendingPictureData=null;
    $scope.orderCondition="";

    //4.1点击待审文章，初始化页面，获得待审的第一页数据----------------------------------------------------------------------------------

    $scope.getPendingPictureData=function(pageID){

        var url=$scope.projectName+'/picture/Pending/'+pageID.toString()+$scope.orderCondition;
       // console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.pendingPictureData=data;
            $scope.pageNums=getPageNums($scope.pendingPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getPendingPictureData(1);//生成待审页面时即产生第一页数据

    $scope.refreshPendingPicture=function()
    {
        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getPendingPictureData(1);
    };

    //排序---------------------------------------------------------------------------------------------------------------
    var wordsOrderState="desc";
    $scope.orderByWords=function(){
        if(wordsOrderState=="desc"){
            $scope.orderCondition="/words/"+"asc";
            wordsOrderState="asc";
        }else if(wordsOrderState=="asc"){
            $scope.orderCondition="/words/"+"desc";
            wordsOrderState="desc";
        }
        $scope.getPendingPictureData(1);
    };

    var newsCommendsOrderState="desc";
    $scope.orderByNewsCommends=function(){
        if(newsCommendsOrderState=="desc"){
            $scope.orderCondition="/newsCommends/"+"asc";
            newsCommendsOrderState="asc";
        }else if(newsCommendsOrderState=="asc"){
            $scope.orderCondition="/newsCommends/"+"desc";
            newsCommendsOrderState="desc";
        }
        $scope.getPendingPictureData(1);
    };
    var crawlerCommendsOrderState="desc";
    $scope.orderByCrawlerCommends=function(){
        if(crawlerCommendsOrderState=="desc"){
            $scope.orderCondition="/crawlerCommends/"+"asc";
            crawlerCommendsOrderState="asc";
        }else if(crawlerCommendsOrderState=="asc"){
            $scope.orderCondition="/crawlerCommends/"+"desc";
            crawlerCommendsOrderState="desc";
        }
        $scope.getPendingPictureData(1);
    };

    var timeOrderState="desc";
    $scope.orderByTime=function(){
        if(timeOrderState=="desc"){
            $scope.orderCondition="/time/"+"asc";
            timeOrderState="asc";
        }else if(timeOrderState=="asc"){
            $scope.orderCondition="/time/"+"desc";
            timeOrderState="desc";
        }
        $scope.getPendingPictureData(1);
    };

    var clicksOrderState="desc";
    $scope.orderByClicks=function(){
        if(clicksOrderState=="desc"){
            $scope.orderCondition="/clicks/"+"asc";
            clicksOrderState="asc";
        }else if(clicksOrderState=="asc"){
            $scope.orderCondition="/clicks/"+"desc";
            clicksOrderState="desc";
        }
        $scope.getPendingPictureData(1);
    };

    var likesOrderState="desc";
    $scope.orderByLikes=function(){
        if(likesOrderState=="desc"){
            $scope.orderCondition="/likes/"+"asc";
            likesOrderState="asc";
        }else if(likesOrderState=="asc"){
            $scope.orderCondition="/likes/"+"desc";
            likesOrderState="desc";
        }
        $scope.getPendingPictureData(1);
    };

    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示
    $scope.checkIfNull=function(str)
    {
        var checkedStr;
        if(str==null||str==""){
            checkedStr="无";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
    $scope.checkNum=function(str)
    {
        var checkedStr;
        if(str=="0"||str==null||str==""){
            checkedStr="0";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
    $scope.checkIfEmpty=function(arr)
    {
        var checkedStr;
        if(arr.length==0){
            checkedStr="无数据";
            return checkedStr;
        }else{
            return arr;
        }
        //return arr.toString();
    };
    $scope.dateStringToDate=function(dateStr)
    {
        if(dateStr==null||dateStr==""){
            return "无";
        }else{
            var date=new Date(Date(dateStr));
            return date.toDateString();
        }
    };

    //文章跳转------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewPictureArticle=function(articleId)
    {
        $scope.showPendingPictureArticle(articleId);
        document.getElementById("pendingPicture").className="tab-pane";
        document.getElementById("pendingPictureView").className="tab-pane active";
        document.getElementById("pendingPictureSidebarID").className="sidebar-list";
    };

    //得到文章的URL
    $scope.getPendingPictureArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/"+articleId;
        return url;
    };

    //将文章数据传输给全局变量articleData
    $scope.transDataToArticleData=function(data)
    {
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                for(i in data[p]){
                    $scope.articleData[p][i]=data[p][i];
                }
            }else{
                $scope.articleData[p]=data[p];
            }
        }
    };

    //显示点击的文章
    $scope.showPendingPictureArticle=function(articleId)
    {
        var url=$scope.getPendingPictureArticleUrl(articleId);

        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
    };

    //文章的选取和操作------------------------------------------------------------------------------------------------------
    //文章的选取
    $scope.articleSelections=[];
    $scope.articleSelectionsUrl="";

    $scope.selectArticle=function(articleId,selectState)
    {
        if(!selectState){
            $scope.articleSelections.push(articleId);
        }else{
            var index=$scope.articleSelections.indexOf(articleId);
            $scope.articleSelections.splice(index,1);
        }
//        console.log($scope.articleSelections);

        if($scope.articleSelections.length>0){
            var str="";
            for(i=0;i<$scope.articleSelections.length;i++){
                str+=($scope.articleSelections[i]+"_");
                $scope.articleSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.articleSelectionsUrl="";
        }
//        console.log($scope.articleSelectionsUrl);
    };

    $scope.checkSelectState=function(articleId)
    {
        if($scope.articleSelections.length>0){
            for(i=0;i<$scope.articleSelections.length;i++){
                if(articleId==$scope.articleSelections[i]){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    };

    function clearArticleSelections()
    {
        $scope.articleSelections=[];
        $scope.articleSelectionsUrl="";
    }

    var allSelectState="none";
    $scope.selectAll=function()
    {
        var arr=$scope.pendingPictureData.tileList;
        if(allSelectState=="none"){
            selectByArr(arr);
            allSelectState="all";
        }else if(allSelectState=="all"){
            selectByArr([]);
            allSelectState="none";
        }
    };
    function selectByArr(arr){
        if(arr.length>0){
            for(i=0;i<arr.length;i++){
                $scope.articleSelections.push(arr[i].articleId);
            }
        }else{
            $scope.articleSelections=[];
        }
        if($scope.articleSelections.length>0){
            var str="";
            for(i=0;i<$scope.articleSelections.length;i++){
                str+=($scope.articleSelections[i]+"_");
                $scope.articleSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.articleSelectionsUrl="";
        }
        $scope.getPendingPictureData($scope.pendingPictureData.currentNo);
    }
    //对选取的文章进行操作
    //删除-------------
    $scope.deletePictureArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定撤销选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getPendingPictureData(1);
                    alert("撤销成功");
                });
            };
        };
    };
//立刻发布--------------------------------------------------------------------------------------------------------
    $scope.publishPictureArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            var url=$scope.projectName+"/picture/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getPendingPictureData(1);
                alert("发布成功");
            });
        };
    };
    //定时发布----------------------------------------------------------------------------------------------------------
    $scope.Time="";
    $scope.publishArticleSelectionsTiming=function()
    {
        if($scope.articleSelectionUrl==""){
            alert("未选取文章");
        }else{
            $scope.Time=$scope.publishTime;
            var url=$scope.projectName+"/article/Pending/"+($scope.pendingPictureData.currentNo).toString()+"/"+$scope.articleSelectionsUrl;
            //加参数time(定时发布的时间）
            clearArticleSelections();
            $scope.getPendingPictureData(1);
            alert("保存成功");
        }
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getPendingPictureData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.pendingPictureData.currentNo);
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
    };
}]);