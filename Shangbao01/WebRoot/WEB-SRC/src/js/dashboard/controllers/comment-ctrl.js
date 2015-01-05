/**
 * Created by QK on 2014/12/4.
 */

angular.module("Dashboard").controller("commentCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.commentData);
        console.log($scope.articleSelections);
        console.log($scope.articleSelectionsUrl);
    };

    $scope.addComment=function()
    {
        console.log("enter add");
        var testCommentData={
            commendId:"2",
            userName:"",
            userId:77,
            timeDate:new Date(),
            level:77,
            state:"unpublished",
            from:"home",
            content:"hello this is a test comment",
            reply:"I get it"
        };
        var url=$scope.projectName+'/commend/1/'+'22/'+'news';
        console.log(url);
        var jsonString=JSON.stringify(testCommentData);
        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("get data");
            console.log(data);
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
        clearArticleSelections();
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
        var url=$scope.projectName+"/article/Crawler/"+($scope.commentData.currentNo).toString()+"/"+articleId;
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
        var arr=$scope.commentData.tileList;
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
        $scope.getCommentData($scope.commentData.currentNo);
    };

    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        if($scope.articleSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定删除选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/article/Crawler/"+($scope.commentData.currentNo).toString()+"/"+$scope.articleSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCommentData(1);
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
            var url=$scope.projectName+"/article/Crawler/"+($scope.commentData.currentNo).toString()+"/"+$scope.articleSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCommentData(1);
                alert("转暂存成功");
            });
        };
    };

    //排序---------------------------------------------------------------------------------------------------------------
    $scope.orderByState=function(){
        $scope.orderCondition="/state";
        $scope.getCommentData(1);
    };

    $scope.orderByNewsCommends=function(){
        $scope.orderCondition="/newsCommends";
        $scope.getCommentData(1);
    };

    $scope.orderByCrawlerCommends=function(){
        $scope.orderCondition="/crawlerCommends";
        $scope.getCommentData(1);
    };
}]);


