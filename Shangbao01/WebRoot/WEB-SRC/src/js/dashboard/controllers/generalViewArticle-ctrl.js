angular.module("Dashboard").controller("generalViewArticleCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.goGeneral=function()
    {
        $scope.clearArticle();
        $scope.coverIt();
        document.getElementById("generalViewArticle").className="tab-pane";
        document.getElementById("generalView").className="tab-pane active";
        document.getElementById("generalSidebarID").className="sidebar-list";
        $scope.getNewGeneralViewData();
        $scope.closeOver();
    };
    $scope.clearArticle=function()
    {
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"||p=="logs"){
                $scope.articleData[p]=[];
            }else if(p=="author"||p=="content"||p=="from"||p=="subTitle"||p=="time"||p=="title"||p=="summary"||p=="activity"||p=="outSideUrl"){
                $scope.articleData[p]="";
            }else{
                $scope.articleData[p]=null;
            }
        }
//        $scope.calculateWords();
//        $scope.calculatePictures();
    };
}]);