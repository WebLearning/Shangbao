//angular.module("Dashboard").controller("commentDetailsCtrl", ["$scope","$http", function ($scope,$http) {
//
//    /* $http.get('http://localhost:8080/Shangbao01/user/users')
//     .success(function(data) {
//     $scope.editorContent = data;
//     }); */
//
//    $scope.testLog=function()
//    {
//        console.log($scope.commentDetailData);
//    };
//
//    $scope.goCommentList=function()
//    {
//        document.getElementById("comment").className="tab-pane active";
//        document.getElementById("commentDetails").className="tab-pane";
//    };
//
//
//}]);



angular.module("Dashboard").controller("commentDetailsCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function(){
        console.log($scope.commentDetailData);
        console.log(commentDetailsUrl);
        console.log($scope.commentSelections);
        console.log($scope.commentSelectionsUrl);
    };

//    $scope.commentDetailData=null;
    $scope.orderCondition="";

    $scope.refreshCommentDetails=function()
    {
        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getCommentDetailData(1);
    };

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
            return checkedStr;
        }else{
            return arr.toString();
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

    //返回评论列表--------------------------------------------------------------------------------------------------------
    $scope.goCommentList=function()
    {
        document.getElementById("comment").className="tab-pane active";
        document.getElementById("commentDetails").className="tab-pane";
    };


    //页面跳转------------------------------------------------------------------------------------------------------------
    $scope.turnToPage=function(pageNum)
    {
        $scope.getCommentDetailData(pageNum);
    };

    //页码样式
    $scope.pageNumClass=function(pageNum)
    {
        return(pageNum==$scope.commentDetailData.currentNo);
    };



    //评论的选取和操作------------------------------------------------------------------------------------------------------
    //评论的选取
    $scope.commentSelections=[];
    $scope.commentSelectionsUrl="";

    $scope.selectArticle=function(articleId,selectState)
    {
        if(!selectState){
            $scope.commentSelections.push(articleId);
        }else{
            var index=$scope.commentSelections.indexOf(articleId);
            $scope.commentSelections.splice(index,1);
        }
//        console.log($scope.commentSelections);

        if($scope.commentSelections.length>0){
            var str="";
            for(i=0;i<$scope.commentSelections.length;i++){
                str+=($scope.commentSelections[i]+"_");
                $scope.commentSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrl="";
        }
//        console.log($scope.commentSelectionsUrl);
    };

    $scope.checkSelectState=function(articleId)
    {
        if($scope.commentSelections.length>0){
            for(i=0;i<$scope.commentSelections.length;i++){
                if(articleId==$scope.commentSelections[i]){
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
        $scope.commentSelections=[];
        $scope.commentSelectionsUrl="";
    }

    var allSelectState="none";
    $scope.selectAll=function()
    {
        var arr=$scope.commentDetailData.tileList;
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
                $scope.commentSelections.push(arr[i].articleId);
            }
        }else{
            $scope.commentSelections=[];
        }
        if($scope.commentSelections.length>0){
            var str="";
            for(i=0;i<$scope.commentSelections.length;i++){
                str+=($scope.commentSelections[i]+"_");
                $scope.commentSelectionsUrl=str.substr(0,str.length-1);
            }
        }else{
            $scope.commentSelectionsUrl="";
        }
        $scope.getCommentDetailData($scope.commentDetailData.currentNo);
    }
    //对选取的文章进行操作
    $scope.deleteArticleSelections=function()
    {
        if($scope.commentSelectionsUrl==""){
            alert("未选取文章");
        }else{
            if (confirm("确定删除选中的文章吗？")==true)
            {
                var url=$scope.projectName+"/article/Crawler/"+($scope.commentDetailData.currentNo).toString()+"/statechange/"+$scope.commentSelectionsUrl;
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCommentDetailData(1);
                    alert("删除成功");
                });
            };
        };
    };

    $scope.saveArticleSelections=function()
    {
        if($scope.commentSelectionsUrl==""){
            alert("未选取文章");
        }else{
            var url=$scope.projectName+"/article/Crawler/"+($scope.commentDetailData.currentNo).toString()+"/statechange/"+$scope.commentSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCommentDetailData(1);
                alert("转暂存成功");
            });
        };
    };

    //排序---------------------------------------------------------------------------------------------------------------
    $scope.orderByWords=function(){
        $scope.orderCondition="/words";
        $scope.getCommentDetailData(1);
    };

    $scope.orderByCommends=function(){
        $scope.orderCondition="/commends";
        $scope.getCommentDetailData(1);
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
        $scope.getCommentDetailData(1);
    };

    $scope.orderByClicks=function(){
        $scope.orderCondition="/clicks";
        $scope.getCommentDetailData(1);
    };

    $scope.orderByLikes=function(){
        $scope.orderCondition="/likes";
        $scope.getCommentDetailData(1);
    };

}]);

