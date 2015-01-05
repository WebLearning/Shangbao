
angular.module("Dashboard").controller("crawlerCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.crawlerData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

    $scope.crawlerData=null;
    $scope.orderCondition="";

    //初始化页面，获取爬虫第一页的数据,返回的是一个titleList-------------------------------------------------------------------
    $scope.getCrawlerData=function(pageID)
    {
        var url=$scope.projectName+'/article/Crawler/'+pageID.toString()+$scope.orderCondition;
        console.log(url);
        $http.get(url).success(function(data){
            $scope.crawlerData=data;
            $scope.pageNums=getPageNums($scope.crawlerData.pageCount);
            console.log("成功获取数据");
        });
    };
    $scope.getCrawlerData(1);//会在生成页面的时候直接运行!

    $scope.refreshCrawler=function()
    {
        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getCrawlerData(1);
    };

    //初始化表头
//    $scope.tableHeadsData=["标题","标题Url","文章ID","作者","时间","来源","分类","等级","点击数","评论数","赞数","摘要","字数","活动","选择"];

    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示
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
    $scope.checkIfEmpty=function(arr)
    {
        var checkedStr;
        if(arr.length==0){
            checkedStr="无数据";
        }
        return checkedStr;
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
    $scope.goNewArticle=function(articleId)
    {
        $scope.showCrawlerArticle(articleId);
        document.getElementById("crawlerArticle").className="tab-pane";
        document.getElementById("newArticle").className="tab-pane active";
        document.getElementById("crawlerSidebarID").className="sidebar-list";
    };

    //得到文章的URL
    $scope.getCrawlerArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/"+articleId;
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
    $scope.showCrawlerArticle=function(articleId)
    {
        var url=$scope.getCrawlerArticleUrl(articleId);

        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCrawlerData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.crawlerData.currentNo);
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

    $scope.selectAll=function()
    {
        var arr=$scope.crawlerData.tileList;
        for(i=0;i<arr.length;i++){
            $scope.articleSelections.push(arr[i].articleId);
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
        $scope.getCrawlerData($scope.crawlerData.currentNo);
    };

    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定删除选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCrawlerData(1);
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
            var url=$scope.projectName+"/article/Crawler/"+($scope.crawlerData.currentNo).toString()+"/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCrawlerData(1);
                alert("转暂存成功");
            });
        };
    };

    //排序---------------------------------------------------------------------------------------------------------------
    $scope.orderByWords=function(){
        $scope.orderCondition="/orderWords";
        $scope.getCrawlerData(1);
    };

    $scope.orderByCommends=function(){
        $scope.orderCondition="/orderCommends";
        $scope.getCrawlerData(1);
    };

    $scope.orderByTime=function(){
        $scope.orderCondition="/orderTime";
        $scope.getCrawlerData(1);
    };

    $scope.orderByClicks=function(){
        $scope.orderCondition="/orderClicks";
        $scope.getCrawlerData(1);
    };

    $scope.orderByLikes=function(){
        $scope.orderCondition="/orderTime";
        $scope.getCrawlerData(1);
    };

}]);



//    $scope.goCrawlerList=function()
//    {
//        document.getElementById("crawlerArticle_article").className="tab-pane";
//        document.getElementById("crawlerArticle_list").className="tab-pane active";
//    };
