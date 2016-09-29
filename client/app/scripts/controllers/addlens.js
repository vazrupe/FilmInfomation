'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:AddlensCtrl
 * @description
 * # AddlensCtrl
 * Controller of the filmApp
 */
angular.module('filmApp')
  .controller('AddLensCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
    var EndURL = '/all/lens';
	$scope.zoom = {
	  value: 10,
	  options: {
		floor: 10,
		ceil: 100,
	    step: 1,
	    translate: function(value) {
	      return 'x' + value/10;
	    }
	  }
	};
	$scope.diaphragm = {
	  minValue: 0,
	  maxValue: 0,
	  options: {
		stepsArray:[]
	  }
	};
	$scope.range = {
	  minValue: 50,
	  maxValue: 900,
	  options: {
		floor: 50,
		ceil: 2500,
	    step: 5,
	    translate: function(value) {
	      return value/100 + 'm';
	    }
	  }
    };
    
    $scope.makers = [];
	$scope.mounts = [];
	
	$scope.rangeMacro = 'N';
	$scope.rangeUnlimited = 'Y';
	
	$scope.maker_complete = function(maker) { $scope.maker=maker; }
	$scope.mount_complete = function(mount) { $scope.mount=mount; }
	
    $scope.init = function() {
    	$http({
			method: 'GET',
			url: 'http://film.codefict.com/server/LensInitData',
			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
		}).success(function(data, status, headers, config) {
			if (data) {
				var makerList = data.maker;
				$scope.makers = makerList;
				
				var mountList = data.mount;
				$scope.mounts = mountList;
				
				var FList = data.flist;
				$scope.diaphragm.options.stepsArray = FList;
				$scope.diaphragm.minValue = FList.indexOf('2.8');
				$scope.diaphragm.maxValue = FList.indexOf('16');
			}
		}).error(function(data, status, headers, config){
			console.log(status);
		});
    }
    
    $scope.submit = function() {
    	var LensData = {
    		name:$scope.name,
    		maker:$scope.maker,
    		mount:$scope.mount,
    		zoom:$scope.zoom.value/10,
    		f_min:$scope.diaphragm.options.stepsArray[$scope.diaphragm.minValue],
    		f_max:$scope.diaphragm.options.stepsArray[$scope.diaphragm.maxValue],
    		focus_min:$scope.range.minValue/100,
    		focus_max:$scope.range.maxValue/100,
    		focus_macro:$scope.rangeMacro,
    		focus_unlimited:$scope.rangeUnlimited
		};
		$http({
			method: 'POST',
			url: 'http://film.codefict.com/server/AddLens',
			data: $.param(LensData),
			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
		}).success(function(data, status, headers, config) {
			if (data) {
				if (data=="success") {
					$location.path(EndURL);
				}
				else {
					$scope.error_msg="이미 존재하는 렌즈입니다";
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
