'use strict';

describe('Controller: AddpictureCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var AddpictureCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddpictureCtrl = $controller('AddpictureCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AddpictureCtrl.awesomeThings.length).toBe(3);
  });
});
