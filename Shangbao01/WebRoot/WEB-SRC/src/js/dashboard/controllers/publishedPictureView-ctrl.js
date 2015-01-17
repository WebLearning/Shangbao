/**
 publishedPictureView-Ctrl
 */
angular.module("Dashboard").controller("publishedPictureViewCtrl",["$scope","$http",function($scope,$http){

    $scope.goPublishedPicture=function()
    {
        $scope.clearArticle();
        document.getElementById("publishedPictureView").className="tab-pane";
        document.getElementById("publishedPicture").className="tab-pane active";
        document.getElementById("publishedPictureSidebarID").className="sidebar-list";
    };
    $scope.testLog=function()
    {
        $scope.calculateWords();
        //console.log($scope.recvData);
        console.log($scope.articleData);
    };

    $scope.clearArticle=function()
    {
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                $scope.articleData[p]=[];
            }else{
                $scope.articleData[p]="";
            }
        }
        $scope.calculateWords();
    };

    //得到字数
    $scope.calculateWords=function()
    {
        $scope.articleData.words=$scope.articleData.content.length;
    };
    //图片数-----------------------------------------------------
    /*$scope.calculatePictures=function(){
     $scope.articleData.pictures=$scope.articleData.picturesUrl.length;
     }*/
    //刷新时间
    $scope.getCurrentDatetime=function()
    {
        $scope.articleData.time=new Date();
    };

    //删除关键词 分类 和 图片数组的操作
    $scope.deleteKeyword=function(index)
    {
        $scope.articleData.keyWord.splice(index,1);
    };

    $scope.deleteChannel=function(index)
    {
        $scope.articleData.channel.splice(index,1);
    };

    $scope.deleteActivity=function()
    {
        $scope.articleData.activity=null;
    };

    $scope.deletePicUrl=function(index)
    {
        $scope.articleData.picturesUrl.splice(index,1);
    };

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.keyWord.push($scope.additionKeyword);
            $('#myPictureModal_addKeyword_published').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myPictureModal_addChannel_published').modal('toggle');
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
            $scope.articleData.activity=$scope.additionActivity;
            $('#myPictureModal_addActivity_published').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPicturePreview_published");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPicturePreview_published">'
            +'</div>';

        document.getElementById("previewPictureFrame_published").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewPictureFrame_published").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myPictureUploadImgForm_published").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_published").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $('#myPictureUploadImgForm_published').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalPictureFooterID_published").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_published").innerHTML=tempString;
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
        var url = document.getElementById("myPictureIFrameID_published").contentDocument.body.innerHTML;
        console.log(url);
        return url;
    };

    $scope.pushPicUrl=function(url)
    {
        $scope.articleData.picturesUrl.push(url);
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加图片到ueditor内容
    $scope.addImgToEditorContent=function(url){
        var text='<img src="'+url+'">';
        $scope.articleData.content=text+$scope.articleData.content;
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myPictureModal_addIMG_published').modal('toggle');
    };

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

    //获得活动目录------------------------------------------------------------------------------------------------------
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
    //关于上传图片的----------------------------------------------------------------------------------------------

}]);