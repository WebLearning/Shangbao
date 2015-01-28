/**
 DraftPicture ctrl
 **/
angular.module("Dashboard").controller("draftPictureCtrl",["$scope","$http", function($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.tempPictureData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

    //检查表的内容 数据若是NULL则显示"无",数组若是空则显示"无数据",转化时间戳为日期显示
    $scope.checkSummary=function(str){
        var checkedStr;
        if(str==null||str==""){
            checkedStr="无";
        }else if(str.length>20){
            checkedStr=str.substr(0,20)+"...";
        }else{
            checkedStr=str;
        }
        return checkedStr;
    };
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
        if(str==null||str==""||str=="0"){
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
            var date=new Date(dateStr);
            return $scope.formatDate(date);
        }
    };

    //文章跳转------------------------------------------------------------------------------------------------------------
    //转到新建文章页面并重置sidebar的爬虫文章按钮，不然会产生点击无效的BUG
    $scope.goNewPictureArticle=function(articleId)
    {
        $scope.showTempArticle(articleId);
        document.getElementById("draftPicture").className="tab-pane";
        document.getElementById("draftPictureView").className="tab-pane active";
        document.getElementById("draftPictureSidebarID").className="sidebar-list";
    };

    //得到文章的URL
    $scope.getTempArticleUrl=function(articleId)
    {
        var url=$scope.projectName+"/picture/Temp/"+($scope.tempPictureData.currentNo).toString()+"/"+articleId;
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
    $scope.showTempArticle=function(articleId)
    {
        var url=$scope.getTempArticleUrl(articleId);

        $http.get(url).success(function(data) {
            $scope.transDataToArticleData(data);
        });
    };

    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getTempPictureData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.tempPictureData.currentNo);
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
        if(allSelectState=="none"){
            selectByArr($scope.tempPictureData.tileList);
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
        $scope.getTempPictureData($scope.tempPictureData.currentNo);
    }

    //对选取的文章进行操作
    $scope.deletePictureArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定删除选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/picture/Temp/"+($scope.tempPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getTempPictureData(1);
                    alert("删除成功");
                });
            };
        };
    };

    $scope.submitPictureArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            var url=$scope.projectName+"/picture/Temp/"+($scope.tempPictureData.currentNo).toString()+"/statechange/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getTempPictureData(1);
                alert("提交成功");
            });
        };
    };

    //排序---------------------------------------------------------------------------------------------------------------
    var wordsOrderState="desc";
    $scope.orderByWords=function(){
        if(wordsOrderState=="desc"){
            $scope.transOrderConditions("/words/asc");
            wordsOrderState="asc";
        }else if(wordsOrderState=="asc"){
            $scope.transOrderConditions("/words/desc");
            wordsOrderState="desc";
        }
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            console.log($scope.tempPictureSearchData.content);
            $scope.getTempPictureSearchData(1);
        }
    };

    var newsCommendsOrderState="desc";
    $scope.orderByNewsCommends=function(){
        if(newsCommendsOrderState=="desc"){
            $scope.transOrderConditions("/newsCommends/asc");
            newsCommendsOrderState="asc";
        }else if(newsCommendsOrderState=="asc"){
            $scope.transOrderConditions("/newsCommends/desc");
            newsCommendsOrderState="desc";
        }
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            console.log($scope.tempPictureSearchData.content);
            $scope.getTempPictureSearchData(1);
        }
    };
    var crawlerCommendsOrderState="desc";
    $scope.orderByCrawlerCommends=function(){
        if(crawlerCommendsOrderState=="desc"){
            $scope.transOrderConditions("/crawlerCommends/asc");
            crawlerCommendsOrderState="asc";
        }else if(crawlerCommendsOrderState=="asc"){
            $scope.transOrderConditions("/crawlerCommends/desc");
            crawlerCommendsOrderState="desc";
        }
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            console.log($scope.tempPictureSearchData.content);
            $scope.getTempPictureSearchData(1);
        }
    };

    var timeOrderState="desc";
    $scope.orderByTime=function(){
        if(timeOrderState=="desc"){
            $scope.transOrderConditions("/time/asc");
            timeOrderState="asc";
        }else if(timeOrderState=="asc"){
            $scope.transOrderConditions("/time/desc");
            timeOrderState="desc";
        }
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            console.log($scope.tempPictureSearchData.content);
            $scope.getTempPictureSearchData(1);
        }
    };

    var clicksOrderState="desc";
    $scope.orderByClicks=function(){
        if(clicksOrderState=="desc"){
            $scope.transOrderConditions("/clicks/asc");
            clicksOrderState="asc";
        }else if(clicksOrderState=="asc"){
            $scope.transOrderConditions("/clicks/desc");
            clicksOrderState="desc";
        }
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            console.log($scope.tempPictureSearchData.content);
            $scope.getTempPictureSearchData(1);
        }
    };

    var likesOrderState="desc";
    $scope.orderByLikes=function(){
        if(likesOrderState=="desc"){
            $scope.transOrderConditions("/likes/asc");
            likesOrderState="asc";
        }else if(likesOrderState=="asc"){
            $scope.transOrderConditions("/likes/desc");
            likesOrderState="desc";
        }
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            console.log($scope.tempPictureSearchData.content);
            $scope.getTempPictureSearchData(1);
        }
    };

}]);