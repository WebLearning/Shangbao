// angular.module("Dashboard", []);

angular.module("Dashboard").controller("generalViewCtrl", ["$scope","$http", function ($scope, $http) {

    $scope.testData=function()
    {
        console.log("test data");
        $http.get('http://localhost:8080/Shangbao01/backapp/all').success(function(data){
            console.log(data);
        });
    };
    $scope.refreshGeneralView=function(){
        var url=$scope.projectName+'/backapp/refresh';
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.newGeneralViewSections=data;
        })
    };
    $scope.generalViewSections=[
        {"name":"热点",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题","新闻6 标题","新闻7 标题","新闻8 标题","新闻9 标题"]},
        {"name":"本地",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"原创",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"娱乐",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"体育",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
        {"name":"财经",
            "content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]}
    ];

    $scope.newGeneralViewSections=null;

    //返回当前所有的分类以及分类的文章----------------------------------------------------------------------------------
    $scope.getNewGeneralViewData=function()
    {
        var url=$scope.projectName+'/backapp/all';
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.newGeneralViewSections=data;
            //console.log("成功获取数据");
        });
    };
    $scope.getNewGeneralViewData();

    //设置文章的位置(上移一位，下移一位)--------------------------------------------------------------------------------
    $scope.upGeneralViewArticle=function(channelEnglishName,index){
        var url=$scope.projectName+'/backapp/setlocation/'+channelEnglishName+'/'+index+'/true';
        //console.log(url);
        $http.put(url).success(function(){
            $scope.getNewGeneralViewData();
            //console.log("上移成功");
        });
    };
    $scope.downGeneralViewArticle=function(channelEnglishName,index){
        var url=$scope.projectName+'/backapp/setlocation/'+channelEnglishName+'/'+index+'/false';
        //console.log(url);
        $http.put(url).success(function(){
            $scope.getNewGeneralViewData();
            //console.log("下移成功");
        });
    };
    //将文章置顶--------------------------------------------------------------------------------------------------------
    $scope.topGeneralViewArticle=function(channelEnglishName,index){
        var url=$scope.projectName+'/backapp/settop/'+channelEnglishName+'/'+index;
        //console.log(url);
        $http.put(url).success(function(){
            //console.log(data);
            $scope.getNewGeneralViewData();
        });
    };
}]);