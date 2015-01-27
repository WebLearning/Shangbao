/**
 * Master Controller
 */
angular.module("Dashboard", ["ng.ueditor","tm.pagination"]).controller("MasterCtrl",["$scope","$http",function($scope,$http){
    //菜单的伸缩
    $scope.toggle=true;

    $scope.toggleSidebar = function()
    {
        $scope.toggle = ! $scope.toggle;
    };

    //IP和projectName的全局变量
    $scope.projectName="http://localhost:8080/Shangbao01";
    $scope.projectActionName="http://localhost:8080/Shangbao01/article/upload";
    $scope.projectPicActionName="http://localhost:8080/Shangbao01/picture/upload";

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
        //如果是点击新建文章就清除文章里的数据
        if(str=="文章/新建"){
            clearNewArticleData();
        }else if(str=="快拍成都/新建"){
            clearNewArticleData();
        }else if(str=="文章/爬虫文章"){
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
    $scope.formatDate=function(now){
        var   year=now.getFullYear();
        var   month=now.getMonth()+1;
        if(month<10){
            month="0"+month;
        }
        var   date=now.getDate();
        if(date<10){
            date="0"+date;
        }
        var   hour=now.getHours();
        if(hour<10){
            hour="0"+hour;
        }
        var   minute=now.getMinutes();
        if(minute<10){
            minute="0"+minute;
        }
        var   second=now.getSeconds();
        if(second<10){
            second="0"+second;
        }
        return   year+"-"+month+"-"+date+"   "+hour+":"+minute+":"+second;
    };
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
    //获取评论数据----------------------------------------------------------------------------------------------------------
    $scope.commentData=null;
    $scope.commentPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getCommentData($scope.commentPaginationConf.currentPage);
        }
    };
    $scope.getCommentData=function(pageID)
    {
        var url=$scope.projectName+'/commend/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.commentData=data;
            $scope.commentPageNums=getPageNums($scope.commentData.pageCount);
            $scope.lastCommentPage=$scope.commentData.pageCount;
            $scope.commentPaginationConf.currentPage=$scope.commentData.currentNo;
            $scope.getLastCommentPageData($scope.lastCommentPage);
        });
    };
    $scope.getLastCommentPageData=function(lastPage){
        var url=$scope.projectName+'/commend/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.latCommentPageData=data;
            $scope.lastCommentPageDataLength=$scope.latCommentPageData.commendList.length;
            $scope.commentPaginationConf.totalItems=(($scope.latCommentPageData.pageCount)-1)*20+$scope.lastCommentPageDataLength;
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
    $scope.crawlerPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getCrawlerData($scope.crawlerPaginationConf.currentPage);
        }
    };
    $scope.getCrawlerData=function(pageID)
    {
        var url=$scope.projectName+'/article/Crawler/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.crawlerData=data;
            $scope.crawlerPageNums=getPageNums($scope.crawlerData.pageCount);
            $scope.lastCrawlerPage=$scope.crawlerData.pageCount;
            $scope.crawlerPaginationConf.currentPage=$scope.crawlerData.currentNo;
            $scope.getLastCrawlerPageData($scope.lastCrawlerPage);
        });
    };
    $scope.getLastCrawlerPageData=function(lastPage){
        var url=$scope.projectName+'/article/Crawler/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastCrawlerPageData=data;
            $scope.lastCrawlerPageDataLength=$scope.lastCrawlerPageData.tileList.length;
            $scope.crawlerPaginationConf.totalItems=(($scope.lastCrawlerPageData.pageCount)-1)*20+$scope.lastCrawlerPageDataLength;
        });
    };
    $scope.getCrawlerData(1);//会在生成页面的时候直接运行!
    $scope.refreshCrawler=function()
    {
        $scope.orderCondition="";
        $scope.getCrawlerData(1);
    };
//(2)获取待审数据-------------------------------------------------------------------------------------------------------
    $scope.pendingData=null;
    $scope.pendingPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getPendingData($scope.pendingPaginationConf.currentPage);
        }
    };
    $scope.getPendingData=function(pageID){
        var url=$scope.projectName+'/article/Pending/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.pendingData=data;
            $scope.pendingPageNums=getPageNums($scope.pendingData.pageCount);
            $scope.lastPendingPage=$scope.pendingData.pageCount;
            $scope.pendingPaginationConf.currentPage=$scope.pendingData.currentNo;
            $scope.getLastPendingPageData($scope.lastPendingPage);
        });
    };
    $scope.getLastPendingPageData=function(lastPage){
        var url=$scope.projectName+'/article/Pending/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastPendingPageData=data;
            $scope.lastPendingPageDataLength=$scope.lastPendingPageData.tileList.length;
            $scope.pendingPaginationConf.totalItems=(($scope.lastPendingPageData.pageCount)-1)*20+$scope.lastPendingPageDataLength;
        });
    };
    $scope.getPendingData(1);//生成待审页面时即产生第一页数据
    $scope.refreshPending=function()
    {
        $scope.orderCondition="";
        $scope.getPendingData(1);
    };
//(3)获取已发布数据-----------------------------------------------------------------------------------------------------
    $scope.publishedData=null;
    $scope.publishedPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getPublishedData($scope.publishedPaginationConf.currentPage);
        }
    };
    $scope.getPublishedData=function(pageID){
        var url=$scope.projectName+'/article/Published/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.publishedData=data;
            $scope.publishedPageNums=getPageNums($scope.publishedData.pageCount);
            $scope.lastPublishedPage=$scope.publishedData.pageCount;
            $scope.publishedPaginationConf.currentPage=$scope.publishedData.currentNo;
            $scope.getLastPublishedPageData($scope.lastPublishedPage);
        });
    };
    $scope.getLastPublishedPageData=function(lastPage){
        var url=$scope.projectName+'/article/Published/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastPublishedPageData=data;
            $scope.lastPublishedPageDataLength=$scope.lastPublishedPageData.tileList.length;
            $scope.publishedPaginationConf.totalItems=(($scope.lastPublishedPageData.pageCount)-1)*20+$scope.lastPublishedPageDataLength;
        });
    };
    $scope.getPublishedData(1);//在点击已发布文章时，直接生成第一页内容
    $scope.refreshPublished=function()
    {
        $scope.orderCondition="";
        $scope.getPublishedData(1);
    };
//(4)获取已撤销数据-----------------------------------------------------------------------------------------------------
    $scope.revokedData=null;
    $scope.revokedPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getRevokedData($scope.revokedPaginationConf.currentPage);
        }
    };
    $scope.getRevokedData=function(pageID)
    {
        var url=$scope.projectName+'/article/Revocation/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.revokedData=data;
            $scope.revokedPageNums=getPageNums($scope.revokedData.pageCount);
            $scope.lastRevokedPage=$scope.revokedData.pageCount;
            $scope.revokedPaginationConf.currentPage=$scope.revokedData.currentNo;
            $scope.getLastRevokedPageData($scope.lastRevokedPage);
        });
    };
    $scope.getLastRevokedPageData=function(lastPage){
        var url=$scope.projectName+'/article/Revocation/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastRevokedPageData=data;
            $scope.lastRevokedPageDataLength=$scope.lastRevokedPageData.tileList.length;
            $scope.revokedPaginationConf.totalItems=(($scope.lastRevokedPageData.pageCount)-1)*20+$scope.lastRevokedPageDataLength;
        });
    };
    $scope.getRevokedData(1);//会在生成页面的时候直接运行!
    $scope.refreshRevoked=function()
    {
        $scope.orderCondition="";
        $scope.getRevokedData(1);
    };
//(5)获取草稿箱数据--------------------------------------------------------------------------------------------------------
    $scope.tempData=null;
    $scope.tempPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getTempData($scope.tempPaginationConf.currentPage);
        }
    };
    $scope.getTempData=function(pageID)
    {
        var url=$scope.projectName+'/article/Temp/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.tempData=data;
            $scope.tempPageNums=getPageNums($scope.tempData.pageCount);
            $scope.lastTempPage=$scope.tempData.pageCount;
            $scope.tempPaginationConf.currentPage=$scope.tempData.currentNo;
            $scope.getLastTempPageData($scope.lastTempPage);
        });
    };
    $scope.getLastTempPageData=function(lastPage){
        var url=$scope.projectName+'/article/Temp/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastTempPageData=data;
            $scope.lastTempPageDataLength=$scope.lastTempPageData.tileList.length;
            $scope.tempPaginationConf.totalItems=(($scope.lastTempPageData.pageCount)-1)*20+$scope.lastTempPageDataLength;
        });
    };
    $scope.getTempData(1);//会在生成页面的时候直接运行!
    $scope.refreshTemp=function()
    {
        $scope.orderCondition="";
        $scope.getTempData(1);
    };
//(6)获取一览数据-------------------------------------------------------------------------------------------------------
    $scope.newGeneralViewSections=null;
    $scope.getNewGeneralViewData=function()
    {
        var url=$scope.projectName+'/backapp/all';
        $http.get(url).success(function(data){
            $scope.newGeneralViewSections=data;
        });
    };
    $scope.getNewGeneralViewData();
    $scope.refreshGeneralView=function(){
        var url=$scope.projectName+'/backapp/refresh';
        $http.get(url).success(function(data){
            $scope.newGeneralViewSections=data;
        })
    };
//（7）获取快拍爬虫数据-------------------------------------------------------------------------------------------------
    $scope.crawlerPictureData=null;
    $scope.crawlerPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getCrawlerPictureData($scope.crawlerPicturePaginationConf.currentPage);
        }
    };
    $scope.getCrawlerPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Crawler/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.crawlerPictureData=data;
            $scope.crawlerPicturePageNums=getPageNums($scope.crawlerPictureData.pageCount);
            $scope.lastCrawlerPicturePage=$scope.crawlerPictureData.pageCount;
            $scope.crawlerPicturePaginationConf.currentPage=$scope.crawlerPictureData.currentNo;
            $scope.getLastCrawlerPicturePageData($scope.lastCrawlerPicturePage);
        });
    };
    $scope.getLastCrawlerPicturePageData=function(lastPage){
        var url=$scope.projectName+'/picture/Crawler/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastCrawlerPicturePageData=data;
            $scope.lastCrawlerPicturePageDataLength=$scope.lastCrawlerPicturePageData.tileList.length;
            $scope.crawlerPicturePaginationConf.totalItems=(($scope.lastCrawlerPicturePageData.pageCount)-1)*20+$scope.lastCrawlerPicturePageDataLength;
        });
    };
    $scope.getCrawlerPictureData(1);//会在生成页面的时候直接运行!
    $scope.refreshCrawlerPicture=function()
    {
        $scope.orderCondition="";
        $scope.getCrawlerPictureData(1);
    };
//(8)获取快拍待审数据---------------------------------------------------------------------------------------------------
    $scope.pendingPictureData=null;
    $scope.pendingPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getPendingPictureData($scope.pendingPicturePaginationConf.currentPage);
        }
    };
    $scope.getPendingPictureData=function(pageID){
        var url=$scope.projectName+'/picture/Pending/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.pendingPictureData=data;
            $scope.pendingPicturePageNums=getPageNums($scope.pendingPictureData.pageCount);
            $scope.lastPendingPicturePage=$scope.pendingPictureData.pageCount;
            $scope.pendingPicturePaginationConf.currentPage=$scope.pendingPictureData.currentNo;
            $scope.getLastPendingPicturePageData($scope.lastPendingPicturePage);
        });
    };
    $scope.getLastPendingPicturePageData=function(lastPage){
        var url=$scope.projectName+'/picture/Pending/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastPendingPicturePageData=data;
            $scope.lastPendingPicturePageDataLength=$scope.lastPendingPicturePageData.tileList.length;
            $scope.pendingPicturePaginationConf.totalItems=(($scope.lastPendingPicturePageData.pageCount)-1)*20+$scope.lastPendingPicturePageDataLength;
        });
    };
    $scope.getPendingPictureData(1);//生成待审页面时即产生第一页数据
    $scope.refreshPendingPicture=function()
    {
        $scope.orderCondition="";
        $scope.getPendingPictureData(1);
    };
//(9)获取快拍已发布-----------------------------------------------------------------------------------------------------
    $scope.publishedPictureData=null;
    $scope.publishedPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getPublishedPictureData($scope.publishedPicturePaginationConf.currentPage);
        }
    };
    $scope.getPublishedPictureData=function(pageID){
        var url=$scope.projectName+'/picture/Published/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.publishedPictureData=data;
            $scope.publishePicturePageNums=getPageNums($scope.publishedPictureData.pageCount);
            $scope.lastPublishedPicturePage=$scope.publishedPictureData.pageCount;
            $scope.publishedPicturePaginationConf.currentPage=$scope.publishedPictureData.currentNo;
            $scope.getLastPublishedPicturePageData($scope.lastPublishedPicturePage);
        });
    };
    $scope.getLastPublishedPicturePageData=function(lastPage){
        var url=$scope.projectName+'/picture/Published/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastPublishedPicturePageData=data;
            $scope.lastPublishedPicturePageDataLength=$scope.lastPublishedPicturePageData.tileList.length;
            $scope.publishedPicturePaginationConf.totalItems=(($scope.lastPublishedPicturePageData.pageCount)-1)*20+$scope.lastPublishedPicturePageDataLength;
        });
    };
    $scope.getPublishedPictureData(1);//在点击已发布文章时，直接生成第一页内容
    $scope.refreshPublishedPicture=function()
    {
        $scope.orderCondition="";
        console.log("test");
        $scope.getPublishedPictureData(1);
    };
//（10）获取快拍已撤销--------------------------------------------------------------------------------------------------
    $scope.revokedPictureData=null;
    $scope.revokedPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getRevokedPictureData($scope.revokedPicturePaginationConf.currentPage);
        }
    };
    $scope.getRevokedPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Revocation/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.revokedPictureData=data;
            $scope.revokedPicturePageNums=getPageNums($scope.revokedPictureData.pageCount);
            $scope.lastRevokedPicturePage=$scope.revokedPictureData.pageCount;
            $scope.revokedPicturePaginationConf.currentPage=$scope.revokedPictureData.currentNo;
            $scope.getLastRevokedPicturePageData($scope.lastRevokedPicturePage);
        });
    };
    $scope.getLastRevokedPicturePageData=function(lastPage){
        var url=$scope.projectName+'/picture/Revocation/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastRevokedPicturePageData=data;
            $scope.lastRevokedPicturePageDataLength=$scope.lastRevokedPicturePageData.tileList.length;
            $scope.revokedPicturePaginationConf.totalItems=(($scope.lastRevokedPicturePageData.pageCount)-1)*20+$scope.lastRevokedPicturePageDataLength;
        });
    };
    $scope.getRevokedPictureData(1);//会在生成页面的时候直接运行!
    $scope.refreshRevokedPicture=function()
    {
        $scope.orderCondition="";
        $scope.getRevokedPictureData(1);
    };
//（11）获取快拍草稿箱数据----------------------------------------------------------------------------------------------
    $scope.tempPictureData=null;
    $scope.tempPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            $scope.getTempPictureData($scope.tempPicturePaginationConf.currentPage);
        }
    };
    $scope.getTempPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Temp/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.tempPictureData=data;
            $scope.tempPicturePageNums=getPageNums($scope.tempPictureData.pageCount);
            $scope.lastTempPicturePage=$scope.tempPictureData.pageCount;
            $scope.tempPicturePaginationConf.currentPage=$scope.tempPictureData.currentNo;
            $scope.getLastTempPicturePageData($scope.lastTempPicturePage);
        });
    };
    $scope.getLastTempPicturePageData=function(lastPage){
        var url=$scope.projectName+'/picture/Temp/'+lastPage+$scope.orderCondition;
        $http.get(url).success(function(data){
            $scope.lastTempPicturePageData=data;
            $scope.lastTempPicturePageDataLength=$scope.lastTempPicturePageData.tileList.length;
            $scope.tempPicturePaginationConf.totalItems=(($scope.lastTempPicturePageData.pageCount)-1)*20+$scope.lastTempPicturePageDataLength;
        });
    };
    $scope.getTempPictureData(1);//会在生成页面的时候直接运行!

    $scope.refreshTempPicture=function()
    {
        $scope.orderCondition="";
        $scope.getTempPictureData(1);
    };
//(12)获取分类(文章）----------------------------------------------------------------------------------------------------------
    $scope.newChannelNames=[];
    $scope.getNewChannelNames=function(){
        var url=$scope.projectName+'/channel/channels';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.checkFirstChannel(data[i]);
                }
            }else{
                $scope.newChannelNames=[];
            }
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
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.newPictureChannelNames.push(data[i]);
                }
            }else{
                $scope.newPictureChannelNames=[];
            }
        });
    };
    $scope.getNewPictureChannelNames();
}]);
