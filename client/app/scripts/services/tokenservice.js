'use strict';

/**
 * @ngdoc service
 * @name filmApp.TokenService
 * @description # TokenService Service in the filmApp.
 */
angular.module('filmApp').service(
		'TokenService', [
		'$location',
		function($location) {
			this.Token = null;
			this.lastid = '';
			this.toPath = '/';
			
			this.getToken = function(toPath) {
				if (this.Token==null) {
					this.toPath = toPath;
					$location.path('/login');
				}
				return this.Token;
			};
			
			this.resetToken = function(toPath) {
				this.Token = null;
				return this.getToken(toPath);
			}
		}]);
