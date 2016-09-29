'use strict';

describe('Controller: LoginformCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var LoginformCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    LoginformCtrl = $controller('LoginformCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(LoginformCtrl.awesomeThings.length).toBe(3);
  });
});
