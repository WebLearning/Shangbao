/**
 managementCtrl.js
 **/
angular.module("Dashboard").controller("managementCtrl",["$scope","$http",function($scope,$http){

    $scope.refreshLog=function(){
        $scope.getAppNames();
    };

    $scope.appNames=[];
    $scope.getAppNames=function(){
        var url=$scope.projectName+'/channel/channels';
        $http.get(url).success(function(data){
            //console.log(data);
            if(data.length>0){
                for(i=0;i<data.length;i++){
                        $scope.appNames.push(data[i]);
                        $scope.getModelNames(data[i].englishName)
                }
            }
        });
    };
    $scope.getAppNames();

    $scope.modelNames=[];
    $scope.getModelNames=function(channelName){
        var url=$scope.projectName+'/channel/'+channelName+'/channels';
        $http.get(url).success(function(data){
            //console.log(data);
            if(data.length>0){
                for(i=0;i<data.length;i++){
                    $scope.modelNames.push(data[i]);
                }
            }
        });
    };
}]);