/**
 crawlerPicture-controller
 **/
angular.module("Dashboard").controller("crawlerPictureCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        $scope.getCrawlerPictureData(1);
//        console.log($scope.crawlerPictureData);
//        console.log($scope.articleSelections);
//        console.log($scope.articleSelectionsUrl);
    };
    $scope.crawlerPictureData=null;
    $scope.orderCondition="";

    //初始化页面，获取爬虫第一页的数据,返回的是一个titleList-------------------------------------------------------------------
    $scope.getCrawlerPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Crawler/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.crawlerPictureData=data;
            $scope.pageNums=getPageNums($scope.crawlerPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getCrawlerPictureData(1);//会在生成页面的时候直接运行!

    $scope.refreshCrawlerPicture=function()
    {
        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getCrawlerPictureData(1);
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
    $scope.checkNum=function(str){
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
    $scope.goNewPicture=function(articleId)
    {
        $scope.showCrawlerPicture(articleId);
        document.getElementById("crawlerPicture").className="tab-pane";
        document.getElementById("crawlerPictureView").className="tab-pane active";
        document.getElementById("crawlerPictureSidebarID").className="sidebar-list";
    };

    //得到文章的URL
    $scope.getCrawlerPictureUrl=function(articleId)
    {
        var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/"+articleId;
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
    $scope.showCrawlerPicture=function(articleId)
    {
        var url=$scope.getCrawlerPictureUrl(articleId);

        $http.get(url).success(function(data) {
//            console.log(data);
            $scope.transDataToArticleData(data);
        });
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCrawlerPictureData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.crawlerPictureData.currentNo);
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
        var arr=$scope.crawlerPictureData.tileList;
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
        $scope.getCrawlerPictureData($scope.crawlerPictureData.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定删除选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCrawlerPictureData(1);
                    alert("删除成功");
                });
            };
        };
    };

    $scope.saveArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            var url=$scope.projectName+"/picture/Crawler/"+($scope.crawlerPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCrawlerPictureData(1);
                alert("转暂存成功");
            });
        };
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
        $scope.getCrawlerPictureData(1);
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
        $scope.getCrawlerPictureData(1);
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
        $scope.getCrawlerPictureData(1);
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
        $scope.getCrawlerPictureData(1);
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
        $scope.getCrawlerPictureData(1);
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
        $scope.getCrawlerPictureData(1);
    };
}]);
