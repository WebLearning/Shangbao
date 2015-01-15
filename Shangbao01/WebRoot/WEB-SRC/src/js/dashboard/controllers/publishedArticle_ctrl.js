/**
 newCrawler-Ctrl
 **/

angular.module("Dashboard").controller("publishedArticleCtrl", ["$scope","$http", function ($scope,$http) {

    $scope.recvData={
        title:"成绵乐客专“蓄势待发” 应急救援演练10天",
        subTitle:"即将进入运行试验阶段 全真模拟开行动车组的情形",
        keyWord:["成绵乐","动车组","救援演练"],
        author:"杨一",
        summary:"摘要：成绵乐客专“蓄势待发” 应急救援演练10天，即将进入运行试验阶段 全真模拟开行动车组的情形",
        content:'<p style="text-indent: 2em;">据了解，为期10天的应急救援演练基本上覆盖了成绵乐客专上的各个车站与区段，应急救援演练的项目至少达到50项以上。200公里时速 模拟运营真实状况 在应急救援演练项目之外，还将专门模拟列车开通运营，模仿运行时刻表，进行行车试验。</p><p style="text-indent: 2em;"><img src="img/spiderMan.jpg"/></p><p style="text-indent: 2em;">据介绍，通过运行图参数测试，掌握全程运行时分，行车试验则对各项设备进行考验，检验各系统和整体系统的稳定性及能力，并且让客运、车站、通信等各个专业的人员得到实作培训，熟悉各种规章制度，熟悉工作流程。</p><p style="text-indent: 2em;"><img src="img/shitMan.jpg"/></p><p style="text-indent: 2em;">据了解，这部分试验将采用CRH1A或CRH2A型动车组，动车组将模拟北向、南向行驶，或全程拉通行驶的运行方式，每种模拟方式基本都是站站停，行进中保持时速200公里，从江油至峨眉山大约需要2个多小时，成都至江油、成都至峨眉山大约各在1个小时左右。</p>',
        time:new Date(),
        channel:["原创","本地","热门"],
        picturesUrl:["http://localhost:8080/Shangbao01/WEB-SRC/src/img/spiderMan.jpg","http://localhost:8080/Shangbao01/WEB-SRC/src/img/shitMan.jpg"],
        level:"一级",
        from:"成都商报"
    };

    $scope.getEditorContent=function()
    {
        //导入数据
        for(p in $scope.articleData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                for(i in $scope.recvData[p]){
                    $scope.articleData[p][i]=$scope.recvData[p][i];
                }
            }else{
                $scope.articleData[p]=$scope.recvData[p];
            }
        }
    };
    $scope.goPublished=function()
    {
        $scope.clearArticle();
        document.getElementById("publishedArticle").className="tab-pane";
        document.getElementById("published").className="tab-pane active";
        document.getElementById("publishedSidebarID").className="sidebar-list";
    };
    $scope.testLog=function()
    {
        $scope.calculateWords();
        console.log($scope.recvData);
        console.log($scope.articleData);
    };

    //footer的3个按钮的操作
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

    $scope.saveArticle=function(){
        $scope.calculateWords();
        var jsonString=JSON.stringify($scope.articleData);
        $http.post($scope.projectName+'/article/newArticle',jsonString).success(function(data) {
            alert("保存文章成功");
        });
    };

    $scope.putArticle=function(){
        $scope.calculateWords();
        var jsonString=JSON.stringify($scope.articleData);
        $http.put($scope.projectName+'/article/newArticle',jsonString).success(function(data) {
            alert("提交审核文章成功");
        });
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
            $('#myModal_addKeyword_pending').modal('toggle');
        }
    };

    $scope.addChannel=function()
    {
        if($scope.additionChannel==undefined||$scope.additionChannel==""){
            alert("没有任何输入");
        }else{
            $scope.articleData.channel.push($scope.additionChannel);
            $('#myModal_addChannel_pending').modal('toggle');
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
        var imgObjPreview=document.getElementById("imgPreview");
        imgObjPreview.src = preViewUrl;
    };

    $scope.addPreviewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deletePreviewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgPreview">'
            +'</div>';

        document.getElementById("previewFrame_pending").innerHTML=tempHtml;
    };

    $scope.deletePreviewFrame=function()
    {
        document.getElementById("previewFrame_pending").innerHTML="";
    };

    $scope.refreshImgInput=function()
    {
        document.getElementById("myUploadImgForm_pending").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传按钮的改变（主要）
    $scope.disableUploadButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-default" disabled>上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_pending").innerHTML=tempString;
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $('#myUploadImgForm_pending').submit();
        $scope.enableConfirmButton();
    };

    //确认按钮的改变（主要）
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().addPicUrl()">确认</button>';

        document.getElementById("modalFooterID_pending").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID_pending").innerHTML=tempString;
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
        var url = document.getElementById("myIFrameID_pending").contentWindow.document.body.innerText;
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
        $('#myModal_addIMG_pending').modal('toggle');
    };


    //关于上传图片的----------------------------------------------------------------------------------------------


}]);
