angular.module("Dashboard").controller("commentPagesCtrl", ["$scope","$http", function ($scope,$http) {

    /* $http.get('http://localhost:8080/Shangbao01/user/users')
     .success(function(data) {
     $scope.editorContent = data;
     }); */


//    //评论数据------------------------------------------------------------------------------------------------------------
//    $scope.commentDetailData={"currentNo":null,"pageCount":null,"commendList":[]};
//    var commentDetailsUrl="";

    //页面，获取评论第一页的数据,返回的是一个titleList-------------------------------------------------------------------
    $scope.orderCondition="";
    $scope.pageNums="";
    $scope.currentPage="";
    $scope.getCommentDetailData=function(pageID)
    {
        var url=commentDetailsUrl+"/"+pageID.toString()+$scope.orderCondition;
//        console.log(url);
        $http.get(url).success(function(data){
            $scope.commentDetailData=data;
            $scope.pageNums=getPageNums($scope.commentDetailData.pageCount);
            $scope.currentPage=$scope.commentDetailData.currentNo;
            setPage($scope.commentDetailData.pageCount,$scope.commentDetailData.currentNo);
//            console.log("成功获取数据");

        });
    };
    function setPage(count,pageIndex){
//        var container=container;//容器
        var count=count;//总页数
        var pageIndex=pageIndex;//当前页数
        var a=[];
        //总页数少于10全部显示，大于10显示前3，后3，中间3，其余...
        if(pageIndex==1){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">&laquo;</a>";
        }else{
            a[a.length]="<a href=\"#\" class=\"prev\">&laquo;</a>";
        }
        function setPageList(){
            if (pageIndex == i) {
                a[a.length] = "<a href=\"#\" class=\"on\">" + i + "</a>";
            } else {
                a[a.length] = "<a href=\"#\">" + i + "</a>";
            }
        }
        //总页数小于10
        if (count <= 10) {
            for (var i = 1; i <= count; i++) {
                setPageList();
            }
        }
        //总页数大于10
        else{
            if(pageIndex<=4){
                for(var i=1;i<=5;i++){
                    setPageList();
                }
                a[a.length]="...<a href=\"#\">" + count + "</a>";
            }else if(pageIndex>=count-3){
                a[a.length]="<a href=\"#\">1</a>...";
                for(var i=count-4;i<=count;i++){
                    setPageList();
                }
            }else{//当前页在中间部分
                a[a.length]="<a href=\"#\">1</a>...";
                for(var i=pageIndex-2;i<=pageIndex+2;i++){
                    setPageList();
                }
                a[a.length]="...<a href=\"#\">" + count + "</a>";
            }
        }
        if(pageIndex==count){
            a[a.length]="<a href=\"#\" class=\"next unclick\">&raquo;</a>";
        }else{
            a[a.length]="<a href=\"#\" class=\"next\">&raquo;</a>";
        }
        document.getElementById("commentDetail_page").innerHTML= a.join("");
        //事件点击
        var pageClick=function(){
            var oAlink=document.getElementById("commentDetail_page").getElementsByTagName("a");
            var inx=pageIndex;//初始页码
            oAlink[0].onclick=function(){//点击上一页
                if(inx==1){
                    return false;
                }
                inx--;
                setPage(count,inx);
                $scope.getCommentDetailData(inx);
                return false;
            };
            for(var i=1;i<oAlink.length-1;i++){//点击页码
                oAlink[i].onclick=function(){
                    inx=parseInt(this.innerHTML);
                    setPage(count,inx);
                    $scope.getCommentDetailData(inx);
                    return false;
                }
            }
            oAlink[oAlink.length-1].onclick=function(){//点击下一页
                if(inx==count){
                    return false;
                }
                inx++;
                setPage(count,inx);
                $scope.getCommentDetailData(inx);
            }
        }()
    }

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

    $scope.getCommentDetailTitle=function(title,typeStr)
    {
        if(typeStr=="news"){
            $scope.commentDetailTitle=title+"_"+"商报评论";
//            console.log($scope.commentDetailTitle);
        }else if(typeStr=="crawler"){
            $scope.commentDetailTitle=title+"_"+"爬虫评论";
//            console.log($scope.commentDetailTitle);
        }
    };

    $scope.transOrderCondition=function(str){
        $scope.orderCondition=str;
        console.log($scope.orderCondition);
    };

}]);