/**
 * Master Controller
 */
angular.module("Dashboard", ["ng.ueditor"]).controller("MasterCtrl",["$scope","$http",function($scope,$http){
    //菜单的伸缩
    $scope.toggle=true;

    $scope.toggleSidebar = function()
    {
        $scope.toggle = ! $scope.toggle;
    };

    //IP和projectName的全局变量
    $scope.projectName="http://localhost:8080/Shangbao01";
    $scope.projectActionName="http://localhost:8080/Shangbao01/article/upload";

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
        state:null,
        tag: null,
        time: "",
        title: "",
        titlePicUrl: null,
        words: null
    };

    $scope.newArticleData={
        activity:null ,
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
        time: "",
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
//        console.log(str);
        //如果是点击新建文章就清除文章里的数据
        if(str=="文章/新建"){
            clearNewArticleData();
//            $scope.getNewChannelNames();
        }else if(str=="快拍成都/新建"){
            clearNewArticleData();
            $scope.getNewActivityNames();
        }
        else if(str=="文章/爬虫文章"){
            $scope.refreshCrawler();
        }else if(str=="文章/待审"){
            $scope.refreshPending();
        }else if(str=="文章/已发布"){
            $scope.refreshPublished();
        }else if(str=="文章/已撤销"){
            $scope.refreshRevoked();
        }else if(str=="文章/草稿箱"){
            $scope.refreshTemp();
        }else if(str=="一览"){
            $scope.refreshGeneralView();
        }else if(str=="快拍成都/爬虫图片"){
            $scope.refreshCrawlerPicture();
        }else if(str=="快拍成都/待审"){
            $scope.refreshPendingPicture();
        }else if(str=="快拍成都/已发布"){
            $scope.refreshPublishedPicture();
        }else if(str=="快拍成都/已撤销"){
            $scope.refreshRevokedPicture();
        }else if(str=="快拍成都/草稿箱"){
            $scope.refreshTempPicture();
        }else if(str=="评论"){
            $scope.goCommentList();
        }
    };

    function clearArticleData(){
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                $scope.articleData[p]=[];
            }else if(p=="words"){
                $scope.articleData[p]=0;
            }else if(p=="author"||p=="title"||p=="content"||p=="from"||p=="subTitle"||p=="summary"||p=="time"){
                $scope.articleData[p]="";
            }else{
                $scope.articleData[p]=null;
            }
        }
    }

    function clearNewArticleData(){
        for(p in $scope.newArticleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                $scope.newArticleData[p]=[];
            }else if(p=="words"){
                $scope.newArticleData[p]=0;
            }else if(p=="author"||p=="title"||p=="content"||p=="from"||p=="subTitle"||p=="summary"||p=="time"){
                $scope.newArticleData[p]="";
            }else{
                $scope.newArticleData[p]=null;
            }
        }
    }

    //评论数据------------------------------------------------------------------------------------------------------------
    $scope.commentDetailData={
        "currentNo":null,
        "pageCount":null,
        "commendList":[{
            commendId:"",
            userName:"",
            userId:"",
            timeDate:new Date(),
            level:"",
            state:"",
            from:"",
            content:"",
            reply:""
        }]
    };
    var commentDetailsUrl="";
    $scope.commentDetailTitle="";

//全局共用数据----------------------------------------------------------------------------------------------------------
    $scope.orderCondition="";
    $scope.transOrderConditions=function(str){
        $scope.orderCondition=str;
//        console.log($scope.orderCondition);
    };
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
//    $scope.articleSelections=[];
//    $scope.articleSelectionsUrl="";
//    function clearArticleSelections()
//    {
//        $scope.articleSelections=[];
//        $scope.articleSelectionsUrl="";
//    }
    //获取评论数据----------------------------------------------------------------------------------------------------------
    $scope.commentData=null;
    $scope.getCommentData=function(pageID)
    {
        var url=$scope.projectName+'/commend/'+pageID.toString()+$scope.orderCondition;
//        console.log(url);
        $http.get(url).success(function(data){
            $scope.commentData=data;
            $scope.commentPageNums=getPageNums($scope.commentData.pageCount);
//            console.log("成功获取数据");
        });
    };
    $scope.getCommentData(1);//会在生成页面的时候直接运行!

    $scope.refreshComment=function()
    {
        $scope.orderCondition="";
        $scope.getCommentData(1);
    };
    $scope.goCommentList=function()
    {
        document.getElementById("comment").className="tab-pane active";
        document.getElementById("commentDetails").className="tab-pane";
        $scope.refreshComment();
    };
//（1）获取爬虫数据-----------------------------------------------------------------------------------------------------
    $scope.crawlerData=null;
    $scope.getCrawlerData=function(pageID)
    {
        var url=$scope.projectName+'/article/Crawler/'+pageID.toString()+$scope.orderCondition;
//        console.log(url);
        $http.get(url).success(function(data){
            $scope.crawlerData=data;
            $scope.crawlerPageNums=getPageNums($scope.crawlerData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getCrawlerData(1);//会在生成页面的时候直接运行!
    $scope.refreshCrawler=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
//        $scope.getNewChannelNames();
        $scope.getCrawlerData(1);
    };
//(2)获取待审数据-------------------------------------------------------------------------------------------------------
    $scope.pendingData=null;
    $scope.getPendingData=function(pageID){

        var url=$scope.projectName+'/article/Pending/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            $scope.pendingData=data;
            $scope.pendingPageNums=getPageNums($scope.pendingData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getPendingData(1);//生成待审页面时即产生第一页数据

    $scope.refreshPending=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getPendingData(1);
    };
//(3)获取已发布数据-----------------------------------------------------------------------------------------------------
    $scope.publishedData=null;
    $scope.getPublishedData=function(pageID){
        var url=$scope.projectName+'/article/Published/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            $scope.publishedData=data;
            $scope.publishedPageNums=getPageNums($scope.publishedData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getPublishedData(1);//在点击已发布文章时，直接生成第一页内容

    $scope.refreshPublished=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getPublishedData(1);
    };
//(4)获取已撤销数据-----------------------------------------------------------------------------------------------------
    $scope.revokedData=null;
    $scope.getRevokedData=function(pageID)
    {
        var url=$scope.projectName+'/article/Revocation/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            $scope.revokedData=data;
            $scope.revokedPageNums=getPageNums($scope.revokedData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getRevokedData(1);//会在生成页面的时候直接运行!

    $scope.refreshRevoked=function()
    {
//        clearArticleSelections();
//        $scope.getNewChannelNames();
        $scope.orderCondition="";
        $scope.getRevokedData(1);
    };
//(5)获取草稿箱数据--------------------------------------------------------------------------------------------------------
    $scope.tempData=null;
    $scope.getTempData=function(pageID)
    {
        var url=$scope.projectName+'/article/Temp/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            $scope.tempData=data;
            $scope.tempPageNums=getPageNums($scope.tempData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getTempData(1);//会在生成页面的时候直接运行!

    $scope.refreshTemp=function()
    {
//        clearArticleSelections();
//        selectByArr([]);
        $scope.orderCondition="";
//        $scope.getNewChannelNames();
        $scope.getTempData(1);
    };
//(6)获取一览数据-------------------------------------------------------------------------------------------------------
    $scope.newGeneralViewSections=null;
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
    $scope.refreshGeneralView=function(){
        var url=$scope.projectName+'/backapp/refresh';
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.newGeneralViewSections=data;
        })
    };
//（7）获取快拍爬虫数据-------------------------------------------------------------------------------------------------
    $scope.crawlerPictureData=null;
    $scope.getCrawlerPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Crawler/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.crawlerPictureData=data;
            $scope.crawlerPicturePageNums=getPageNums($scope.crawlerPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getCrawlerPictureData(1);//会在生成页面的时候直接运行!
    $scope.refreshCrawlerPicture=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getCrawlerPictureData(1);
        $scope.getNewActivityNames();
    };
//(8)获取快拍待审数据---------------------------------------------------------------------------------------------------
    $scope.pendingPictureData=null;
    $scope.getPendingPictureData=function(pageID){

        var url=$scope.projectName+'/picture/Pending/'+pageID.toString()+$scope.orderCondition;
        // console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            $scope.pendingPictureData=data;
            $scope.pendingPicturePageNums=getPageNums($scope.pendingPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getPendingPictureData(1);//生成待审页面时即产生第一页数据
    $scope.refreshPendingPicture=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getPendingPictureData(1);
    };
//(9)获取快拍已发布-----------------------------------------------------------------------------------------------------
    $scope.publishedPictureData=null;
    $scope.getPublishedPictureData=function(pageID){
        var url=$scope.projectName+'/picture/Published/'+pageID.toString()+$scope.orderCondition;
        console.log(url);
        $http.get(url).success(function(data){
            $scope.publishedPictureData=data;
            $scope.publishePicturePageNums=getPageNums($scope.publishedPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getPublishedPictureData(1);//在点击已发布文章时，直接生成第一页内容
    $scope.refreshPublishedPicture=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
        console.log("test");
        $scope.getPublishedPictureData(1);
//        $scope.getNewActivityNames();
    };
//（10）获取快拍已撤销--------------------------------------------------------------------------------------------------
    $scope.revokedPictureData=null;
    $scope.getRevokedPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Revocation/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            $scope.revokedPictureData=data;
            $scope.revokedPicturePageNums=getPageNums($scope.revokedPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getRevokedPictureData(1);//会在生成页面的时候直接运行!
    $scope.refreshRevokedPicture=function()
    {
//        clearArticleSelections();
        $scope.orderCondition="";
        $scope.getRevokedPictureData(1);
        $scope.getNewActivityNames();
    };
//（11）获取快拍草稿箱数据----------------------------------------------------------------------------------------------
    $scope.tempPictureData=null;
    $scope.getTempPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Temp/'+pageID.toString()+$scope.orderCondition;
        //console.log(url);
        $http.get(url).success(function(data){
            $scope.tempPictureData=data;
            $scope.tempPicturePageNums=getPageNums($scope.tempPictureData.pageCount);
            //console.log("成功获取数据");
        });
    };
    $scope.getTempPictureData(1);//会在生成页面的时候直接运行!

    $scope.refreshTempPicture=function()
    {
//        clearArticleSelections();
//        selectByArr([]);
        $scope.orderCondition="";
        $scope.getTempPictureData(1);
        $scope.getNewActivityNames();
    };
//(12)获取分类(文章）----------------------------------------------------------------------------------------------------------
    function uniques(data){
//        data=data||[];
        var a={};
        for(var i=0;i<data.length;i++){
            var v=data[i];
            if(typeof (a[v])=="undefined"){
                a[v]=1;
            }
        }
        data.length=0;
        for(var i in a){
            data[data.length]=i;
        }
        return data;
    }
    $scope.newChannelNames=[];
    $scope.getNewChannelNames=function(){
        var url=$scope.projectName+'/channel/channels';
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.checkFirstChannel(data[i]);
                }
            }else{
                $scope.newChannelNames=[];
            }
            //console.log($scope.newChannelNames);
        });
    };
    $scope.getNewChannelNames();
    //判断是否有次级目录-------------------------------------------------------------------------------------------------
    $scope.checkFirstChannel=function(channelData){
        var url=$scope.projectName+'/channel/'+channelData.englishName+'/channels';
        $http.get(url).success(function (data) {
            if(data.length>0){
                $scope.getSecondChannelNames(channelData.englishName);
            }else{
                $scope.newChannelNames.push(channelData);
            }
        });
    };
    //获得次级目录名----------------------------------------------------------------------------------------------------
    $scope.getSecondChannelNames=function(channelName){
        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        //console.log(url);
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.newChannelNames.push(data[i]);
                }
            }
        });
    };
//(13)获取分类和活动（快拍）--------------------------------------------------------------------------------------------
    $scope.newPictureChannelNames=[];
    $scope.getNewPictureChannelNames=function(){
        var url=$scope.projectName+'/channel/kuaipai/channels';
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.newPictureChannelNames.push(data[i]);
                }
            }else{
                $scope.newPictureChannelNames=[];
            }
            //console.log($scope.newPictureChannelNames);
        });
    };
    $scope.getNewPictureChannelNames();
    //活动活动目录------------------------------------------------------------------------------------------------------
    $scope.newActivityNames=[];
    $scope.oldActivityNames=[];
    $scope.getNewActivityNames=function(){
        var url=$scope.projectName+'/channel/activities';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.oldActivityNames.push(data[i]);
                }
            }else{
                $scope.oldActivityNames=[];
            }
        });
//        console.log($scope.oldActivityNames);
        if($scope.oldActivityNames.length>0){
            $scope.newActivityNames=uniques($scope.oldActivityNames);
        }
    };
    $scope.getNewActivityNames();
}]);
