'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:AddpictureCtrl
 * @description
 * # AddpictureCtrl
 * Controller of the filmApp
 */
angular.module('filmApp')
  .controller('AddpictureCtrl', ['$scope', '$location', '$http', 'TokenService', 'UserFilmId', function ($scope, $location, $http, TokenService, UserFilmId) {
	  var EndURL = '/user/film/'+UserFilmId;
		$scope.iso = {
		  value: 0,
		  options: {
			stepsArray:[]
		  }
		};
		$scope.ev = {
		  value: 0,
		  options: {
			floor: -2,
			ceil: 2,
		    step: 1,
		  }
		};
		$scope.zoom = {
		  value: 10,
		  options: {
			floor: 10,
			ceil: 10,
		    step: 1,
		    translate: function(value) {
		      return 'x' + value/10;
		    }
		  }
		};
		$scope.diaphragm = {
		  value: 0,
		  options: {
			stepsArray:[]
		  }
		};
		$scope.range = {
		  value: 100,
		  options: {
			floor: 50,
			ceil: 2500,
		    step: 5,
		    disabled: false,
		    translate: function(value) {
		      return value/100 + 'm';
		    }
		  }
	    };
		$scope.shutterspeed = {
	      value: 0,
	      options: {
	        stepsArray:[]
	      }
	    };
	    
		$scope.userFilmNumber = UserFilmId;
	    $scope.makers = [];
		$scope.mounts = [];
		
		$scope.hasRangeMacro = false;
		$scope.hasRangeUnlimited = false;
		$scope.range.type="slide";
		
		$scope.rangeTypeChange = function() {
		  if($scope.range.type=="slide") {
			  $scope.range.options.disabled=false;
		  }
		  else {
			  $scope.range.options.disabled=true;
		  }
		};
		
		$scope.maker_complete = function(maker) { $scope.maker=maker; }
		$scope.mount_complete = function(mount) { $scope.mount=mount; }
		
	    $scope.init = function() {
	    	var token = TokenService.getToken('/add/picture/'+UserFilmId);
	    	
	    	var initData = {
	    			token:token,
	    			id:UserFilmId
	    	};
	    	if(token) {
		    	$http({
					method: 'GET',
					url: 'http://film.codefict.com/server/PictureInitData',
					params: initData,
					headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
				}).success(function(data, status, headers, config) {
					if(data=="Invalid Token") TokenService.resetToken();
					if (data == "Not Mount Lens") {
						$location.url('/user/camera');
					}
					else if(data != "failed") {
						var ISO = data.iso;
						var ISOList = data.iso_list;
						$scope.iso.value = ISOList.indexOf(ISO);
						$scope.iso.options.stepsArray = ISOList;
						
						var shutterSpeedList = data.shutter_speed_list;
						$scope.shutterspeed.value = shutterSpeedList.indexOf("1/250");
						$scope.shutterspeed.options.stepsArray = shutterSpeedList;
						
						var FList = data.f_list;
						$scope.diaphragm.value = FList.indexOf("2.8");
						$scope.diaphragm.options.stepsArray = FList;
						
						var maxZoom = data.max_zoom*10;
						$scope.zoom.value = 10;
						$scope.zoom.options.ceil = maxZoom;
						
						var minFocus = data.min_focus*100;
						var maxFocus = data.max_focus*100;
						var macroFocus = data.macro_focus;
						var unlimitedFocus = data.unlimited_focus;
						
						$scope.range.value = 100;
						$scope.range.options.floor = minFocus;
						$scope.range.options.ceil = maxFocus;
						
						$scope.hasRangeMacro = (macroFocus=="Y");
						$scope.hasRangeUnlimited = (unlimitedFocus=="Y");
					}
				}).error(function(data, status, headers, config){
					if(data=="Invalid Token") TokenService.resetToken();
				});
	    	}
	    }
	    
	    $scope.submit = function() {
	    	var token = TokenService.getToken('/add/picture/'+UserFilmId);
	    	
	    	var LensData = {
	    		token:token,
	    		id:UserFilmId,
	    		target:$scope.target,
	    		shutter_speed:$scope.shutterspeed.options.stepsArray[$scope.shutterspeed.value],
	    		diaphragm:$scope.diaphragm.options.stepsArray[$scope.diaphragm.value],
	    		range:1,
	    		zoom:$scope.zoom.value/10,
	    		ev:$scope.ev.value,
	    		iso:$scope.iso.options.stepsArray[$scope.iso.value]
			};
	        if($scope.range.type=="slide") LensData.range = $scope.range.value/100;
	        else {
	        	LensData.range = $scope.range.type;
	        }
	    	if (token) {
				$http({
					method: 'POST',
					url: 'http://film.codefict.com/server/AddPicture',
					data: $.param(LensData),
					headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
				}).success(function(data, status, headers, config) {
					if (data) {
						if (data=="success") {
							$location.path(EndURL);
						}
						else {
							$scope.error_msg="정보를 다시 확인해주세요";
							$("#adderr").removeClass('hidden');
							return;
						}
					}
				}).error(function(data, status, headers, config){
					console.log(status);
				});
	    	}
		}
		
	    $scope.close = function() {
			$location.path(EndURL);
		}
  }]);
