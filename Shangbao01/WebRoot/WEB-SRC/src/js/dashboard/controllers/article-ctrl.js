

//, ["ng.ueditor"]
angular.module("Dashboard").controller("articleCtrl", ["$scope","$http", function ($scope,$http) {

    /* $http.get('http://localhost:8080/Shangbao01/user/users')
     .success(function(data) {
     $scope.editorContent = data;
     }); */

    $scope.recvData={
        title:"成绵乐客专“蓄势待发” 应急救援演练10天",
        subTitle:"即将进入运行试验阶段 全真模拟开行动车组的情形",
        keyWord:["成绵乐","动车组","救援演练"],
        author:"杨一",
        summary:"成绵乐客专“蓄势待发”",
        content:'<p style="text-indent: 2em;">据了解，为期10天的应急救援演练基本上覆盖了成绵乐客专上的各个车站与区段，应急救援演练的项目至少达到50项以上。200公里时速 模拟运营真实状况 在应急救援演练项目之外，还将专门模拟列车开通运营，模仿运行时刻表，进行行车试验。</p><p style="text-indent: 2em;"><img src="img/spiderMan.jpg"/></p><p style="text-indent: 2em;">据介绍，通过运行图参数测试，掌握全程运行时分，行车试验则对各项设备进行考验，检验各系统和整体系统的稳定性及能力，并且让客运、车站、通信等各个专业的人员得到实作培训，熟悉各种规章制度，熟悉工作流程。</p><p style="text-indent: 2em;"><img src="img/shitMan.jpg"/></p><p style="text-indent: 2em;">据了解，这部分试验将采用CRH1A或CRH2A型动车组，动车组将模拟北向、南向行驶，或全程拉通行驶的运行方式，每种模拟方式基本都是站站停，行进中保持时速200公里，从江油至峨眉山大约需要2个多小时，成都至江油、成都至峨眉山大约各在1个小时左右。</p>',
        time:new Date(),
        channel:["原创","本地","热门"],
        picturesUrl:["http://localhost:8080/Shangbao01/WEB-SRC/src/img/shitMan.jpg"],
        level:"一级",
        from:"成都商报"
    };

    $scope.articleData={
        title:"",
        subTitle:"",
        keyWord:new Array(),
        author:"",
        summary:"",
        content:'',
        time:null,
        channel:[],
        picturesUrl:[],
        level:"",
        from:""
        };

    $scope.getEditorContent=function()
    {
        //导入数据
        for(p in $scope.recvData){
            if(p=="keyWord"||p=="channel"||p=="picturesUrl"){
                for(i in $scope.recvData[p]){
                    $scope.articleData[p][i]=$scope.recvData[p][i];
                }
            }else{
                $scope.articleData[p]=$scope.recvData[p];
            }
        }
    };

    $scope.testLog=function()
    {
        console.log($scope.recvData);
        console.log($scope.articleData);
    };


    $scope.changeSort=function(sortName)
    {
        $scope.recvData.sorts[sortName]=!$scope.recvData.sorts[sortName];
    };

    $scope.saveArticle=function(){
        var jsonString=JSON.stringify($scope.articleData);
        $http.post('http://localhost:8080/Shangbao01/article/newArticle',jsonString).success(function(data) {
            console.log("success save testData");
        });
    };

    $scope.putArticle=function(){
        var jsonString=JSON.stringify($scope.articleData);
        $http.put('http://localhost:8080/Shangbao01/article/newArticle',jsonString).success(function(data) {
            console.log("success put testData");
        });
    };




    //关于上传图片的----------------------------------------------------------------------------------------------

    //当input file选择的文件有变化时
    $scope.onInputChange=function(inputFileObj)
    {
        $scope.previewIMG(inputFileObj);
        $scope.disableConfirmButton();
    };

    //预览图片

    $scope.previewIMG=function(inputFileObj)
    {
        if(inputFileObj.value==""){
            $scope.deletePreviewFrame();
        }else{
            $scope.addPreviewFrame();
            $scope.loadPreviewIMG(inputFileObj);
        }
    };

    $scope.loadPreviewIMG=function(obj)
    {
        var docObj = obj;
        var preViewUrl = window.URL.createObjectURL(docObj.files[0]);
        //    console.log(preViewUrl);
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
        document.getElementById("myUploadImgForm").innerHTML='<input type="file" name="file" accept="image/*" onchange="angular.element(this).scope().onInputChange(this)"/>';
    };

    //上传图片
    $scope.uploadImg=function()
    {
        $('#myUploadImgForm').submit();
        $scope.enableConfirmButton();
    };

    //添加图片到ueditor内容
    $scope.addImgToEditorContent=function(text){
        text="http://localhost:8080/Shangbao01/WEB-SRC"+text.substr(2);
        text='<img src="'+text+'">';
        $scope.recvData["textContent"]=text+$scope.recvData["textContent"];
        $scope.$apply();//相当于刷新一下scope 不然内容加不上
    };

    //添加图片到编辑页面
    $scope.showImg=function()
    {
        var text = document.getElementById("myIFrameID").contentWindow.document.body.innerText;
        text=text.substr(8);
        text=".."+text;
        //    console.log(text);

        $scope.addViewIMG(text);
    };

    $scope.addViewIMG=function(text)
    {
        $scope.addViewFrame();
        $scope.loadViewIMG(text);
        $scope.turnOffUploadModal();
        $scope.addImgToEditorContent(text);
    };

    $scope.loadViewIMG=function(text)
    {
        var relativeURL=text;
        var imgObjPreview=document.getElementById("imgView");
        imgObjPreview.src = relativeURL;
    };

    $scope.addViewFrame=function()
    {
        var tempHtml='<div class="thumbnail">'
            +'<button type="button" class="close" onclick="angular.element(this).scope().deleteViewFrame()"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>'
            +'<img id="imgView">'
            +'</div>';

        document.getElementById("viewFrame").innerHTML=tempHtml;
    };

    $scope.deleteViewFrame=function() {
        document.getElementById("previewFrame").innerHTML = "";
    };

    //确认按钮的改变
    $scope.enableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-primary" onclick="angular.element(this).scope().showImg()">确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    $scope.disableConfirmButton=function()
    {
        var tempString='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>'
            +'<button type="button" class="btn btn-success" onclick="angular.element(this).scope().uploadImg()">上传</button>'
            +'<button type="button" class="btn btn-default" disabled>确认</button>';

        document.getElementById("modalFooterID").innerHTML=tempString;
    };

    //关闭上传框
    $scope.turnOffUploadModal=function()
    {
        $('#myModal_addIMG').modal('toggle');
    };

    //关于上传图片的----------------------------------------------------------------------------------------------


}]);




//$('#intro').submit();





