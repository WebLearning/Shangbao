
angular.module("Dashboard").controller("articleCtrl", ["$scope","$http", function ($scope,$http) {

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
        $http.post($scope.projectName+'/article/newArticle',jsonString).success(function(data) {
            alert("保存文章成功");
        });
        $scope.clearArticle();
    };
    $scope.pendArticleInNewArticle=function(){
        $scope.calculateWords();
        var url=$scope.projectName+"/article/newArticle/pend";
        var jsonString=JSON.stringify($scope.newArticleData);
        $http.post(url,jsonString).success(function(){
            alert("提交成功！");
            $scope.clearArticle();
        });
    };
    $scope.publishedArticleInNewArticle=function(){
        $scope.calculateWords();
        var url=$scope.projectName+"/article/newArticle/pend";
        var jsonString=JSON.stringify($scope.newArticleData);
        $http.post(url,jsonString).success(function(){
            alert("发送成功！");
            $scope.clearArticle();
        });
    };
    $scope.publishArticleInNewArticleTiming=function()
    {
        var myDate=new Date();
        var myDateTime=myDate.getTime();
        var str1=$scope.publishTimeInNewArticle.substr(0,10);
        var str2=$scope.publishTimeInNewArticle.substr(11,16);
        var str3=str1.concat(" ");
        var str4=str3.concat(str2);
        var str5=new Date(str4);
        var myPublishedTime=str5.getTime();
        var time=myPublishedTime-myDateTime;
        console.log(time);
        var url=$scope.projectName+"/article/newArticle/timingpublish/"+time;
        console.log(url);
        $http.get(url).success(function(){
            alert("定时成功");
            $('#Select_TimeInNewArticle').modal('toggle');
            $scope.clearArticle();
        });
    };

    //得到字数
    $scope.calculateWords=function()
    {
        var str1=$scope.delHtmlTag($scope.newArticleData.content);
        $scope.newArticleData.words=$scope.delStrTrim(str1).length;
    };

    $scope.delHtmlTag=function(str){
        return str.replace(/<[^>]+>/g,"");//去掉所有的html标记
    };
    $scope.delStrTrim=function(str){
        while(str.indexOf(" ")!=-1){
            var str=str.replace(" ","");
        }
        return str;
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

    $scope.deletePicUrl=function(index)
    {
        $scope.newArticleData.picturesUrl.splice(index,1);
    };

    //添加关键词和分类
    $scope.addKeyword=function()
    {
        if($scope.additionKeyword==undefined||$scope.additionKeyword==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.keyWord.push($scope.additionKeyword);
            $('#myModal_addKeyword').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.newArticleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel').modal('toggle');
        }
    };
    $scope.addPictureToEditor=function(picUrl){
        //console.log(picUrl);
        var text='<img src="'+picUrl+'">';
        $scope.newArticleData.content=text+$scope.newArticleData.content;
//        $scope.$apply();//相当于刷新一下scope 不然内容加不上
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
        var imgObjPreview=document.getElementById("imgPreview");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview">'
            +'</div>';

        document.getElementById("previewFrame").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        document.form_newArticle.action=$scope.projectActionName;
        console.log(document.form_newArticle.action);
        $('#myUploadImgForm').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    $scope.addPicUrl=function()
    {
        var url = $scope.getPicUrl();
        $scope.pushPicUrl(url);
        $scope.addImgToEditorContent(url);
        $scope.turnOffUploadModal();
        $scope.deletePreviewFrame();
    };

    $scope.getPicUrl=function()
    {
        var url=document.getElementById("myIFrameID").contentDocument.body.innerHTML;
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
        $('#myModal_addIMG').modal('toggle');
    };
    //关于上传图片的----------------------------------------------------------------------------------------------
}]);






