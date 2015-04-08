function readAndZanCtrl($scope, $http) {

	// 通过解析当前url获取articleID
	$scope.url = {
		value : window.location.href,
	};
	var oldUrl = $scope.url.value;
	// var oldUrl
	// ="http://202.115.17.218:8080/Shangbao01/app/iphone/articledetail/139";
	var articleID = oldUrl.substring(oldUrl.lastIndexOf("articledetail") + 14);
	// var articleID = 306;
	var basic = oldUrl.substring(0, oldUrl.lastIndexOf("app") + 4);

	var url_zan_load = basic + "js/getlike/" + articleID;
	var url_zan_add = basic + "js/addlike/" + articleID;

	var url_read_load = basic + "js/getclick/" + articleID;
	var url_read_add = basic + "js/addclick/" + articleID;

	var pictureBasic = oldUrl.substring(0, oldUrl.lastIndexOf("app"));
	$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan.png";

	var cookieName_zan = getCookie(articleID + "zan");
	if (cookieName_zan == null) {
		$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan.png";
		console.log("not not");
	} else if (cookieName_zan == "true") {
		$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan2.png";
		console.log("yes yes");
	} else {
		alert("error!!!");
	}

	console.log(url_zan_load);
	$http.get(url_zan_load).success(function(data) {
		// alert("jsonp come in");
		// console.log(data);
		console.log(data);
		$scope.zanNum = data;
		console.log($scope.zanNum);
	});

	

	$scope.clickNum = 0;
	var cookieName_read = getCookie(articleID + "read");
	if (cookieName_read == null) {
		console.log(url_read_add);
		//$scope.clickNum = Number(Number($scope.clickNum) + 1);
		$http.put(url_read_add).success(function(response) {
			console.log(response);
			$scope.clickNum =response;
		});

		addCookie(articleID + "read", "true", 99999999);
	} else if (cookieName_read == "true") {
		console.log("---read num added---");
		$http.get(url_read_load).success(function(response) {
		$scope.clickNum = response;
		console.log($scope.clickNum);
	});
	} else {
		alert("error!!!");
	}

	$scope.zanAdd = function(zanNum, pictureUrl) {
		if (pictureUrl == pictureBasic + "WEB-SRC/src/img/zan.png") {
			$scope.zanNum = Number(Number(zanNum) + 1);
			$scope.pictureUrl = pictureBasic + "WEB-SRC/src/img/zan2.png";

			console.log(url_zan_add);
			$http.put(url_zan_add).success(function(response) {
				console.log(response);
				// $scope.zanNum = response;
			});
			addCookie(articleID + "zan", "true", 99999999);
		}
	};

};
function getCookie(name) {
	console.log("get cookie ----come in");
	var strCookie = document.cookie;
	console.log(strCookie);
	var arrCookie = strCookie.split("; ");
	for (var i = 0; i < arrCookie.length; i++) {
		var arr = arrCookie[i].split("=");
		if (arr[0] == name) {
			console.log(arr[0]);
			console.log(arr[1]);
			return "true";
		}
	}
	return null;
};

function addCookie(name, value, expiresHours) {
	var cookieString = name + "=" + escape(value);

	if (expiresHours > 0) {
		var date = new Date();
		date.setTime(date.getTime + expiresHours * 3600 * 1000);
		cookieString = cookieString + "; expires=" + date.toGMTString();
	}
	document.cookie = cookieString;
};