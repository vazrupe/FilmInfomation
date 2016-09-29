'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:AddcameraCtrl
 * @description
 * # AddcameraCtrl
 * Controller of the filmApp
 */
angular.module('filmApp')
  .controller('AddCameraCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
	var EndURL = '/all/camera';
	$scope.shutter = {
	  minValue: 0,
	  maxValue: 0,
	  options: {
	    stepsArray:[]
	  }
	};
    
    $scope.makers = [];
	$scope.mounts = [];
	
	$scope.maker_complete = function(maker) { $scope.maker=maker; }
	$scope.mount_complete = function(mount) { $scope.mount=mount; }
	
    $scope.init = function() {
    	$http({
			method: 'GET',
			url: 'http://film.codefict.com/server/CameraInitData',
			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
		}).success(function(data, status, headers, config) {
			if (data) {
				var makerList = data.maker;
				$scope.makers = makerList;
				
				var mountList = data.mount;
				$scope.mounts = mountList;
				
				var shtList = data.speed;
				$scope.shutter.options.stepsArray = shtList;
				$scope.shutter.minValue = shtList.indexOf('1/1000');
				$scope.shutter.maxValue = shtList.indexOf('1');
			}
		}).error(function(data, status, headers, config){
			console.log(status);
		});
    }
    
    $scope.submit = function() {
    	var CameraData = {
			name:$scope.name,
			maker:$scope.maker,
			mount:$scope.mount,
			shutter_min:$scope.shutter.options.stepsArray[$scope.shutter.minValue],
			shutter_max:$scope.shutter.options.stepsArray[$scope.shutter.maxValue]
		};
		$http({
			method: 'POST',
			url: 'http://film.codefict.com/server/AddCamera',
			data: $.param(CameraData),
			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
		}).success(function(data, status, headers, config) {
			if (data) {
				if (data=="success") {
					$location.path(EndURL);
				}
				else {
					$scope.error_msg="이미 존재하는 카메라입니다";
					$("#adderr").removeClass('hidden');
					return;
				}
			}
		}).error(function(data, status, headers, config){
			console.log(status);
		});
	}
	
    $scope.close = function() {
		$location.path(EndURL);
	}
  }]);
