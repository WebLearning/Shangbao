// angular.module("Dashboard", []);

angular.module("Dashboard").controller("generalViewCtrl", ["$scope","$http", function ($scope, $http) {

          // $scope.currentMenu = "menu1";
          /* $scope.selectMenu = function (menu) {
              $scope.currentMenu = menu;
          } */

		  /* $http.get('http://localhost:8080/Shangbao01/user/users')
			.success(function(data) {
				$scope.generalViewSections = data;
			}); */

		  $scope.generalViewSections=[
                        {"name":"热点",
						"content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题","新闻6 标题","新闻7 标题","新闻8 标题","新闻9 标题"]},
                        {"name":"本地",
						"content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
						{"name":"原创",
						"content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
						{"name":"娱乐",
						"content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
						{"name":"体育",
						"content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]},
						{"name":"财经",
						"content":["新闻1 标题","新闻2 标题","新闻3 标题","新闻4 标题","新闻5 标题"]}
						];
      }]);