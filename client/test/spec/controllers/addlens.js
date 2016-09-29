'use strict';

describe('Controller: AddlensCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var AddlensCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddlensCtrl = $controller('AddlensCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AddlensCtrl.awesomeThings.length).toBe(3);
  });
});
