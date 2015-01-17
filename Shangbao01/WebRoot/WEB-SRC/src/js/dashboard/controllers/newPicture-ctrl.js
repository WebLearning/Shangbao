/**
 *NewPicture controller
 */
angular.module("Dashboard").controller("newPictureCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.recvData={
        activity: null,
        author: "杨一",
        channel: ["原创","本地","热门"],
        channelIndex: null,
        clicks: null,
        content: "据了解",
        crawlerCommends: null,
        crawlerCommendsPublish: null,
        from: "成都商报",
        keyWord: ["成绵乐","动车组","救援演练"],
        level: "1等",
        likes: null,
        newsCommends: null,
        newsCommendsPublish: null,
        picturesUrl: ["http://localhost:8080/Shangbao01/WEB-SRC/src/img/spiderMan.jpg","http://localhost:8080/Shangbao01/WEB-SRC/src/img/shitMan.jpg"],
        subTitle: "即将进入",
        summary: "摘要：成绵乐",
        tag: null,
        time: new Date(),
        title: "成绵乐",
        titlePicUrl: null,
        words: null
    };
    $scope.channelNames=[
        {channelName:'国内'},
        {channelName:'商报原创'},
        {channelName:'活动'},
        {channelName:'娱乐'}
    ];
    //获得顶级目录名----------------------------------------------------------------------------------------------------
    $scope.newChannelNames=[];
    $scope.getNewChannelNames=function(){
        var url=$scope.projectName+'/channel/kuaipai/channels';
        //console.log(url);
        $http.get(url).success(function(data){
            //console.log(data);
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.newChannelNames.push(data[i]);
                }
            }else{
                $scope.newChannelNames=[];
            }
            //console.log($scope.newChannelNames);
        });
    };
    $scope.getNewChannelNames();
    //活动活动目录------------------------------------------------------------------------------------------------------
    $scope.newActivityNames=[];
    $scope.getNewActivityNames=function(){
        var url=$scope.projectName+'/channel/activities';
        $http.get(url).success(function(data){
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.newActivityNames.push(data[i]);
                }
            }else{
                $scope.newActivityNames=[];
            }
        });
    };
    $scope.getNewActivityNames();
    //
    $scope.getEditorContent=function()
    {
        //导入数据
        for(p in $scope.newArticleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                for(i in $scope.recvData[p]){
                    $scope.newArticleData[p][i]=$scope.recvData[p][i];
                }
            }else{
                $scope.newArticleData[p]=$scope.recvData[p];
            }
        }
    };

    $scope.testLog=function()
    {
        $scope.calculateWords();
//        console.log($scope.recvData);
        console.log($scope.newArticleData);
    };

    //footer的3个按钮的操作
    $scope.clearArticle=function()
    {
        for(p in $scope.newArticleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                $scope.newArticleData[p]=[];
            }else{
                $scope.newArticleData[p]="";
            }
        }
        $scope.calculateWords();
    };

    $scope.saveArticle=function(){
        $scope.calculateWords();
        var jsonString=JSON.stringify($scope.newArticleData);
        //console.log(jsonString);
        $http.post($scope.projectName+'/picture/newPicture',jsonString).success(function(data) {
            alert("保存成功");
        });
        $scope.clearArticle();
    };


    //得到字数
    $scope.calculateWords=function()
    {
        $scope.newArticleData.words=$scope.newArticleData.content.length;
    };
    //图片数-----------------------------------------------------
    /*$scope.calculatePictures=function(){
     $scope.newArticleData.pictures=$scope.newArticleData.picturesUrl.length;
     }*/
    //刷新时间
    $scope.getCurrentDatetime=function()
    {
        $scope.newArticleData.time=new Date();
    };

    //删除关键词 分类 和 图片数组的操作
    $scope.deleteKeyword=function(index)
    {
        $scope.newArticleData.keyWord.splice(index,1);
    };

    $scope.deleteChannel=function(index)
    {
        $scope.newArticleData.channel.splice(index,1);
    };

    $scope.deleteActivity=function()
    {
        $scope.newArticleData.activity=null;
    };

    $scope.deletePicUrl=function(index)
    {
        $scope.newArticleData.picturesUrl.splice(index,1);
    };

    //添加关键词和分类、活动
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.keyWord.push($scope.additionKeyword);
            $('#myPictureModal_addKeyword').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.channel.push($scope.additionChannel);
            $('#myPictureModal_addChannel').modal('toggle');
        }
    };

    $scope.replyBtnStr=function(str){
        if(str==""||str==null){
            return "sr-only";
        }else{
            return "col-md-2";
        }
    };

    $scope.addActivity=function(){
        if($scope.additionActivity==undefined||$scope.additionActivity==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.activity=$scope.additionActivity;
            $('#myPictureModal_addActivity').modal('toggle');
        }
    };

    //关于上传图片----------------------------------------------------------------------------------------------

    //当input file选择的文件有变化时
    $scope.onInputChange=function(inputFileObj)
    {
        $scope.previewIMG(inputFileObj);
    };

    //预览图片

    $scope.previewIMG=function(inputFileObj)
    {
        if(inputFileObj.value==""){
            $scope.deletePreviewFrame();
            $scope.disableUploadButton();
            $scope.refreshImgInput();
        }else{
            $scope.addPreviewFrame();
            $scope.loadPreviewIMG(inputFileObj);
            $scope.disableConfirmButton();
        }
    };

    $scope.loadPreviewIMG=function(obj)
    {
        var docObj = obj;
        var preViewUrl = window.URL.createObjectURL(docObj.files[0]);
        var imgObjPreview=document.getElementById("imgPicturePreview");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPicturePreview">'
            +'</div>';

        document.getElementById("previewPictureFrame").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewPictureFrame").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myPictureUploadImgForm").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $('#myPictureUploadImgForm').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalPictureFooterID").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
    };

    $scope.getPicUrl=function()
    {
        var url=document.getElementById("myPictureIFrameID").contentDocument.body.innerHTML;
        console.log(url);
        return url;
    };

    $scope.pushPicUrl=function(url)
    {
        $scope.newArticleData.picturesUrl.push(url);
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加图片到ueditor内容
    $scope.addImgToEditorContent=function(url){
        var text='<img src="'+url+'">';
        $scope.newArticleData.content=text+$scope.newArticleData.content;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myPictureModal_addIMG').modal('toggle');
    };


    //关于上传图片的----------------------------------------------------------------------------------------------


}]);