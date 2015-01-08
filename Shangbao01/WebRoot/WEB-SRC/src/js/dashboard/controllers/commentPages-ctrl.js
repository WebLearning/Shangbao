angular.module("Dashboard").controller("commentPagesCtrl", ["$scope","$http", function ($scope,$http) {

    /* $http.get('http://localhost:8080/Shangbao01/user/users')
     .success(function(data) {
     $scope.editorContent = data;
     }); */


//    //评论数据------------------------------------------------------------------------------------------------------------
//    $scope.commentDetailData={"currentNo":null,"pageCount":null,"commendList":[]};
//    var commentDetailsUrl="";

    //页面，获取评论第一页的数据,返回的是一个titleList-------------------------------------------------------------------
    $scope.getCommentDetailData=function(pageID)
    {
        console.log("commentDetailsUrl:"+commentDetailsUrl);

        var url=commentDetailsUrl+"/"+pageID.toString();
        $http.get(url).success(function(data){
            $scope.commentDetailData=data;
            $scope.pageNums=getPageNums($scope.commentDetailData.pageCount);
            console.log("成功获取数据");
        });
    };
//    $scope.getCommentDetailData(1);

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


}]);