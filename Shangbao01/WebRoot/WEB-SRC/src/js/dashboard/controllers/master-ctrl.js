/**
 * Master Controller
 */
angular.module('Dashboard', ["ng.ueditor"]).controller('MasterCtrl', ['$scope', MasterCtrl]);

function MasterCtrl($scope) {

    //菜单的伸缩
    $scope.toggle=true;

    $scope.toggleSidebar = function()
    {
        $scope.toggle = ! $scope.toggle;
    };

    //IP和projectName的全局变量
    $scope.projectName="http://localhost:8080/Shangbao01";

    //文章数据
//    $scope.articleData={
//        title:"",
//        subTitle:"",
//        keyWord:[],
//        author:"",
//        summary:"",
//        content:'',
//        time:"",
//        channel:[],
//        picturesUrl:[],
//        level:"",
//        from:"",
//        words:0
//    };

    $scope.articleData={
        activity:"" ,
        author: "",
        channel: [],
        channelIndex: null,
        clicks: null,
        content: "",
        crawlerCommends: null,
        crawlerCommendsPublish: null,
        from: "",
        id: null,
        keyWord: [],
        level: null,
        likes: null,
        newsCommends: null,
        newsCommendsPublish: null,
        picturesUrl: [],
        subTitle: "",
        summary: "",
        tag: null,
        time: null,
        title: "",
        titlePicUrl: null,
        words: null
    };

    $scope.newArticleData={
        activity:"" ,
        author: "",
        channel: [],
        channelIndex: null,
        clicks: null,
        content: "",
        crawlerCommends: null,
        crawlerCommendsPublish: null,
        from: "",
        keyWord: [],
        level: null,
        likes: null,
        newsCommends: null,
        newsCommendsPublish: null,
        picturesUrl: [],
        subTitle: "",
        summary: "",
        tag: null,
        time: null,
        title: "",
        titlePicUrl: null,
        words: null
    };


    //初始化header
    $scope.curPage = "一览";

    //点击sidebar之后改变header
    $scope.changeCurPage = function(str)
    {
        $scope.curPage=str;
        //如果是点击新建文章就清除文章里的数据
        if(str=="文章/新建"){
            clearNewArticleData();
        }
    };

    function clearArticleData(){
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                $scope.articleData[p]=[];
            }else if(p=="words"){
                $scope.articleData[p]=0;
            }else{
                $scope.articleData[p]="";
            }
        }
    }

    function clearNewArticleData(){
        for(p in $scope.newArticleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                $scope.newArticleData[p]=[];
            }else if(p=="words"){
                $scope.newArticleData[p]=0;
            }else{
                $scope.newArticleData[p]="";
            }
        }
    }

}
