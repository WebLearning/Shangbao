/**
 * Master Controller
 */
angular.module('Dashboard', ["ng.ueditor"]).controller('MasterCtrl', ['$scope', MasterCtrl]);

function MasterCtrl($scope) {

    $scope.toggle=true;

    $scope.toggleSidebar = function()
    {
        $scope.toggle = ! $scope.toggle;
    };


    //设置当前内容的header

    $scope.curPage = "一览";

    $scope.changeCurPage = function(str)
    {
        $scope.curPage=str;
    }

    //导航条
//    $scope.sidebars=[
//        {name:"一览",href:"generalView",icon:"menu-icon glyphicon glyphicon-eye-open",content:[]},
//        {name:"文章",href:"article",icon:"menu-icon glyphicon glyphicon-file",content:[
//            {name:"新建",href:"newArticle"},
//            {name:"爬虫文章",href:"crawlerArticle"},
//            {name:"待审",href:"pendingTrial"},
//        ]},
//        {name:"评论",href:"comment",icon:"menu-icon glyphicon glyphicon-list-alt",content:[]},
//        {name:"管理",href:"management",icon:"menu-icon glyphicon glyphicon-folder-open",content:[]},
//        {name:"设置",href:"settings",icon:"menu-icon glyphicon glyphicon-cog",content:[]}
//    ];

}
