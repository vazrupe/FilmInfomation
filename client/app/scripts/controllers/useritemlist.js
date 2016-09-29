'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:UseritemlistCtrl
 * @description
 * # UseritemlistCtrl
 * Controller of the filmApp
 */
angular.module('filmApp')
  .controller('UseritemlistCtrl', ['$scope', '$location', '$http', 'TokenService', 'ModalService', 'itemType', function ($scope, $location, $http, TokenService, ModalService, itemType) {
	    var showSelectModal = function(option, callback) {
	  	  ModalService.showModal({
	  	    templateUrl: "views/selectModal.html", controller: "SelectmodalCtrl", inputs: { options:option }
	  	  }).then(function(modal) {
	  	    modal.element.modal(); modal.close.then(function(result) {callback(result);});
	  	  });
	  	}
	      var AjaxService = function(type, url, sendData, success_callback) {
	      	if (type=="GET") {
	      		$http({
	      			method: 'GET',
	      			url: url,
	          		params: sendData,
	      			headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
	      		})
	  	    	.success(function(data, status, headers, config) { success_callback(data, status, headers, config); })
	  	    	.error(function(data, status, headers, config){ console.log(status); });
	      	}
	      	else if(type=="POST") {
	      		$http({
	  	    		method: 'POST',
	  	    		url: url,
	  	    		data: $.param(sendData),
	  	    		headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
	  	    	})
	  	    	.success(function(data, status, headers, config) { if(data=="Invalid Token") TokenService.resetToken(); success_callback(data, status, headers, config); })
	  	    	.error(function(data, status, headers, config){ console.log(status); });
	      	}
	      }
	      
	  	var types = ['camera', 'lens', 'film'];
	  	var typeKR = {camera:'카메라', lens:'렌즈', film:'필름'};
	  	
	  	if (types.indexOf(itemType)<0)
	  		$location.url('/');
	  	else
	  		$location.url('/all/'+itemType);

	  	$scope.itemType = itemType;
	      $scope.title = "모든 " + typeKR[itemType];
	      
	      $scope.itemList = [];
	      $scope.userEnableItemList = [];
	      
	      $scope.contentsKeyRempping = {
	      	name:"이름",
	      	maker:"제조사",
	      	
	      	mount:"마운트",
	      	
	      	iso:"감도",
	      	
	      	shutter_min:"최소 셔터 스피드",
	  		shutter_max:"최대 셔터 스피드",
	      	
	      	f_min:"조리개 최소 크기",
	      	f_max:"조리개 최대 크기",
	      	
	      	zoom:"최대 줌 배율",
	      	
	      	focus_min:"최소 초점 거리",
	          focus_max:"최대 초점 거리",
	          focus_macro:"MACRO 초점 거리",
	          focus_unlimited:"무한대 초점 거리"
	      };
	      
	      var loadInitList = false;
	      var loadEnableList = false;
	      var allInsertedFilm = false;
	      var addButtonRemove = function() {
	      	if (loadInitList && loadEnableList) {
	      	  for(var i=0; i<$scope.itemList.length; i++) {
	  	    	$scope.itemList[i].enabled = ($scope.userEnableItemList.indexOf($scope.itemList[i].id)>=0);
	  	      }
	      	}
	      	
	      	if (loadInitList && allInsertedFilm) {
	      	  for(var i=0; i<$scope.itemList.length; i++) {
	  		    $scope.itemList[i].enabled = true;
	  		  }
	      	}
	      }
	      
	      $scope.init = function() {
	      	var Type = {type:itemType};
	      	
	      	// Base Data Load
	      	AjaxService("GET", 'http://film.codefict.com/server/AllItemList', Type, function(data, status, headers, config) {
	  			if (data) {
	  				$scope.itemList = data;
	  				
	  				loadInitList = true;
	  				addButtonRemove();
	  			}
	  		});

	      	Type.token = TokenService.getToken($location.url());
	      	
	      	// Contains Item in User
	      	if (itemType!='film') {
	      		AjaxService("GET", 'http://film.codefict.com/server/UserEnableList', Type, function(data, status, headers, config) {
	          		if (data) {
	      				$scope.userEnableItemList = data;

	      				loadEnableList = true;
	      				addButtonRemove();
	      			}
	      		});
	      	}
	      	else {
	      		AjaxService("GET", 'http://film.codefict.com/server/NotInsertedCameraList', Type, function(data, status, headers, config) {
	  	    		if (data.length==0) {
	  	    		  allInsertedFilm = true;
	  				  addButtonRemove();
	  	    		}
	      		});
	      	}
	      }
	      
	      // List & Button Event
	      var btnEvent = false;

	      $scope.hasUserItem = function(item) { return !item.enabled; }
	      $scope.addItem = function(item) {
	      	btnEvent = true;
	      	
	      	var sendUrl = 'http://film.codefict.com/server/';
	      	
	      	if (itemType=='film') {
	      		var getUserCameraList = {token:TokenService.getToken($location.url())};
	      		AjaxService("GET", 'http://film.codefict.com/server/NotInsertedCameraList', getUserCameraList, function(data, status, headers, config) {
	  	    		if (data) {
	  					for(var i=0; i<data.length; i++) {
	  						data[i].content = '<span>'+data[i].name+' <small><em>'+data[i].name+'</em></small></span>';
	  					}
	  	        		var Option = {
	  	        				title: "필름을 넣을 카메라를 선택해주세요",
	  	        			    okNull: false,
	  	        			    itemList: data
	  	        		};
	  	    			if (data.length==0 && !Option.okNull) return;
	  	        		
	  	        		showSelectModal(Option, function(user_camera) {
	  	        			if (user_camera.id==null) return;
	  	        			
	  	        			var insertFilmOnCamera = {
	  	        					token:TokenService.getToken($location.url()),
	  	        					uc_id:user_camera.id,
	  	        					f_id:item.id
	  	        			};
	  	        			
	  	        			AjaxService("POST", 'http://film.codefict.com/server/InsertUserFilm', insertFilmOnCamera, function(data, status, headers, config) {
	  	        				if (data=="success") {
	  	        					if (Option.itemList.length==1 && !Option.okNull) {
	  	        						allInsertedFilm = true;
	  	        						addButtonRemove();
	  	        					}
	  	        				}
	  	        			});
	  	      		    });
	  	        		
	  	    		}
	      		});
	      	}
	      	else {
	  	    	sendUrl = sendUrl + (itemType=='camera'?'AddUserCamera':'AddUserLens');

	  	    	var SendData = {
	  	    			token:TokenService.getToken($location.url()),
	  	        		id:item.id
	  	    		};
	  	    	AjaxService("POST", sendUrl, SendData, function(data, status, headers, config) {
	  	    		if (data=="success") {
	  	    			item.enabled = true;
	  	    		}
	      		});
	      	}
	      }
	      
	      
	      
	      $scope.toggleOpen = function(item) {
	      	var itemIDs = {
	      			type:itemType,
	      			id:item.id
	      	}
	      	if(!item.contents) {
	      		AjaxService("GET", 'http://film.codefict.com/server/ItemData', itemIDs, function(data, status, headers, config) {
	      			if (data) {
	      				item.contents = data;
	      				
	      				item.open = !item.open;
	      			}
	      		});
	      	}
	      	else item.open = !item.open;
	      }
	    }]);
