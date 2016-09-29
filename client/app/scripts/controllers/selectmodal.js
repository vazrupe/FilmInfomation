'use strict';

/**
 * @ngdoc function
 * @name filmApp.controller:SelectmodalCtrl
 * @description
 * # SelectmodalCtrl
 * Controller of the filmApp
 */
angular.module('filmApp')
  .controller('SelectmodalCtrl', ['$scope', '$element', 'options', 'close', function($scope, $element, options, close) {
	// options
	/*
	 * {
	 *   title: headerText,
	 *   itemList: [{id:NUMBER, content:CONTENT_TAGS}],
	 *   okNull: true/false
	 * }
	 */
	$scope.title=options.title;
	$scope.itemList = options.itemList;
	$scope.id=null;
	
	if(options.okNull) {
		$scope.itemList.unshift({id:"NULL", content:'선택 안함'});
	}
	  
	$scope.select = function(item) {
	  close({
	    id: item.id
	  }, 100);
	};
	  
	$scope.close = function() {
	  close({
	    id: null
	  }, 100);
	};
  }]);
