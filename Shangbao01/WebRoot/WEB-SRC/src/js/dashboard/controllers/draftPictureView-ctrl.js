/**
 draftPictureView-Ctrl
 **/

angular.module("Dashboard").controller("draftPictureViewCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.backCurDraftPicture=function(){
        if($scope.tempPictureSearchData.content==""||$scope.tempPictureSearchData.content==null){
            $scope.getTempPictureData($scope.tempPictureData.currentNo);
            $scope.closeOver();
        }else{
            $scope.getTempPictureSearchData($scope.tempPictureData.currentNo);
            $scope.closeOver();
        }
    };
    $scope.goDraftPicture=function()
    {
        $scope.clearPictureArticle();
        $scope.coverIt();
        document.getElementById("draftPictureView").className="tab-pane";
        document.getElementById("draftPicture").className="tab-pane active";
        document.getElementById("draftPictureSidebarID").className="sidebar-list";
        $scope.backCurDraftPicture();
    };

    //footer的3个按钮的操作
    $scope.clearPictureArticle=function()
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
        $scope.calculateWords();
        $scope.calculatePictures();
    };

    $scope.savePictureArticle=function(){
        $scope.coverIt();
        //console.log("test new save");
        $scope.calculateWords();
        $scope.calculatePictures();
        if($scope.articleData.channel.length==0){
            alert("分类不能为空");
            $scope.closeOver();
        }else if($scope.articleData.channel.length!=0){
            var jsonString=JSON.stringify($scope.articleData);
            //console.log($scope.articleData);
            var url=$scope.projectName+'/picture/Temp/1/'+$scope.articleData.id;
            $http.put(url,jsonString).success(function(data) {
//            $scope.saveStateInDraftPic1=data;
//            alert("保存文章成功");
                $scope.goDraftPicture();
                alert("保存文章成功");
                $scope.closeOver();
            });
        }
    };
    $scope.deletePictureArticleInDraftPicture=function()
    {
        $scope.coverIt();
        var url=$scope.projectName+"/picture/Temp/"+($scope.tempPictureData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
        $http.delete(url).success(function(){
//            alert("删除成功");
            $scope.goDraftPicture();
            alert("删除成功");
            $scope.closeOver();
        });
    };
    $scope.saveStateInDraftPic1="";
    $scope.submitPictureArticleInDraftPicture=function()
    {
        $scope.coverIt();
        $scope.calculateWords();
        $scope.calculatePictures();
        if($scope.articleData.channel.length==0){
            alert("分类不能为空");
            $scope.closeOver();
        }else if($scope.articleData.channel.length!=0){
            var jsonString=JSON.stringify($scope.articleData);
            //console.log($scope.articleData);
            var url1=$scope.projectName+'/picture/Temp/1/'+$scope.articleData.id;
            $http.put(url1,jsonString).success(function(data) {
                $scope.saveStateInDraftPic1=data;
                console.log(data);
                alert("保存文章成功");
                var url=$scope.projectName+"/picture/Temp/"+($scope.tempPictureData.currentNo).toString()+"/statechange/"+$scope.articleData.id;
                if($scope.saveStateInDraftPic1=="true"){
                    $http.put(url).success(function(){
//                    alert("提交成功");
                        $scope.goDraftPicture();
                        alert("提交成功");
                        $scope.closeOver();
                    });
                }
            });
        }
    };
    $scope.saveStateInDraftPic2="";
    $scope.publishArticleTimingInDraftPicture=function()
    {
        $scope.coverIt();
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeInDraftPicture.substr(0,10);
        var str2=$scope.publishTimeInDraftPicture.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        $scope.calculateWords();
        $scope.calculatePictures();
        if($scope.articleData.channel.length==0){
            alert("分类不能为空");
            $scope.closeOver();
        }else if($scope.articleData.channel.length!=0){
            var jsonString=JSON.stringify($scope.articleData);
            //console.log($scope.articleData);
            var url1=$scope.projectName+'/picture/Temp/1/'+$scope.articleData.id;
            $http.put(url1,jsonString).success(function(data) {
                $scope.saveStateInDraftPic2=data;
                alert("保存文章成功");
                var url=$scope.projectName+"/picture/Temp/"+($scope.tempPictureData.currentNo).toString()+"/timingpublish/"+$scope.articleData.id+"/"+time;
                console.log(url);
                if($scope.saveStateInDraftPic2=="true"){
                    $http.get(url).success(function(){
//                    alert("定时成功");
                        $('#Select_TimeInDraftPicture').modal('toggle');
                        $scope.goDraftPicture();
                        alert("定时成功");
                        $scope.closeOver();
                    });
                }
            });
        }
    };

    //得到字数
    $scope.calculateWords=function()
    {
        $scope.articleData.words=$scope.delHtmlTag($scope.articleData.content).length;
    };
    $scope.delHtmlTag=function(str){
        return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
    };
    //图片数-----------------------------------------------------
    $scope.calculatePictures=function() {
        $scope.articleData.pictures = $scope.articleData.picturesUrl.length;
    };
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
    $scope.addPictureToEditor=function(picUrl){
        //console.log(picUrl);
        var text='<img src="'+picUrl+'">';
        $scope.articleData.content=text+$scope.articleData.content;
//        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.keyWord.push($scope.additionKeyword);
            $('#myPictureModal_addKeyword_draft').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myPictureModal_addChannel_draft').modal('toggle');
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
            $('#myPictureModal_addActivity_draft').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPicturePreview_draft");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPicturePreview_draft">'
            +'</div>';

        document.getElementById("previewPictureFrame_draft").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewPictureFrame_draft").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myPictureUploadImgForm_draft").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_draft").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        document.form_draftPicture.action=$scope.projectPicActionName;
        $('#myPictureUploadImgForm_draft').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalPictureFooterID_draft").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalPictureFooterID_draft").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
        $scope.clearIframeContentInDraftPic();
    };

    $scope.clearIframeContentInDraftPic=function(){
        document.getElementById("myPictureIFrameID_draft").contentDocument.body.innerHTML="";
    };
    $scope.getPicUrl=function()
    {
        var url = document.getElementById("myPictureIFrameID_draft").contentDocument.body.innerHTML;
        //url=url.substr(8);
        //url=$scope.projectName+"/WEB-SRC"+url;
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
        $('#myPictureModal_addIMG_draft').modal('toggle');
    };
    //关于上传图片的----------------------------------------------------------------------------------------------


}]);