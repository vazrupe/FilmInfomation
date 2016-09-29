'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:MainCtrl
 * @description # MainCtrl Controller of the filmApp
 */
angular.module('filmApp').controller(
		'MainCtrl',
		[
				'$scope', '$location', '$http',
				'TokenService',
				function($scope, $location, $http, TokenService) {
					$scope.test_var = TokenService.getToken('/');
					$scope.filmList = [];
					
					$scope.noneInsertedFilm = false;
					
					$scope.init = function() {
						var token =  {
							token:TokenService.getToken('/')
						};
						if(token.token) {
							$http({
								method: 'GET',
								url: 'http://film.codefict.com/server/InsertedFilmList',
								params: token,
								headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=utf-8'}
							}).success(function(data, status, headers, config) {
								if (data) {
									$scope.filmList = data;
									$scope.noneInsertedFilm = (data.length>0);
								}
							}).error(function(data, status, headers, config){
								console.log(status);
							});
						}
					}
				} ]);
