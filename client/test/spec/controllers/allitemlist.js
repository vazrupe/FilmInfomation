'use strict';

describe('Controller: AllitemlistCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var AllitemlistCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AllitemlistCtrl = $controller('AllitemlistCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AllitemlistCtrl.awesomeThings.length).toBe(3);
  });
});
