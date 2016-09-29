'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:LoginCtrl
 * @description # LoginCtrl Controller of the filmApp
 */
angular
		.module('filmApp')
		.controller(
				'LoginCtrl', ['$scope', '$http', '$location', 'TokenService',
				function($scope, $http, $location, TokenService) {
					$scope.init = function() {
						if(TokenService.Token!=null) $location.path('/');
						
						$scope.lastid = TokenService.lastid;
					}
					
					$scope.submit = function() {
						var temp_id = String($scope.id);
						var temp_pwd = String($scope.passwd);
						var validInputForm = (temp_id.replace(/(^\s*)|(\s*$)/gi, "").length > 0)&&(temp_pwd.length > 0);
						if(!validInputForm) {
							$scope.error_msg = "아이디나 비밀번호가 비어있습니다";
							$("#loginerr").removeClass('hidden');
							return;
						}
						
						var LoginData = {
								id:$scope.id,
								pwd:$scope.passwd
							};
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
										TokenService.lastid = $scope.id;
										TokenService.Token = data;
										$location.path(TokenService.toPath);
									}
								}
							}).error(function(data, status, headers, config){
								//console.log(status);
							});
						};
				}]);
