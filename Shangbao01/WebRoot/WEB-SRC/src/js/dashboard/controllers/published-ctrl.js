/**
 Published-ctrl
 **/
angular.module("Dashboard").controller("publishedCtrl",["$scope","$http",function($scope,$http){

    $scope.testLog=function(){
        console.log($scope.publishedData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

    $scope.publishedData=null;
    $scope.orderCondition="";

    //初始化页面，获取已发布文章的第一页数据，返回的是一个titleList-----------------------------------------------------
    $scope.getPublishedData=function(pageID){
        var url=$scope.projectName+'/article/Published/'+pageID.toString()+$scope.orderCondition;
        console.log(url);
        $http.get(url).success(function(data){
            $scope.publishedData=data;
            $scope.pageNums=getPageNums($scope.publishedData.pageCount);
            console.log("成功获取数据");
        });
    };
    $scope.getPublishedData(1);//在点击已发布文章时，直接生成第一页内容

    $scope.refreshPublished=function()
    {
        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getPublishedData(1);
    };
    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示---------------------
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
            return checkedStr;
        }else{
            return arr.toString();
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
    //文章跳转,点击文章名（标题）在新建页面显示文章内容------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewArticle=function(articleId)
    {
        $scope.showPublishedArticle(articleId);
        document.getElementById("published").className="tab-pane";
        document.getElementById("publishedArticle").className="tab-pane active";
        document.getElementById("publishedSidebarID").className="sidebar-list";
    };

    //得到文章的URL
    $scope.getPublishedArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/article/Published/"+($scope.publishedData.currentNo).toString()+"/"+articleId;
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
    $scope.showPublishedArticle=function(articleId)
    {
        var url=$scope.getPublishedArticleUrl(articleId);

        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
    };

    //排序(作者，评论数等）---------------------------------------------------------------------------------------------------------------
    $scope.orderByWords=function(){
        $scope.orderCondition="/words";
        $scope.getPublishedData(1);
    };

    $scope.orderByCommends=function(){
        $scope.orderCondition="/commends";
        $scope.getPublishedData(1);
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
        $scope.getPublishedData(1);
    };

    $scope.orderByClicks=function(){
        $scope.orderCondition="/clicks";
        $scope.getPublishedData(1);
    };

    $scope.orderByLikes=function(){
        $scope.orderCondition="/likes";
        $scope.getPublishedData(1);
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getPublishedData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.publishedData.currentNo);
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
        var arr=$scope.publishedData.tileList;
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
        $scope.getPublishedData($scope.publishedData.currentNo);
    };
    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定撤销选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/article/Published/"+($scope.publishedData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getPublishedData(1);
                    alert("撤销成功");
                });
            };
        };
    };
}]);