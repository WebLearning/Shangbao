
angular.module("Dashboard").controller("commentDetailsCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.testLog=function()
    {
        console.log($scope.commentDetailData);
        console.log(commentDetailsUrl);
        console.log($scope.commentSelections);
        console.log($scope.commentSelectionsUrl);
    };

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

    $scope.selectArticle=function(commendId,selectState)
    {
        if(!selectState){
            $scope.commentSelections.push(commendId);
        }else{
            var index=$scope.commentSelections.indexOf(commendId);
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

    $scope.checkSelectState=function(commendId)
    {
        if($scope.commentSelections.length>0){
            for(i=0;i<$scope.commentSelections.length;i++){
                if(commendId==$scope.commentSelections[i]){
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
        var arr=$scope.commentDetailData.commendList;
        console.log($scope.commentDetailData.commendList);
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
                $scope.commentSelections.push(arr[i].commendId);
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
    $scope.deleteCommentSelections=function()
    {
        if($scope.commentSelectionsUrl==""){
            alert("未选取评论");
        }else{
            if (confirm("确定删除选中的评论吗？")==true)
            {
                var url=commentDetailsUrl+'/'+$scope.commentSelectionsUrl;
                console.log(url);
                $http.delete(url).success(function(){
                    clearArticleSelections();
                    $scope.getCommentDetailData(1);
                    alert("删除成功");
                });
            };
        };
    };

    $scope.publishCommentSelections=function()
    {
        if($scope.commentSelectionsUrl==""){
            alert("未选取评论");
        }else{
            var url=commentDetailsUrl+'/'+$scope.commentSelectionsUrl;
            $http.put(url).success(function(){
                clearArticleSelections();
                $scope.getCommentDetailData(1);
                alert("发布成功");
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

    //对一篇文章新建评论------------------------------------------------------------------------------------------------
    $scope.inputCommentData={
        commendId:"2",
        userName:"user",
        userId:77,
        timeDate:new Date(),
        level:77,
        state:"unpublished",
        from:"home",
        content:"",
        reply:""
    };
    $scope.testInputCommentData=function(){
        console.log($scope.inputCommentData);
    };
    $scope.addComments=function(){
        var url=commentDetailsUrl;
        console.log(url);
        var jsonString=JSON.stringify($scope.inputCommentData);
        console.log(jsonString);
        $http.post(url,jsonString).success(function(data){
            console.log("添加成功");
        });
        $scope.refreshCommentDetails();
        $('#myModal_addComment').modal('toggle')
    };
}]);

