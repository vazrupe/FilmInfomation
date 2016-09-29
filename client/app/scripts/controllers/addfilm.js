'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:AddfilmCtrl
 * @description
 * # AddfilmCtrl
 * Controller of the filmApp
 */
angular.module('filmApp')
  .controller('AddFilmCtrl', ['$scope', '$http', '$location', function ($scope, $http, $location) {
	var EndURL = '/all/film';
	$scope.iso = {
	  value: 0,
	  options: {
	    stepsArray:[0]
	  }
	};
    
    $scope.makers = [];
	
	$scope.maker_complete = function(maker) { $scope.maker=maker; }
	
    $scope.init = function() {
    	$http({
			method: 'GET',
			url: 'http://film.codefict.com/server/FilmInitData',
			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
		}).success(function(data, status, headers, config) {
			if (data) {
				var makerList = data.maker;
				$scope.makers = makerList;
				
				var isoList = data.iso;
				$scope.iso.options.stepsArray = isoList;
				$scope.iso.value = isoList.indexOf(100);
			}
		}).error(function(data, status, headers, config){
			console.log(status);
		});
    }
    
    $scope.submit = function() {
    	var FilmData = {
			name:$scope.name,
			maker:$scope.maker,
			iso:$scope.iso.options.stepsArray[$scope.iso.value]
		};
		$http({
			method: 'POST',
			url: 'http://film.codefict.com/server/AddFilm',
			data: $.param(FilmData),
			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
		}).success(function(data, status, headers, config) {
			if (data) {
				if (data=="success") {
					$location.path(EndURL);
				}
				else {
					$scope.error_msg="이미 존재하는 필름입니다";
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
