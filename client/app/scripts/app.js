'use strict';

/**
 * @ngdoc overview
 * @name filmApp
 * @description
 * # filmApp
 *
 * Main module of the application.
 */
angular
  .module('filmApp', [
    'angularModalService',
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'rzModule',
    'angucomplete-alt',
    'ui.bootstrap.accordion'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl',
        controllerAs: 'main'
      })
      .when('/login', {
        templateUrl: 'views/login.html',
        controller: 'LoginCtrl',
        controllerAs: 'login'
      })
      .when('/user/:itemType', {
        templateUrl: 'views/allItemList.html',
        controller: 'UseritemlistCtrl',
        controllerAs: 'allitemlist',
        resolve: {
          itemType: function($route) {
          	return $route.current.params.itemType.toLowerCase();
          }
        }
      })
      
      .when('/add/picture/:UserFilmId', {
        templateUrl: 'views/addPicture.html',
        controller: 'AddpictureCtrl',
        controllerAs: 'allpicture',
        resolve: {
        	UserFilmId: function($route) {
        		return $route.current.params.UserFilmId;
        	}
        }
      })
      
      .when('/all/:itemType', {
        templateUrl: 'views/allItemList.html',
        controller: 'AllitemlistCtrl',
        controllerAs: 'allitemlist',
        resolve: {
        	itemType: function($route) {
        		return $route.current.params.itemType.toLowerCase();
        	}
        }
      })
      
      .when('/all/film/add', {
        templateUrl: 'views/addFilm.html',
        controller: 'AddFilmCtrl',
        controllerAs: 'addfilm'
      })
      .when('/all/camera/add', {
        templateUrl: 'views/addCamera.html',
        controller: 'AddCameraCtrl',
        controllerAs: 'addcamera'
      })
      .when('/all/lens/add', {
        templateUrl: 'views/addLens.html',
        controller: 'AddLensCtrl',
        controllerAs: 'addlens'
      })
      .otherwise({
        redirectTo: '/'
      });
  });
