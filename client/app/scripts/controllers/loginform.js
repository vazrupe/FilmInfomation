'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:LoginformCtrl
 * @description # LoginformCtrl Controller of the filmApp
 */
angular.module('filmApp').controller(
		'LoginformCtrl',
		[ '$scope', '$element', '$http', 'lastid', 'close',
				function($scope, $element, $http, lastid, close) {
					$scope.id = lastid;
					$scope.passwd = null;

					$scope.close = function() {
						var LoginData = {
							id:$scope.id,
							pwd:$scope.passwd
						};
						//Ajax 처리
						$http({
							method: 'POST',
							url: 'http://film.codefict.com/server/UserToken',
							data: $.param(LoginData),
							headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
						}).success(function(data, status, headers, config) {
							if (data) {
								if (data=="Not Matched Password") {
									$scope.error_msg="패스워드를 확인해주세요";
									$("#loginerr").removeClass('hidden');
									return false;
								}
								else {
									var returnData = {
											id : $scope.id,
											token : data
										};
									$element.modal('hide');
									close(returnData, 500);
								}
							}
						}).error(function(data, status, headers, config){
							console.log(status);
						});
					};
					
					$scope.closeFunc = function() {
						
					}
				} ]);
