'use strict';

describe('Controller: AddcameraCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var AddcameraCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddcameraCtrl = $controller('AddcameraCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AddcameraCtrl.awesomeThings.length).toBe(3);
  });
});
