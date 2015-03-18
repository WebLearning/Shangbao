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
    $scope.addAppPicActionName="http://localhost:8080/Shangbao01/channel/uploadstartpicture";

    $scope.articleData={
        activity:"" ,
        author: "",
        channel: [],
        channelIndex: null,
        clicks: null,
        content: "",
        crawlerCommends: null,
        crawlerCommendsPublish: null,
        crawlerCommendsUnpublish:null,
        from: "",
        id: null,
        keyWord: [],
        level: null,
        likes: null,
        newsCommends: null,
        newsCommendsPublish: null,
        newsCommendsUnpublish:null,
        picturesUrl: [],
        logs:[],
        subTitle: "",
        summary: "",
        state:null,
        tag: null,
        time: "",
        title: "",
        outSideUrl:"",
        titlePicUrl: null,
        js_clicks:null,
        words: null,
        pictures:null
//        logs:[]
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
        outSideUrl:"",
        titlePicUrl: null,
        words: null,
        pictures:null
    };
    //显示当前登录用户名------------------------------------------------------------------------------------------------
    $scope.userInfo_name="";
    $scope.userInfo_duty="";
    $scope.getCurUserName=function(){
        var url=$scope.projectName+"/user/userinfo";
        $http.get(url).success(function(data){
            console.log(data.name);
            $scope.userInfo_name=data.name;
            $scope.userInfo_duty=data.duty;
            console.log($scope.userInfo_duty);
            $scope.getPendingData(1);
        });
    };
    $scope.getCurUserName();
    //获得所有大的分类（duty）------------------------------------------------------------------------------------------
    $scope.userDuty="";
    $scope.getUserDuty=function(){
        var url=$scope.projectName+"/channel/channels";
        $http.get(url).success(function(data){
            $scope.userDuty=data;
        });
    };

    $scope.getUserDuty();
    //退出登录----------------------------------------------------------------------------------------------------------
    $scope.exitLog=function(){
        var url=$scope.projectName+"/j_spring_security_logout";
        if(confirm("确认退出?")==true){
            $http.get(url).success(function(){
                top.location="../../login.jsp";
            });
        }
    };
    //初始化header
    $scope.curPage = "一览";

    //点击sidebar之后改变header
    $scope.changeCurPage = function(str)
    {
        $scope.curPage=str;
        $scope.refreshChannelNames();
        $scope.getNewPictureChannelNames();
        $scope.getNewChannelNames();
        clearArticleData();
        $scope.getSetState();
        $scope.getCommentSetState();
        $scope.setButtonInNewArticleForPending();
        $scope.setButtonInNewArticleForPublish();
        //如果是点击新建文章就清除文章里的数据
        if(str=="新建"){
            clearNewArticleData();
            $scope.newArticleData.time=new Date();
        }else if(str=="爬虫"){
            clearCrawlerSearchData();
            $scope.refreshCrawler();
        }else if(str=="文章/新建"){
            clearNewArticleData();
            $scope.newArticleData.time=new Date();
//            $scope.setButtonInNewArticleForPending();
//            $scope.setButtonInNewArticleForPublish();
        }else if(str=="快拍成都/新建"){
            clearNewArticleData();
            $scope.newArticleData.time=new Date();
//            $scope.setButtonInNewArticleForPending();
//            $scope.setButtonInNewArticleForPublish();
        }else if(str=="文章/爬虫文章"){
            clearCrawlerSearchData();
            $scope.refreshCrawler();
        }else if(str=="文章/待审"){
            clearPendingSearchData();
            $scope.refreshPending();
        }else if(str=="文章/已发布"){
            clearPublishedSearchData();
            $scope.refreshPublished();
        }else if(str=="文章/已撤销"){
            clearRevokedSearchData();
            $scope.refreshRevoked();
        }else if(str=="文章/草稿箱"){
            clearTempSearchData();
            $scope.refreshTemp();
        }else if(str=="一览"){
            $scope.refreshGeneralView();
        }else if(str=="快拍成都/爬虫图片"){
            clearCrawlerPitureSearchData();
            $scope.refreshCrawlerPicture();
        }else if(str=="快拍成都/待审"){
            clearPendingPictureSearchData();
            $scope.refreshPendingPicture();
        }else if(str=="快拍成都/已发布"){
            clearPublishedPictureSearchData();
            $scope.refreshPublishedPicture();
        }else if(str=="快拍成都/已撤销"){
            clearRevokedPictureSearchData();
            $scope.refreshRevokedPicture();
        }else if(str=="快拍成都/草稿箱"){
            clearTempPictureSearchData();
            $scope.refreshTempPicture();
        }else if(str=="评论"){
            $scope.refreshCommentCur();
        }else if(str=="分类1/待审"){
            clearPendingSearchData_type1();
            $scope.refreshPending_type1();
        }else if(str=="分类1/已发布"){

        }else if(str=="分类1/已撤销"){

        }else if(str=="分类1/草稿箱"){

        }
    };

    function clearArticleData(){
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"||p=="logs"){
                $scope.articleData[p]=[];
            }else if(p=="words"){
                $scope.articleData[p]=0;
            }else if(p=="author"||p=="title"||p=="content"||p=="from"||p=="subTitle"||p=="summary"||p=="time"||p=="activity"||p=="outSideUrl"){
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
            }else if(p=="author"||p=="title"||p=="content"||p=="from"||p=="subTitle"||p=="summary"||p=="time"||p=="outSideUrl"){
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
    $scope.commentDetailsUrlFor="";
    $scope.commentDetailTitle="";

//全局共用数据----------------------------------------------------------------------------------------------------------
    $scope.orderCondition="/time/desc";
    $scope.transOrderConditions=function(str){
        $scope.orderCondition=str;
        console.log($scope.orderCondition);
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
            if($scope.commentPaginationConf.currentPage>0){
                $scope.getCommentData($scope.commentPaginationConf.currentPage);
            }
        }
    };
    $scope.getCommentData=function(pageID)
    {
        var url=$scope.projectName+'/commend/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.commentData=data;
                $scope.commentPageNums=getPageNums($scope.commentData.pageCount);
                $scope.lastCommentPage=$scope.commentData.pageCount;
                $scope.commentPaginationConf.currentPage=$scope.commentData.currentNo;
                $scope.getLastCommentPageData($scope.lastCommentPage);
            }else{
                $scope.commentData=data;
                $scope.commentPaginationConf.currentPage=0;
                $scope.commentPaginationConf.totalItems=0;
            }
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
        $scope.orderCondition="/time/desc";
        $scope.getCommentData($scope.commentData.currentNo);
    };
    $scope.refreshCommentCur=function(){
        $scope.orderCondition="/time/desc";
        document.getElementById("comment").className="tab-pane active";
        document.getElementById("commentDetails").className="tab-pane";
        $scope.getCommentData(1);
    };
    $scope.goCommentList=function()
    {
        document.getElementById("comment").className="tab-pane active";
        document.getElementById("commentDetails").className="tab-pane";
//        $scope.refreshComment();
        $scope.getCommentData($scope.commentData.currentNo);
    };
//（1）获取爬虫数据-----------------------------------------------------------------------------------------------------
    $scope.crawlerData=null;
    $scope.crawlerSearchData={
        content:""
    };
    $scope.crawlerPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.crawlerPaginationConf.currentPage>0){
                if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
                    $scope.getCrawlerData($scope.crawlerPaginationConf.currentPage);
                }else{
                    $scope.getCrawlerSearchData($scope.crawlerPaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getCrawlerData=function(pageID)
    {
        var url=$scope.projectName+'/article/Crawler/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.crawlerData=data;
                $scope.crawlerPageNums=getPageNums($scope.crawlerData.pageCount);
                $scope.lastCrawlerPage=$scope.crawlerData.pageCount;
                $scope.crawlerPaginationConf.currentPage=$scope.crawlerData.currentNo;
                $scope.getLastCrawlerPageData($scope.lastCrawlerPage);
            }else{
                $scope.crawlerData=data;
                $scope.crawlerPaginationConf.currentPage=0;
                $scope.crawlerPaginationConf.totalItems=0;
            }
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
    function clearCrawlerSearchData(){
        for(p in $scope.crawlerSearchData){
            $scope.crawlerSearchData[p]="";
        }
    }
    $scope.refreshCrawler=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData(1);
        }else{
            $scope.getCrawlerSearchData(1);
        }
    };
    $scope.refreshCrawlerCur=function()
    {
//        $scope.orderCondition="";
        if($scope.crawlerSearchData.content==""||$scope.crawlerSearchData.content==null){
            $scope.getCrawlerData($scope.crawlerData.currentNo);
        }else{
            $scope.getCrawlerSearchData($scope.crawlerData.currentNo);
        }
    };
    //(a)搜索爬虫-------------------------------------------------------------------------------------------------------
    $scope.getCrawlerSearchData=function(pageID){
        var url=$scope.projectName+'/article/Crawler/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.crawlerSearchData);
        $http.post(url,$scope.crawlerSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.crawlerData=data;
                $scope.crawlerPageNums=getPageNums($scope.crawlerData.pageCount);
                $scope.lastCrawlerPage=$scope.crawlerData.pageCount;
                $scope.crawlerPaginationConf.currentPage=$scope.crawlerData.currentNo;
                $scope.getLastCrawlerSearchPageData($scope.lastCrawlerPage);
            }else{
                $scope.crawlerData=data;
                $scope.crawlerPaginationConf.currentPage=0;
                $scope.crawlerPaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastCrawlerSearchPageData=function(lastPage){
        var url=$scope.projectName+'/article/Crawler/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.crawlerSearchData).success(function(data){
            $scope.lastCrawlerPageData=data;
            $scope.lastCrawlerPageDataLength=$scope.lastCrawlerPageData.tileList.length;
            $scope.crawlerPaginationConf.totalItems=(($scope.lastCrawlerPageData.pageCount)-1)*20+$scope.lastCrawlerPageDataLength;
        });
    };
//(2)获取待审数据-------------------------------------------------------------------------------------------------------
    $scope.pendingData=null;
    $scope.pendingSearchData={
        content:""
    };
    $scope.pendingPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.pendingPaginationConf.currentPage>0){
                if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
                    $scope.getPendingData($scope.pendingPaginationConf.currentPage);
                }else{
                    $scope.getPendingSearchData($scope.pendingPaginationConf.currentPage);
                }
            }
        }
    };
    $scope.urlForPending="";
    $scope.getPendingData=function(pageID){
//        $scope.getCurUserName();
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPending=$scope.projectName+'/article/Pending/'+pageID.toString()+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPending=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Pending/'+pageID.toString()+$scope.orderCondition;
            }
            $http.get($scope.urlForPending).success(function(data){
                if(data.pageCount>0){
                    $scope.pendingData=data;
                    $scope.pendingPageNums=getPageNums($scope.pendingData.pageCount);
                    $scope.lastPendingPage=$scope.pendingData.pageCount;
                    $scope.pendingPaginationConf.currentPage=$scope.pendingData.currentNo;
                    $scope.getLastPendingPageData($scope.lastPendingPage);
                }else{
                    $scope.pendingData=data;
                    $scope.pendingPaginationConf.currentPage=0;
                    $scope.pendingPaginationConf.totalItems=0;
                }
            });
        }else{
            console.log("url错误");
        }
    };
    $scope.urlForPendingLastPage="";
    $scope.getLastPendingPageData=function(lastPage){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPendingLastPage=$scope.projectName+'/article/Pending/'+lastPage+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPendingLastPage=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Pending/'+lastPage+$scope.orderCondition;
            }
            $http.get($scope.urlForPendingLastPage).success(function(data){
                $scope.lastPendingPageData=data;
                $scope.lastPendingPageDataLength=$scope.lastPendingPageData.tileList.length;
                $scope.pendingPaginationConf.totalItems=(($scope.lastPendingPageData.pageCount)-1)*20+$scope.lastPendingPageDataLength;
            });
        }else{
            console.log("url错误");
        }
    };
//    $scope.getPendingData(1);//生成待审页面时即产生第一页数据
    function clearPendingSearchData(){
        for(p in $scope.pendingSearchData){
            $scope.pendingSearchData[p]="";
        }
    }
    $scope.refreshPending=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData(1);
        }else{
            $scope.getPendingSearchData(1);
        }
    };
    $scope.refreshPendingCur=function()
    {
//        $scope.orderCondition="";
        if($scope.pendingSearchData.content==""||$scope.pendingSearchData.content==null){
            $scope.getPendingData($scope.pendingData.currentNo);
        }else{
            $scope.getPendingSearchData($scope.pendingData.currentNo);
        }
    };
    //(b)搜索待审数据---------------------------------------------------------------------------------------------------
    $scope.urlForPendingSearch="";
    $scope.getPendingSearchData=function(pageID){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPendingSearch=$scope.projectName+'/article/Pending/'+pageID.toString()+'/query'+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPendingSearch=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Pending/'+pageID.toString()+'/query'+$scope.orderCondition;
            }
            console.log($scope.pendingSearchData);
            $http.post($scope.urlForPendingSearch,$scope.pendingSearchData).success(function(data){
                console.log(data);
                if(data.pageCount>0){
                    $scope.pendingData=data;
                    $scope.pendingPageNums=getPageNums($scope.pendingData.pageCount);
                    $scope.lastPendingPage=$scope.pendingData.pageCount;
                    $scope.pendingPaginationConf.currentPage=$scope.pendingData.currentNo;
                    $scope.getLastPendingSearchPageData($scope.lastPendingPage);
                }else{
                    $scope.pendingData=data;
                    $scope.pendingPaginationConf.currentPage=0;
                    $scope.pendingPaginationConf.totalItems=0;
                }
            });
        }else{
            console.log("url错误！");
        }
    };
    $scope.urlForPendingSearchLastpage="";
    $scope.getLastPendingSearchPageData=function(lastPage){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPendingSearchLastpage=$scope.projectName+'/article/Pending/'+lastPage+'/query'+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPendingSearchLastpage=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Pending/'+lastPage+'/query'+$scope.orderCondition;
            }
            $http.post($scope.urlForPendingSearchLastpage,$scope.pendingSearchData).success(function(data){
                $scope.lastPendingPageData=data;
                $scope.lastPendingPageDataLength=$scope.lastPendingPageData.tileList.length;
                $scope.pendingPaginationConf.totalItems=(($scope.lastPendingPageData.pageCount)-1)*20+$scope.lastPendingPageDataLength;
            });
        }else{
            console.log("url错误");
        }
    };

//(3)获取已发布数据-----------------------------------------------------------------------------------------------------
    $scope.publishedData=null;
    $scope.publishedSearchData={
        content:""
    };
    $scope.publishedPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.publishedPaginationConf.currentPage>0){
                if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
                    $scope.getPublishedData($scope.publishedPaginationConf.currentPage);
                }else{
                    $scope.getPublishedSearchData($scope.publishedPaginationConf.currentPage);
                }
            }
        }
    };
    $scope.urlForPublished="";
    $scope.getPublishedData=function(pageID){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPublished=$scope.projectName+'/article/Published/'+pageID.toString()+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPublished=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Published/'+pageID.toString()+$scope.orderCondition;
            }
            console.log($scope.urlForPublished);
            $http.get($scope.urlForPublished).success(function(data){
                if(data.pageCount>0){
                    $scope.publishedData=data;
                    $scope.publishedPageNums=getPageNums($scope.publishedData.pageCount);
                    $scope.lastPublishedPage=$scope.publishedData.pageCount;
                    $scope.publishedPaginationConf.currentPage=$scope.publishedData.currentNo;
                    $scope.getLastPublishedPageData($scope.lastPublishedPage);
                }else{
                    $scope.publishedData=data;
                    $scope.publishedPaginationConf.currentPage=0;
                    $scope.publishedPaginationConf.totalItems=0;
                }
            });
        }else{
            console.log("url错误");
        }
    };
    $scope.urlForPublishedLastPage="";
    $scope.getLastPublishedPageData=function(lastPage){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPublishedLastPage=$scope.projectName+'/article/Published/'+lastPage+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPublishedLastPage=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Published/'+lastPage+$scope.orderCondition;
            }
            $http.get($scope.urlForPublishedLastPage).success(function(data){
                $scope.lastPublishedPageData=data;
                $scope.lastPublishedPageDataLength=$scope.lastPublishedPageData.tileList.length;
                $scope.publishedPaginationConf.totalItems=(($scope.lastPublishedPageData.pageCount)-1)*20+$scope.lastPublishedPageDataLength;
            });
        }else{
            console.log("url错误");
        }
    };
    $scope.getPublishedData(1);//在点击已发布文章时，直接生成第一页内容
    function clearPublishedSearchData(){
        for(p in $scope.publishedSearchData){
            $scope.publishedSearchData[p]="";
        }
    }
    $scope.refreshPublished=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData(1);
        }else{
            $scope.getPublishedSearchData(1);
        }
    };
    $scope.refreshPublishedCur=function()
    {
        if($scope.publishedSearchData.content==""||$scope.publishedSearchData.content==null){
            $scope.getPublishedData($scope.publishedData.currentNo);
        }else{
            $scope.getPublishedSearchData($scope.publishedData.currentNo);
        }
    };
    //(c)搜索已发布数据-------------------------------------------------------------------------------------------------
    $scope.urlForPublishedSearch="";
    $scope.getPublishedSearchData=function(pageID){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPublishedSearch=$scope.projectName+'/article/Published/'+pageID.toString()+'/query'+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPublishedSearch=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Published/'+pageID.toString()+'/query'+$scope.orderCondition;
            }
            console.log($scope.publishedSearchData);
            $http.post($scope.urlForPublishedSearch,$scope.publishedSearchData).success(function(data){
                console.log(data);
                if(data.pageCount>0){
                    $scope.publishedData=data;
                    $scope.publishedPageNums=getPageNums($scope.publishedData.pageCount);
                    $scope.lastPublishedPage=$scope.publishedData.pageCount;
                    $scope.publishedPaginationConf.currentPage=$scope.publishedData.currentNo;
                    $scope.getLastPublishedSearchPageData($scope.lastPublishedPage);
                }else{
                    $scope.publishedData=data;
                    $scope.publishedPaginationConf.currentPage=0;
                    $scope.publishedPaginationConf.totalItems=0;
                }
            });
        }else{
            console.log("url错误");
        }
    };
    $scope.urlForPublishedSearchLastPage="";
    $scope.getLastPublishedSearchPageData=function(lastPage){
        if($scope.userInfo_duty!=""){
            if($scope.userInfo_duty=="super"){
                $scope.urlForPublishedSearchLastPage=$scope.projectName+'/article/Published/'+lastPage+'/query'+$scope.orderCondition;
            }else if((($scope.userInfo_duty)!="kuaipai")&&(($scope.userInfo_duty)!="super")){
                $scope.urlForPublishedSearchLastPage=$scope.projectName+'/article/channel/'+$scope.userInfo_duty+'/Published/'+lastPage+'/query'+$scope.orderCondition;
            }
            $http.post($scope.urlForPublishedSearchLastPage,$scope.publishedSearchData).success(function(data){
                $scope.lastPublishedPageData=data;
                $scope.lastPublishedPageDataLength=$scope.lastPublishedPageData.tileList.length;
                $scope.publishedPaginationConf.totalItems=(($scope.lastPublishedPageData.pageCount)-1)*20+$scope.lastPublishedPageDataLength;
            });
        }else{
            console.log("url错误");
        }
    };
//(4)获取已撤销数据-----------------------------------------------------------------------------------------------------
    $scope.revokedData=null;
    $scope.revokedSearchData={
        content:""
    };
    $scope.revokedPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.revokedPaginationConf.currentPage>0){
                if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
                    $scope.getRevokedData($scope.revokedPaginationConf.currentPage);
                }else{
                    $scope.getRevokedSearchData($scope.revokedPaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getRevokedData=function(pageID)
    {
        var url=$scope.projectName+'/article/Revocation/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.revokedData=data;
                $scope.revokedPageNums=getPageNums($scope.revokedData.pageCount);
                $scope.lastRevokedPage=$scope.revokedData.pageCount;
                $scope.revokedPaginationConf.currentPage=$scope.revokedData.currentNo;
                $scope.getLastRevokedPageData($scope.lastRevokedPage);
            }else{
                $scope.revokedData=data;
                $scope.revokedPaginationConf.currentPage=0;
                $scope.revokedPaginationConf.totalItems=0;
            }
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
    function clearRevokedSearchData(){
        for(p in $scope.revokedSearchData){
            $scope.revokedSearchData[p]="";
        }
    }
    $scope.refreshRevoked=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData(1);
        }else{
            $scope.getRevokedSearchData(1);
        }
    };
    $scope.refreshRevokedCur=function()
    {
//        $scope.orderCondition="";
        if($scope.revokedSearchData.content==""||$scope.revokedSearchData.content==null){
            $scope.getRevokedData($scope.revokedData.currentNo);
        }else{
            $scope.getRevokedSearchData($scope.revokedData.currentNo);
        }
    };
    //(d)搜索已撤销数据-------------------------------------------------------------------------------------------------
    $scope.getRevokedSearchData=function(pageID){
        var url=$scope.projectName+'/article/Revocation/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log($scope.revokedSearchData);
        $http.post(url,$scope.revokedSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.revokedData=data;
                $scope.revokedPageNums=getPageNums($scope.revokedData.pageCount);
                $scope.lastRevokedPage=$scope.revokedData.pageCount;
                $scope.revokedPaginationConf.currentPage=$scope.revokedData.currentNo;
                $scope.getLastRevokedSearchPageData($scope.lastRevokedPage);
            }else{
                $scope.revokedData=data;
                $scope.revokedPaginationConf.currentPage=0;
                $scope.revokedPaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastRevokedSearchPageData=function(lastPage){
        var url=$scope.projectName+'/article/Revocation/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.revokedSearchData).success(function(data){
            $scope.lastRevokedPageData=data;
            $scope.lastRevokedPageDataLength=$scope.lastRevokedPageData.tileList.length;
            $scope.revokedPaginationConf.totalItems=(($scope.lastRevokedPageData.pageCount)-1)*20+$scope.lastRevokedPageDataLength;
        });
    };
//(5)获取草稿箱数据--------------------------------------------------------------------------------------------------------
    $scope.tempData=null;
    $scope.tempSearchData={
        content:""
    };
    $scope.tempPaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.tempPaginationConf.currentPage>0){
                if($scope.tempSearchData.content==""||$scope.tempSearchData.content==null){
                    $scope.getTempData($scope.tempPaginationConf.currentPage);
                }else{
                    $scope.getTempSearchData($scope.tempPaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getTempData=function(pageID)
    {
        var url=$scope.projectName+'/article/Temp/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.tempData=data;
                $scope.tempPageNums=getPageNums($scope.tempData.pageCount);
                $scope.lastTempPage=$scope.tempData.pageCount;
                $scope.tempPaginationConf.currentPage=$scope.tempData.currentNo;
                $scope.getLastTempPageData($scope.lastTempPage);
            }else{
                $scope.tempData=data;
                $scope.tempPaginationConf.currentPage=0;
                $scope.tempPaginationConf.totalItems=0;
            }
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
    function clearTempSearchData(){
        for(p in $scope.tempSearchData){
            $scope.tempSearchData[p]="";
        }
    }
    $scope.refreshTemp=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.tempSearchData.content==""||$scope.tempSearchData.content==null){
            $scope.getTempData(1);
        }else{
            $scope.getTempSearchData(1);
        }
    };
    $scope.refreshTempCur=function()
    {
//        $scope.orderCondition="";
        if($scope.tempSearchData.content==""||$scope.tempSearchData.content==null){
            $scope.getTempData($scope.tempData.currentNo);
        }else{
            $scope.getTempSearchData($scope.tempData.currentNo);
        }
    };
    //(e)搜索草稿数据---------------------------------------------------------------------------------------------------
    $scope.getTempSearchData=function(pageID){
        var url=$scope.projectName+'/article/Temp/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.tempSearchData);
        $http.post(url,$scope.tempSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.tempData=data;
                $scope.tempPageNums=getPageNums($scope.tempData.pageCount);
                $scope.lastTempPage=$scope.tempData.pageCount;
                $scope.tempPaginationConf.currentPage=$scope.tempData.currentNo;
                $scope.getLastTempSearchPageData($scope.lastTempPage);
            }else{
                $scope.tempData=data;
                $scope.tempPaginationConf.currentPage=0;
                $scope.tempPaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastTempSearchPageData=function(lastPage){
        var url=$scope.projectName+'/article/Temp/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.tempSearchData).success(function(data){
            $scope.lastTempPageData=data;
            $scope.lastTempPageDataLength=$scope.lastTempPageData.tileList.length;
            $scope.tempPaginationConf.totalItems=(($scope.lastTempPageData.pageCount)-1)*20+$scope.lastTempPageDataLength;
        });
    };
//(6)获取一览数据-------------------------------------------------------------------------------------------------------
    $scope.newGeneralViewSections=null;
    $scope.newGeneralViews=null;
    $scope.getNewGeneralViewData=function()
    {
        var url=$scope.projectName+'/backapp/all';
        $http.get(url).success(function(data) {
//            $scope.newGeneralViewSections=data;

            $scope.newGeneralViews = data;
            $scope.generalArray = [];

            for (var j = 0; j < data.length; j++) {
                if ((($scope.newGeneralViews)[j].content).length > 20) {
                    for (var i = 0; i < 20; i++) {
                        $scope.generalArray[i] = (($scope.newGeneralViews)[j].content)[i];
//                    console.log(($scope.newGeneralViews)[j].content.title);
                    }
                    ($scope.newGeneralViews)[j].content = $scope.generalArray;
                    $scope.generalArray=[];
                } else {
                    for (var k = 0; k < (($scope.newGeneralViews)[j].content).length; k++) {
                        $scope.generalArray[k] = (($scope.newGeneralViews)[j].content)[k];
//                    console.log(($scope.newGeneralViews)[j].content.title);
                    }
                    ($scope.newGeneralViews)[j].content = $scope.generalArray;
                    $scope.generalArray=[];
                }
//                ($scope.newGeneralViews)[j].content=$scope.generalArray;
            }
//            console.log(($scope.newGeneralViews)[0]);
            $scope.newGeneralViewSections=$scope.newGeneralViews;
//            console.log($scope.newGeneralViewSections);
        });
    };
    $scope.getNewGeneralViewData();
    $scope.refreshGeneralView=function(){
        var url=$scope.projectName+'/backapp/refresh';
        $http.get(url).success(function(data){
            $scope.newGeneralViewSections=data;
            console.log($scope.newGeneralViewSections);
        })
    };
//（7）获取快拍爬虫数据-------------------------------------------------------------------------------------------------
    $scope.crawlerPictureData=null;
    $scope.crawlerPictureSearchData={
        content:""
    };
    $scope.crawlerPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.crawlerPicturePaginationConf.currentPage>0){
                if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
                    $scope.getCrawlerPictureData($scope.crawlerPicturePaginationConf.currentPage);
                }else{
                    $scope.getCrawlerPictureSearchData($scope.crawlerPicturePaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getCrawlerPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Crawler/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.crawlerPictureData=data;
                $scope.crawlerPicturePageNums=getPageNums($scope.crawlerPictureData.pageCount);
                $scope.lastCrawlerPicturePage=$scope.crawlerPictureData.pageCount;
                $scope.crawlerPicturePaginationConf.currentPage=$scope.crawlerPictureData.currentNo;
                $scope.getLastCrawlerPicturePageData($scope.lastCrawlerPicturePage);
            }else{
                $scope.crawlerPictureData=data;
                $scope.crawlerPicturePaginationConf.currentPage=0;
                $scope.crawlerPicturePaginationConf.totalItems=0;
            }

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
    function clearCrawlerPitureSearchData(){
        for(p in $scope.crawlerPictureSearchData){
            $scope.crawlerPictureSearchData[p]="";
        }
    }
    $scope.refreshCrawlerPicture=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData(1);
        }else{
            $scope.getCrawlerPictureSearchData(1);
        }
    };
    $scope.refreshCrawlerPictureCur=function()
    {
        if($scope.crawlerPictureSearchData.content==""||$scope.crawlerPictureSearchData.content==null){
            $scope.getCrawlerPictureData($scope.crawlerPictureData.currentNo);
        }else{
            $scope.getCrawlerPictureSearchData($scope.crawlerPictureData.currentNo);
        }
    };
    //(f)搜索快拍爬虫数据--------------------------------------------------------------------------------------------------
    $scope.getCrawlerPictureSearchData=function(pageID){
        var url=$scope.projectName+'/picture/Crawler/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.crawlerPictureSearchData);
        $http.post(url,$scope.crawlerPictureSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.crawlerPictureData=data;
                $scope.crawlerPicturePageNums=getPageNums($scope.crawlerPictureData.pageCount);
                $scope.lastCrawlerPicturePage=$scope.crawlerPictureData.pageCount;
                $scope.crawlerPicturePaginationConf.currentPage=$scope.crawlerPictureData.currentNo;
                $scope.getLastCrawlerPictureSearchPageData($scope.lastCrawlerPicturePage);
            }else{
                $scope.crawlerPictureData=data;
                $scope.crawlerPicturePaginationConf.currentPage=0;
                $scope.crawlerPicturePaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastCrawlerPictureSearchPageData=function(lastPage){
        var url=$scope.projectName+'/picture/Crawler/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.crawlerPictureSearchData).success(function(data){
            $scope.lastCrawlerPicturePageData=data;
            $scope.lastCrawlerPicturePageDataLength=$scope.lastCrawlerPicturePageData.tileList.length;
            $scope.crawlerPicturePaginationConf.totalItems=(($scope.lastCrawlerPicturePageData.pageCount)-1)*20+$scope.lastCrawlerPicturePageDataLength;
        });
    };
//(8)获取快拍待审数据---------------------------------------------------------------------------------------------------
    $scope.pendingPictureData=null;
    $scope.pendingPictureSearchData={
        content:""
    };
    $scope.pendingPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.pendingPicturePaginationConf.currentPage>0){
                if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
                    $scope.getPendingPictureData($scope.pendingPicturePaginationConf.currentPage);
                }else{
                    $scope.getPendingPictureSearchData($scope.pendingPicturePaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getPendingPictureData=function(pageID){
        var url=$scope.projectName+'/picture/Pending/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.pendingPictureData=data;
                $scope.pendingPicturePageNums=getPageNums($scope.pendingPictureData.pageCount);
                $scope.lastPendingPicturePage=$scope.pendingPictureData.pageCount;
                $scope.pendingPicturePaginationConf.currentPage=$scope.pendingPictureData.currentNo;
                $scope.getLastPendingPicturePageData($scope.lastPendingPicturePage);
            }else{
                $scope.pendingPictureData=data;
                $scope.pendingPicturePaginationConf.currentPage=0;
                $scope.pendingPicturePaginationConf.totalItems=0;
            }
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
    function clearPendingPictureSearchData(){
        for(p in $scope.pendingPictureSearchData){
            $scope.pendingPictureSearchData[p]="";
        }
    }
    $scope.refreshPendingPicture=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData(1);
        }else{
            $scope.getPendingPictureSearchData(1);
        }
    };
    $scope.refreshPendingPictureCur=function()
    {
//        $scope.orderCondition="";
        if($scope.pendingPictureSearchData.content==""||$scope.pendingPictureSearchData.content==null){
            $scope.getPendingPictureData($scope.pendingPictureData.currentNo);
        }else{
            $scope.getPendingPictureSearchData($scope.pendingPictureData.currentNo);
        }
    };
    //搜索快拍待审数据--------------------------------------------------------------------------------------------------
    $scope.getPendingPictureSearchData=function(pageID){
        var url=$scope.projectName+'/picture/Pending/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.pendingPictureSearchData);
        $http.post(url,$scope.pendingPictureSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.pendingPictureData=data;
                $scope.pendingPicturePageNums=getPageNums($scope.pendingPictureData.pageCount);
                $scope.lastPendingPicturePage=$scope.pendingPictureData.pageCount;
                $scope.pendingPicturePaginationConf.currentPage=$scope.pendingPictureData.currentNo;
                $scope.getLastPendingPictureSearchPageData($scope.lastPendingPicturePage);
            }else{
                $scope.pendingPictureData=data;
                $scope.pendingPicturePaginationConf.currentPage=0;
                $scope.pendingPicturePaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastPendingPictureSearchPageData=function(lastPage){
        var url=$scope.projectName+'/picture/Pending/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.pendingPictureSearchData).success(function(data){
            $scope.lastPendingPicturePageData=data;
            $scope.lastPendingPicturePageDataLength=$scope.lastPendingPicturePageData.tileList.length;
            $scope.pendingPicturePaginationConf.totalItems=(($scope.lastPendingPicturePageData.pageCount)-1)*20+$scope.lastPendingPicturePageDataLength;
        });
    };
//(9)获取快拍已发布-----------------------------------------------------------------------------------------------------
    $scope.publishedPictureData=null;
    $scope.publishedPictureSearchData={
        content:""
    };
    $scope.publishedPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.publishedPicturePaginationConf.currentPage>0){
                if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
                    $scope.getPublishedPictureData($scope.publishedPicturePaginationConf.currentPage);
                }else{
                    $scope.getPublishedPictureSearchData($scope.publishedPicturePaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getPublishedPictureData=function(pageID){
        var url=$scope.projectName+'/picture/Published/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.publishedPictureData=data;
                $scope.publishePicturePageNums=getPageNums($scope.publishedPictureData.pageCount);
                $scope.lastPublishedPicturePage=$scope.publishedPictureData.pageCount;
                $scope.publishedPicturePaginationConf.currentPage=$scope.publishedPictureData.currentNo;
                $scope.getLastPublishedPicturePageData($scope.lastPublishedPicturePage);
            }else{
                $scope.publishedPictureData=data;
                $scope.publishedPicturePaginationConf.currentPage=0;
                $scope.publishedPicturePaginationConf.totalItems=0;
            }
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
    function clearPublishedPictureSearchData(){
        for(p in $scope.publishedPictureSearchData){
            $scope.publishedPictureSearchData[p]="";
        }
    }
    $scope.refreshPublishedPicture=function()
    {
        $scope.orderCondition="/time/desc";
        console.log("test");
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData(1);
        }else{
            $scope.getPublishedPictureSearchData(1);
        }
    };
    $scope.refreshPublishedPictureCur=function()
    {
//        $scope.orderCondition="";
        console.log("test");
        if($scope.publishedPictureSearchData.content==""||$scope.publishedPictureSearchData.content==null){
            $scope.getPublishedPictureData($scope.publishedPictureData.currentNo);
        }else{
            $scope.getPublishedPictureSearchData($scope.publishedPictureData.currentNo);
        }
    };
    //搜索快拍已发布数据------------------------------------------------------------------------------------------------
    $scope.getPublishedPictureSearchData=function(pageID){
        var url=$scope.projectName+'/picture/Published/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.publishedPictureSearchData);
        $http.post(url,$scope.publishedPictureSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.publishedPictureData=data;
                $scope.publishePicturePageNums=getPageNums($scope.publishedPictureData.pageCount);
                $scope.lastPublishedPicturePage=$scope.publishedPictureData.pageCount;
                $scope.publishedPicturePaginationConf.currentPage=$scope.publishedPictureData.currentNo;
                $scope.getLastPublishedPictureSearchPageData($scope.lastPublishedPicturePage);
            }else{
                $scope.publishedPictureData=data;
                $scope.publishedPicturePaginationConf.currentPage=0;
                $scope.publishedPicturePaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastPublishedPictureSearchPageData=function(lastPage){
        var url=$scope.projectName+'/picture/Published/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.publishedPictureSearchData).success(function(data){
            $scope.lastPublishedPicturePageData=data;
            $scope.lastPublishedPicturePageDataLength=$scope.lastPublishedPicturePageData.tileList.length;
            $scope.publishedPicturePaginationConf.totalItems=(($scope.lastPublishedPicturePageData.pageCount)-1)*20+$scope.lastPublishedPicturePageDataLength;
        });
    };
//（10）获取快拍已撤销--------------------------------------------------------------------------------------------------
    $scope.revokedPictureData=null;
    $scope.revokedPictureSearchData={
        content:""
    };
    $scope.revokedPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.revokedPicturePaginationConf.currentPage>0){
                if($scope.revokedPictureSearchData.content==""||$scope.revokedPictureSearchData.content==null){
                    $scope.getRevokedPictureData($scope.revokedPicturePaginationConf.currentPage);
                }else{
                    $scope.getRevokedPictureSearchData($scope.revokedPicturePaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getRevokedPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Revocation/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.revokedPictureData=data;
                $scope.revokedPicturePageNums=getPageNums($scope.revokedPictureData.pageCount);
                $scope.lastRevokedPicturePage=$scope.revokedPictureData.pageCount;
                $scope.revokedPicturePaginationConf.currentPage=$scope.revokedPictureData.currentNo;
                $scope.getLastRevokedPicturePageData($scope.lastRevokedPicturePage);
            }else{
                $scope.revokedPictureData=data;
                $scope.revokedPicturePaginationConf.currentPage=0;
                $scope.revokedPicturePaginationConf.totalItems=0;
            }
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
    function clearRevokedPictureSearchData(){
        for(p in $scope.revokedPictureSearchData){
            $scope.revokedPictureSearchData[p]="";
        }
    }
    $scope.refreshRevokedPicture=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.revokedPictureSearchData.content==""||$scope.revokedPictureSearchData.content==null){
            $scope.getRevokedPictureData(1);
        }else{
            $scope.getRevokedPictureSearchData(1);
        }
    };
    $scope.refreshRevokedPictureCur=function()
    {
//        $scope.orderCondition="";
        if($scope.revokedPictureSearchData.content==""||$scope.revokedPictureSearchData.content==null){
            $scope.getRevokedPictureData($scope.revokedPictureData.currentNo);
        }else{
            $scope.getRevokedPictureSearchData($scope.revokedPictureData.currentNo);
        }
    };
    //搜索快拍已撤销数据------------------------------------------------------------------------------------------------
    $scope.getRevokedPictureSearchData=function(pageID){
        var url=$scope.projectName+'/picture/Revocation/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.revokedPictureSearchData);
        $http.post(url,$scope.revokedPictureSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.revokedPictureData=data;
                $scope.revokedPicturePageNums=getPageNums($scope.revokedPictureData.pageCount);
                $scope.lastRevokedPicturePage=$scope.revokedPictureData.pageCount;
                $scope.revokedPicturePaginationConf.currentPage=$scope.revokedPictureData.currentNo;
                $scope.getLastRevokedPictureSearchPageData($scope.lastRevokedPicturePage);
            }else{
                $scope.revokedPictureData=data;
                $scope.revokedPicturePaginationConf.currentPage=0;
                $scope.revokedPicturePaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastRevokedPictureSearchPageData=function(lastPage){
        var url=$scope.projectName+'/picture/Revocation/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.revokedPictureSearchData).success(function(data){
            $scope.lastRevokedPicturePageData=data;
            $scope.lastRevokedPicturePageDataLength=$scope.lastRevokedPicturePageData.tileList.length;
            $scope.revokedPicturePaginationConf.totalItems=(($scope.lastRevokedPicturePageData.pageCount)-1)*20+$scope.lastRevokedPicturePageDataLength;
        });
    };
//（11）获取快拍草稿箱数据----------------------------------------------------------------------------------------------
    $scope.tempPictureData=null;
    $scope.tempPictureSearchData={
        content:""
    };
    $scope.tempPicturePaginationConf = {
        currentPage: null,
        totalItems:null,
        itemsPerPage: 20,
        pagesLength: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        rememberPerPage: 'perPageItems',
        onChange: function(){
            if($scope.tempPicturePaginationConf.currentPage>0){
                if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
                    $scope.getTempPictureData($scope.tempPicturePaginationConf.currentPage);
                }else{
                    $scope.getTempPictureSearchData($scope.tempPicturePaginationConf.currentPage);
                }
            }
        }
    };
    $scope.getTempPictureData=function(pageID)
    {
        var url=$scope.projectName+'/picture/Temp/'+pageID.toString()+$scope.orderCondition;
        $http.get(url).success(function(data){
            if(data.pageCount>0){
                $scope.tempPictureData=data;
                $scope.tempPicturePageNums=getPageNums($scope.tempPictureData.pageCount);
                $scope.lastTempPicturePage=$scope.tempPictureData.pageCount;
                $scope.tempPicturePaginationConf.currentPage=$scope.tempPictureData.currentNo;
                $scope.getLastTempPicturePageData($scope.lastTempPicturePage);
            }else{
                $scope.tempPictureData=data;
                $scope.tempPicturePaginationConf.currentPage=0;
                $scope.tempPicturePaginationConf.totalItems=0;
            }
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
    function clearTempPictureSearchData(){
        for(p in $scope.tempPictureSearchData){
            $scope.tempPictureSearchData[p]="";
        }
    }
    $scope.refreshTempPicture=function()
    {
        $scope.orderCondition="/time/desc";
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData(1);
        }else{
            $scope.getTempPictureSearchData(1);
        }
    };
    $scope.refreshTempPictureCur=function()
    {
//        $scope.orderCondition="";
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData($scope.tempPictureData.currentNo);
        }else{
            $scope.getTempPictureSearchData($scope.tempPictureData.currentNo);
        }
    };
    //搜索快拍草稿数据--------------------------------------------------------------------------------------------------
    $scope.getTempPictureSearchData=function(pageID){
        var url=$scope.projectName+'/picture/Temp/'+pageID.toString()+'/query'+$scope.orderCondition;
        console.log(url);
        console.log($scope.tempPictureSearchData);
        $http.post(url,$scope.tempPictureSearchData).success(function(data){
            console.log(data);
            if(data.pageCount>0){
                $scope.tempPictureData=data;
                $scope.tempPicturePageNums=getPageNums($scope.tempPictureData.pageCount);
                $scope.lastTempPicturePage=$scope.tempPictureData.pageCount;
                $scope.tempPicturePaginationConf.currentPage=$scope.tempPictureData.currentNo;
                $scope.getLastTempPictureSearchPageData($scope.lastTempPicturePage);
            }else{
                $scope.tempPictureData=data;
                $scope.tempPicturePaginationConf.currentPage=0;
                $scope.tempPicturePaginationConf.totalItems=0;
            }
        });
    };
    $scope.getLastTempPictureSearchPageData=function(lastPage){
        var url=$scope.projectName+'/picture/Temp/'+lastPage+'/query'+$scope.orderCondition;
        $http.post(url,$scope.tempPictureSearchData).success(function(data){
            $scope.lastTempPicturePageData=data;
            $scope.lastTempPicturePageDataLength=$scope.lastTempPicturePageData.tileList.length;
            $scope.tempPicturePaginationConf.totalItems=(($scope.lastTempPicturePageData.pageCount)-1)*20+$scope.lastTempPicturePageDataLength;
        });
    };

    //-----------------------------------

//    $scope.runFunction=function(){
//        if($scope.userInfo_duty=="super"){
//            $scope.getPendingData(1);
//        }
//    };
//    $scope.runFunction();
//(12)获取分类(文章）----------------------------------------------------------------------------------------------------------
    $scope.superChannelNames=[];
    $scope.normalChannelNames=[];
    $scope.newChannelNames=[];
    $scope.getSuperChannelNames=function(){
        var url=$scope.projectName+'/channel/channels';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    if(data[i].englishName!="kuaipai"){
                        $scope.checkFirstChannelForSuper(data[i]);
                    }
                }
            }else{
                $scope.superChannelNames=[];
            }
        });
    };
    $scope.getSuperChannelNames();
    //判断是否有次级目录-------------------------------------------------------------------------------------------------
    $scope.checkFirstChannelForSuper=function(channelData){
        var url=$scope.projectName+'/channel/'+channelData.englishName+'/channels';
        $http.get(url).success(function (data) {
            if(data.length>0){
                $scope.getSecondChannelNamesForSuper(channelData.englishName);
            }else{
                $scope.superChannelNames.push(channelData);
            }
        });
    };
    //获得次级目录名----------------------------------------------------------------------------------------------------
    $scope.getSecondChannelNamesForSuper=function(channelName){
        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.superChannelNames.push(data[i]);
                }
            }
        });
    };
    //根据duty判断是否有次级目录-------------------------------------------------------------------------------------------------
    $scope.checkFirstChannelForNormal=function(channelData){
        var url=$scope.projectName+'/channel/'+channelData.englishName+'/channels';
        $http.get(url).success(function (data) {
            if(data.length>0){
                $scope.getSecondChannelNamesForNormal(channelData.englishName);
            }else{
                $scope.normalChannelNames.push(channelData);
            }
        });
    };
    //根据duty获得次级目录名----------------------------------------------------------------------------------------------------
    $scope.getSecondChannelNamesForNormal=function(channelName){
        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.normalChannelNames.push(data[i]);
                }
            }
        });
    };
    //根据duty获得分类--------------------------------------------------------------------------------------------------
    $scope.getNormalChannelNames=function(){
        var url=$scope.projectName+'/channel/channels';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    if(data[i].englishName==$scope.userInfo_duty){
                        $scope.checkFirstChannelForNormal(data[i]);
                    }
                }
            }else{
                $scope.normalChannelNames=[];
            }
        });
    };
    $scope.getNormalChannelNames();
    $scope.getNewChannelNames=function(){
        if($scope.userInfo_duty=="super"){
//            $scope.getSuperChannelNames();
            $scope.newChannelNames=$scope.superChannelNames;
        }else{
//            $scope.getNormalChannelNames();
            $scope.newChannelNames=$scope.normalChannelNames;
        }
    };
    $scope.getNewChannelNames();
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
    $scope.refreshChannelNames=function(){
        $scope.newChannelNames=[];
        $scope.newPictureChannelNames=[];
    };
//----------------------------------------------------------------------------------------------------------------------
    //新加功能按钮------------------------------------------------------------------------------------------------------
    //判断设置中选择的是哪一个radio框-----------------------------------------------------------------------------------
    $scope.setStates="";
    $scope.getSetState=function(){
        var url=$scope.projectName+"/article/ispending";
        $http.get(url).success(function(data){
//            console.log(data);
            $scope.setStates=data;
            console.log($scope.setStates);
            var vv=document.getElementsByName("radios");
            for(var i=0;i<vv.length;i++){
                if(vv.item(i).value==$scope.setStates){
                    vv.item(i).checked=true;
                }
            }
        });
    };
    $scope.getSetState();
    $scope.selectSetStates="";
    //设置radio选项框---------------------------------------------------------------------------------------------------
    $scope.setRadioState=function(){
        var v=document.getElementsByName("radios");
        for(var i=0;i< v.length;i++){
            if(v.item(i).checked){
                var ss= v.item(i).value;
                $scope.selectSetStates=ss;
//                console.log(ss);
                console.log($scope.selectSetStates);
            }
        }
        var url=$scope.projectName+"/setting/settag/article/"+$scope.selectSetStates;
        $http.get(url).success(function(){
            alert("设置成功");
        });
    };
    //设置审查评论的状态------------------------------------------------------------------------------------------------
    $scope.setCommentStates="";
    $scope.getCommentSetState=function(){
        var url=$scope.projectName+"/setting/istag/comment";
        $http.get(url).success(function(data){
//            console.log(data);
            $scope.setCommentStates=data;
//            console.log($scope.setCommentStates);
            var vv=document.getElementsByName("radios1");
            for(var i=0;i<vv.length;i++){
                if(vv.item(i).value==$scope.setCommentStates){
                    vv.item(i).checked=true;
                }
            }
        });
    };
    $scope.getCommentSetState();
    $scope.selectSetCommentStates="";
    $scope.setCommentRadioState=function(){
        var v=document.getElementsByName("radios1");
        for(var i=0;i< v.length;i++){
            if(v.item(i).checked){
                var sss= v.item(i).value;
                $scope.selectSetCommentStates=sss;
//                console.log(ss);
                console.log($scope.selectSetCommentStates);
            }
        }
        var url=$scope.projectName+"/setting/settag/comment/"+$scope.selectSetCommentStates;
        $http.get(url).success(function(){
            alert("设置成功");
        });
    };
    //文章新建添加按钮--------------------------------------------------------------------------------------------------
    $scope.setButtonInNewArticleForPending=function(){
        if($scope.setStates=="true"){
            return "btn btn-md btn-info";
        }else{
            return "btn btn-md btn-info sr-only";
        }
    };
    $scope.setButtonInNewArticleForPublish=function(){
        if($scope.setStates=="true"){
            return "btn btn-md btn-info sr-only";
        }else{
            return "btn btn-md btn-info";
        }
    };
    //是否显示快拍、是否为超级管理员------------------------------------------------------------------------------------
    $scope.showDutyForKuaiPai=function(){
        if($scope.userInfo_duty=="kuaipai"){
            return "sidebar-list dropdown";
        }else if($scope.userInfo_duty=="super"){
            return "sidebar-list dropdown";
        }else{
            return "sidebar-list dropdown sr-only";
        }
    };
    $scope.showDutyForWenZhang=function(){
        if($scope.userInfo_duty=="super"){
            return "sidebar-list dropdown";
        }else if($scope.userInfo_duty!="kuaipai"){
            return "sidebar-list dropdown";
        }else{
            return "sidebar-list dropdown sr-only";
        }
    };
    //添加用户----------------------------------------------------------------------------------------------------------
    $scope.addUserInfo={
        name:"",
        passwd:"",
        duty:""
    };
    $scope.elsePassword="";
    $scope.clearUserInfo=function(){
        $scope.addUserInfo.passwd="";
        $scope.elsePassword="";
    };
    $scope.addUserInformation1=function(){
        var url=$scope.projectName+"/user/register";
        console.log(url);
        console.log($scope.addUserInfo);
        if($scope.addUserInfo.passwd!=$scope.elsePassword){
            alert("两次输入密码不一致！");
            $scope.clearUserInfo();
        }else{
            if($scope.addUserInfo.duty=="超级管理员"){
                $scope.addUserInfo.duty="super";
                $http.post(url,$scope.addUserInfo).success(function(){
                    alert("注册成功！");
                    $scope.clearUserInfo();
                    $scope.addUserInfo.name="";
                    $('#addUser_1').modal('toggle');
                });
            }else if($scope.addUserInfo.duty==""){
                alert("请选择管理分类！");
            }else{
                for(var i=0;i<$scope.userDuty.length;i++){
                    if(($scope.userDuty)[i].channelName==$scope.addUserInfo.duty){
                        $scope.addUserInfo.duty=($scope.userDuty)[i].englishName;
                        $http.post(url,$scope.addUserInfo).success(function(){
                            alert("注册成功！");
                            $scope.clearUserInfo();
                            $scope.addUserInfo.name="";
                            $('#addUser_1').modal('toggle');
                        });
                    }
                }
            }
        }
    };
    //修改密码-----------------------------------------------------------------------------------------------------------
    $scope.changePassWord={
        oldPasswd:"",
        newPasswd:""
    };
    $scope.clearModifyPassword=function(){
        $scope.changePassWord.newPasswd="";
        $scope.newPassword1="";
    };
    $scope.newPassword1="";
    $scope.modifyPassWord=function(){
        var url=$scope.projectName+"/user/update";
        console.log(url);
        console.log($scope.changePassWord);
        if($scope.changePassWord.newPasswd!=$scope.newPassword1){
            alert("两次输入的新密码不一致！");
            $scope.clearModifyPassword();
        }else{
            $http.post(url,$scope.changePassWord).success(function(data){
                console.log(data);
                $scope.clearModifyPassword();
                $scope.changePassWord.oldPasswd="";
                $('#modify_passWord').modal('toggle');
            });
        }
    };
    //测试修改密码------------------------------------------------------------------------------------------------------
    $scope.modifyPassWord1=function(){
        var url=$scope.projectName+"/user/update";
        console.log(url);
        console.log($scope.changePassWord);
        if($scope.changePassWord.newPasswd!=$scope.newPassword1){
            alert("两次输入的新密码不一致！");
            $scope.clearModifyPassword();
        }else{
            $http.post(url,$scope.changePassWord).success(function(data){
                console.log(data);
                $scope.clearModifyPassword();
                $scope.changePassWord.oldPasswd="";
                $('#modify_passWord1').modal('toggle');
            });
        }
    };
//    //设置app打开图片---------------------------------------------------------------------------------------------------

//------------------------//-----------------------//---------------------//----------------------//--------------------//
    //(已发布模块)加评论的已发布和未发布--------------------------------------------------------------------------------
    $scope.goCommentDetailsInPublished=function()
    {
        document.getElementById("published").className="tab-pane";
        document.getElementById("publishedCommentDetail").className="tab-pane active";
    };
    $scope.commentDetailDataInPublished={
        pageCount:null,
        currentNo:null,
        commendList:[{
            commendId:null,
            userName:"",
            userId:null,
            timeDate:"",
            level:null,
            state:"",
            from:null,
            content:"",
            reply:""
        }]
    };
    $scope.getCommentDetailDataInPublished=function(pageID)
    {
        var url=$scope.commentDetailsUrlInPublished+"/"+pageID.toString()+$scope.orderCondition;
//        console.log(url);
        $http.get(url).success(function(data){
//            if(data.length>0){
            $scope.commentDetailDataInPublished=data;
            $scope.pageNumsInCommentOfPublished=getPageNums($scope.commentDetailDataInPublished.pageCount);
//            $scope.currentPage=$scope.commentDetailDataInPublished.currentNo;
            setPageInPub($scope.commentDetailDataInPublished.pageCount,$scope.commentDetailDataInPublished.currentNo);
//            console.log("成功获取数据");
//            }else{
//                $scope.commentDetailData="";
//                setPageInPub(1,1);
//            }
        });
    };
    function setPageInPub(count,pageIndex){
//        var container=container;//容器
        var count=count;//总页数
        var pageIndex=pageIndex;//当前页数
        var a=[];
        //总页数少于10全部显示，大于10显示前3，后3，中间3，其余...
        if(pageIndex==1){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">&laquo;</a>";
        }else if(pageIndex==0){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">暂无数据</a>";
        }
        else{
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
                a[a.length]=". . .<a href=\"#\">" + count + "</a>";
            }else if(pageIndex>=count-3){
                a[a.length]="<a href=\"#\">1</a>. . .";
                for(var i=count-4;i<=count;i++){
                    setPageList();
                }
            }else{//当前页在中间部分
                a[a.length]="<a href=\"#\">1</a>. . .";
                for(var i=pageIndex-2;i<=pageIndex+2;i++){
                    setPageList();
                }
                a[a.length]=". . .<a href=\"#\">" + count + "</a>";
            }
        }
        if(pageIndex==count&&pageIndex!=0){
            a[a.length]="<a href=\"#\" class=\"next unclick\">&raquo;</a>";
        }else if(pageIndex==0&&count==0){
            a[a.length]="<a href=\"#\" class=\"next unclick\"></a>";
        }
        else{
            a[a.length]="<a href=\"#\" class=\"next\">&raquo;</a>";
        }
        document.getElementById("commentDetail_pageInPub").innerHTML= a.join("");
        //事件点击
        var pageClick=function(){
            var oAlink=document.getElementById("commentDetail_pageInPub").getElementsByTagName("a");
            var inx=pageIndex;//初始页码
            oAlink[0].onclick=function(){//点击上一页
                if(inx==1){
                    return false;
                }
                inx--;
                setPageInPub(count,inx);
                $scope.getCommentDetailDataInPublished(inx);
                return false;
            };
            for(var i=1;i<oAlink.length-1;i++){//点击页码
                oAlink[i].onclick=function(){
                    inx=parseInt(this.innerHTML);
                    setPageInPub(count,inx);
                    $scope.getCommentDetailDataInPublished(inx);
                    return false;
                }
            }
            oAlink[oAlink.length-1].onclick=function(){//点击下一页
                if(inx==count){
                    return false;
                }
                inx++;
                setPageInPub(count,inx);
                $scope.getCommentDetailDataInPublished(inx);
            }
        }()
    }
    //将文章评论数据传输给全局变量commentDetailData
    $scope.transDataToCommentDetailDataInPublished=function(data)
    {
        for(p in $scope.commentDetailDataInPublished){
            if(p=="commendList"){
                for(i in data[p]){
                    $scope.commentDetailDataInPublished[p][i]=data[p][i];
                }
            }else{
                $scope.commentDetailDataInPublished[p]=data[p];
            }
        }
    };

    $scope.getCommentDetailTitleInPublished=function(title,typeStr)
    {
        if(typeStr=="news"){
            $scope.commentDetailTitleInPublished=title+"_"+"商报评论";
//            console.log($scope.commentDetailTitle);
        }else if(typeStr=="crawler"){
            $scope.commentDetailTitleInPublished=title+"_"+"爬虫评论";
//            console.log($scope.commentDetailTitle);
        }
    };
    $scope.publishIconState="";
    $scope.setPublishIconState=function(){
        if($scope.publishIconState=="publish"){
            return "btn btn-info sr-only";
        }else if($scope.publishIconState=="unpublish"){
            return "btn btn-info";
        }
    };
    //点击显示文章评论明细
    $scope.commentDetailsUrlInPublished="";
    $scope.commentDetailsUrlInPublishedFor="";
    $scope.showCommentsInPublished=function(articleId,title,type,stateType)
    {
        $scope.commentDetailsUrlInPublished=$scope.projectName+'/commend/1/'+articleId+'/'+type+'/'+stateType;
        $scope.commentDetailsUrlInPublishedFor=$scope.projectName+'/commend/1/'+articleId+'/'+type;
        $scope.publishIconState=stateType;
//        $scope.publishIconState();
        $scope.goCommentDetailsInPublished();
        $scope.getCommentDetailDataInPublished(1);
        $scope.getCommentDetailTitleInPublished(title,type);
    };
//(快拍已发布模块)加评论的已发布和未发布--------------------------------------------------------------------------------
    $scope.goCommentDetailsInPublishedPic=function()
    {
        document.getElementById("publishedPicture").className="tab-pane";
        document.getElementById("publishedPicCommentDetail").className="tab-pane active";
    };
    $scope.commentDetailDataInPublishedPic="";
    $scope.getCommentDetailDataInPublishedPic=function(pageID)
    {
        var url=$scope.commentDetailsUrlInPublishedPic+"/"+pageID.toString()+$scope.orderCondition;
//        console.log(url);
        $http.get(url).success(function(data){
//            if(data.length>0){
            $scope.commentDetailDataInPublishedPic=data;
            $scope.pageNumsInCommentOfPublishedPic=getPageNums($scope.commentDetailDataInPublishedPic.pageCount);
//            $scope.currentPage=$scope.commentDetailDataInPublished.currentNo;
            setPageInPubPic($scope.commentDetailDataInPublishedPic.pageCount,$scope.commentDetailDataInPublishedPic.currentNo);
//            console.log("成功获取数据");
//            }else{
//                $scope.commentDetailData="";
//                setPageInPubPic(1,1);
//            }
        });
    };
    function setPageInPubPic(count,pageIndex){
//        var container=container;//容器
        var count=count;//总页数
        var pageIndex=pageIndex;//当前页数
        var a=[];
        //总页数少于10全部显示，大于10显示前3，后3，中间3，其余...
        if(pageIndex==1){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">&laquo;</a>";
        }else if(pageIndex==0){
            a[a.length]="<a href=\"#\" class=\"prev unclick\">暂无数据</a>";
        }
        else{
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
                a[a.length]=". . .<a href=\"#\">" + count + "</a>";
            }else if(pageIndex>=count-3){
                a[a.length]="<a href=\"#\">1</a>. . .";
                for(var i=count-4;i<=count;i++){
                    setPageList();
                }
            }else{//当前页在中间部分
                a[a.length]="<a href=\"#\">1</a>. . .";
                for(var i=pageIndex-2;i<=pageIndex+2;i++){
                    setPageList();
                }
                a[a.length]=". . .<a href=\"#\">" + count + "</a>";
            }
        }
        if(pageIndex==count&&pageIndex!=0){
            a[a.length]="<a href=\"#\" class=\"next unclick\">&raquo;</a>";
        }else if(pageIndex==0&&count==0){
            a[a.length]="<a href=\"#\" class=\"next unclick\"></a>";
        }
        else{
            a[a.length]="<a href=\"#\" class=\"next\">&raquo;</a>";
        }
        document.getElementById("commentDetail_pageInPubPic").innerHTML= a.join("");
        //事件点击
        var pageClick=function(){
            var oAlink=document.getElementById("commentDetail_pageInPubPic").getElementsByTagName("a");
            var inx=pageIndex;//初始页码
            oAlink[0].onclick=function(){//点击上一页
                if(inx==1){
                    return false;
                }
                inx--;
                setPageInPubPic(count,inx);
                $scope.getCommentDetailDataInPublishedPic(inx);
                return false;
            };
            for(var i=1;i<oAlink.length-1;i++){//点击页码
                oAlink[i].onclick=function(){
                    inx=parseInt(this.innerHTML);
                    setPageInPubPic(count,inx);
                    $scope.getCommentDetailDataInPublishedPic(inx);
                    return false;
                }
            }
            oAlink[oAlink.length-1].onclick=function(){//点击下一页
                if(inx==count){
                    return false;
                }
                inx++;
                setPageInPubPic(count,inx);
                $scope.getCommentDetailDataInPublishedPic(inx);
            }
        }()
    }
    //将文章评论数据传输给全局变量commentDetailData
    $scope.transDataToCommentDetailDataInPublishedPic=function(data)
    {
        for(p in $scope.commentDetailDataInPublishedPic){
            if(p=="commendList"){
                for(i in data[p]){
                    $scope.commentDetailDataInPublishedPic[p][i]=data[p][i];
                }
            }else{
                $scope.commentDetailDataInPublishedPic[p]=data[p];
            }
        }
    };
    $scope.getCommentDetailTitleInPublishedPic=function(title,typeStr)
    {
        if(typeStr=="news"){
            $scope.commentDetailTitleInPublishedPic=title+"_"+"商报评论";
//            console.log($scope.commentDetailTitle);
        }else if(typeStr=="crawler"){
            $scope.commentDetailTitleInPublishedPic=title+"_"+"爬虫评论";
//            console.log($scope.commentDetailTitle);
        }
    };
    $scope.publishPicIconState="";
    $scope.setPublishPicIconState=function(){
        if($scope.publishPicIconState=="publish"){
            return "btn btn-info sr-only";
        }else if($scope.publishPicIconState=="unpublish"){
            return "btn btn-info";
        }
    };
    //点击显示文章评论明细
    $scope.commentDetailsUrlInPublishedPic="";
    $scope.commentDetailsUrlInPublishedPicFor="";
    $scope.showCommentsInPublishedPic=function(articleId,title,type,stateType)
    {
        $scope.commentDetailsUrlInPublishedPic=$scope.projectName+'/commend/1/'+articleId+'/'+type+'/'+stateType;
        $scope.commentDetailsUrlInPublishedPicFor=$scope.projectName+'/commend/1/'+articleId+'/'+type;
        $scope.publishPicIconState=stateType;
//        $scope.publishIconState();
        $scope.goCommentDetailsInPublishedPic();
        $scope.getCommentDetailDataInPublishedPic(1);
        $scope.getCommentDetailTitleInPublishedPic(title,type);
    };
    //------------------------------------------------------------------------------------------------------------------
    //加载时显示转圈----------------------------------------------------------------------------------------------------
//------------------------------------------------
    $scope.coverIt=function(){
        var cover = document.getElementById("cover");
        var covershow = document.getElementById("coverShow");
        cover.style.display = 'block';
        covershow.style.display = 'block';
        alert("遮罩");
    };
    $scope.closeOver=function(){
        var cover = document.getElementById("cover");
        var covershow = document.getElementById("coverShow");
        cover.style.display = 'none';
        covershow.style.display = 'none';
        alert("遮罩取消");
    };
    //-----------------------------------------------------------------------------------------------------------------

    //判断外链文章url是否为正确的url形式-----------
//    $scope.checkOutSideUrl=function(outUrl){
//        var outSide=/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test(outUrl);
////        alert(/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"\"])*$/.test(outUrl));
//        if(outSide=="true")
//
//    };

}]);
