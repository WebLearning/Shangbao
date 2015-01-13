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
        console.log(url);
        $http.get(url).success(function(data){
            console.log(data);
            $scope.crawlerPictureData=data;
            $scope.pageNums=getPageNums($scope.crawlerPictureData.pageCount);
            console.log("成功获取数据");
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
}]);
