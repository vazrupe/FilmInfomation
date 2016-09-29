'use strict';

/**
 * @ngdoc directive
 * @name filmApp.directive:limitLength
 * @description
 * # limitLength
 */
angular.module('filmApp')
  .directive('limitLength', function () {
    return {
    	restrict: "A",
        link: function(scope, elem, attrs) {
            var limit = parseInt(attrs.limitLength);
            angular.element(elem).on("keydown", function(e) {
            	if (e.which == 8) return true;
                if (this.value.length == limit) return false;
            });
        }
    };
  });
