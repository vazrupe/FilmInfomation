'use strict';

describe('Controller: UseritemlistCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var UseritemlistCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    UseritemlistCtrl = $controller('UseritemlistCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(UseritemlistCtrl.awesomeThings.length).toBe(3);
  });
});
