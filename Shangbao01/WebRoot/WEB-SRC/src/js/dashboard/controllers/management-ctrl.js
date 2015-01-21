/**
 managementCtrl.js
 **/
angular.module("Dashboard").controller("managementCtrl",["$scope","$http",function($scope,$http){

    $scope.testLog=function()
    {
        console.log($scope.appNames);
        console.log($scope.modelNamesSet);
        console.log($scope.newChannelData);
        console.log($scope.newChannelDataState);
    };

    $scope.refreshManagePage=function(){
        $scope.getAppNames();
        $scope.checkNewChannelDataState();
    };

    //显示----------------------------------------------------------------------------------------------------------------
    $scope.appNames=[];
    $scope.modelNamesSet={};

    $scope.getAppNames=function(){
        var url=$scope.projectName+'/channel/channels';
        $http.get(url).success(function(data){
//            console.log(data);
            $scope.appNames=data;
            getModelNamesSet(data);
        });
    };
    $scope.getAppNames();

    function getModelNamesSet(data){
        $scope.modelNamesSet={};
        for(i=0;i<data.length;i++){
            $scope.addModelNames(data[i].englishName);
        }
    }

    $scope.addModelNames=function(channelName){
        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        $http.get(url).success(function(data){
            $scope.modelNamesSet[channelName]=data;
        });
    };

    $scope.getModelNames=function(iModelNamesSet,englishName)
    {
        return iModelNamesSet[englishName];
    };

    //操作----------------------------------------------------------------------------------------------------------------

    //添加分类---------------------------------------------

    $scope.newChannelData={
        channelName:"", //栏目名称
        summary:"", //栏目介绍
        related:"", //（如果是顶级分类，为空；如果是子分类，填写父分类的channelName）
        englishName:"" // 栏目的英文名称
    };

    $scope.newChannelDataState="";
    $scope.relatedSelection_Class="form-group sr-only";

    $scope.checkNewChannelDataState=function()
    {
        //console.log($scope.newChannelDataState);
        if($scope.newChannelDataState=="下级分类"){
            $scope.relatedSelection_Class="form-group";
        }else{
            $scope.relatedSelection_Class="form-group sr-only";
            $scope.newChannelData.related="";
        }
    };

    $scope.addChannel=function()
    {
        var url=$scope.projectName+'/channel/'+ulrfyChannelState($scope.newChannelDataState);//Father|Son|Activity
        //console.log(url);
        //console.log($scope.newChannelData);
        $http.post(url,$scope.newChannelData).success(function(data){
            if(data=="OK"){
                alert("添加成功");
                $('#myModal_newChannel').modal('toggle');
                $scope.clearNewChannel();
                $scope.refreshManagePage();
            }else{
                alert("添加失败");
            }
        });
    };

    function ulrfyChannelState(state){
        if(state=="顶级分类"){return "Father"}
        else if(state=="下级分类"){return "Son"}
        else if(state=="活动"){return "Activity"}
        else{alert("Error:no such channel state")}
    }

    $scope.clearNewChannel=function(){
        $scope.newChannelData={
            channelName:"", //栏目名称
            summary:"", //栏目介绍
            related:"", //（如果是顶级分类，为空；如果是子分类，填写父分类的channelName）
            englishName:"" // 栏目的英文名称
        };
        $scope.newChannelDataState="";
    };

    //删除分类---------------------------------------------

    $scope.deleteChannel=function(channelName,englishName,channelState)
    {
        if(channelState=="Father"){
            if (confirm("确认删除该分类及其下级分类？")==true){
                reallyDeleteChannel(channelName,englishName,channelState);
            }
        }else if(channelState=="Son"){
            if (confirm("确认删除该分类？")==true){
                reallyDeleteChannel(channelName,englishName,channelState);
            }
        }

        function reallyDeleteChannel(channelName,englishName,channelState){
            var url=$scope.projectName+'/channel/delete/'+channelState;//Father|Son|Activity
            var channelInfo={};
            channelInfo['channelName']=channelName;
            channelInfo['englishName']=englishName;
            $http.post(url,channelInfo).success(function(data){
                if(data=="OK"){
                    alert("删除成功");
                    $scope.refreshManagePage();
                }else{
                    alert("删除失败");
                }
            });

        }

    };

    //移动分类---------------------------------------------

    $scope.upMove=function(father,state,index)
    {
        if((index-1)==0){
            alert("已到达顶部");
        }else{
            var url=$scope.projectName+'/channel/'+father+'/'+state+'/swap/'+index+'/'+(index-1);
            //console.log(url);
            $http.put(url).success(function(data){
                if(data=='done'){
                    $scope.refreshManagePage();
                }else{
                    alert('上移失败');
                }
            });
        }
    };

    $scope.downMove=function(father,state,index)
    {
        var maxIndex;
        if(father=='top'){
            maxIndex=$scope.appNames.length;
        }else{
            maxIndex=($scope.getModelNames($scope.modelNamesSet,father)).length;
        }

        if(index==maxIndex){
            alert("已到达底部");
        }else{
            var url=$scope.projectName+'/channel/'+father+'/'+state+'/swap/'+index+'/'+(index+1);
            //console.log(url);
            $http.put(url).success(function(data){
                if(data=='done'){
                    $scope.refreshManagePage();
                }else{
                    alert('下移失败');
                }
            });
        }
    };
}]);