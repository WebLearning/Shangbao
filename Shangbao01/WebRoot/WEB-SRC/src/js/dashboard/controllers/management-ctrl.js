/**
 managementCtrl.js
 **/
angular.module("Dashboard").controller("managementCtrl",["$scope","$http",function($scope,$http){

    $scope.testLog=function()
    {
        console.log($scope.appNames);
        console.log($scope.modelNamesSet);
    };

    $scope.refreshLog=function(){
        $scope.getAppNames();
    };

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

//    $scope.modelNames=[];

    $scope.addModelNames=function(channelName){
//        console.log(channelName);

        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        $http.get(url).success(function(data){
//            console.log(data);
            $scope.modelNamesSet[channelName]=data;
        });
    };

    $scope.getModelNames=function(iModelNamesSet,englishName)
    {
        return iModelNamesSet[englishName];
    };


}]);