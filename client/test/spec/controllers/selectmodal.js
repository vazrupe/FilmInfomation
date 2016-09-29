'use strict';

describe('Controller: SelectmodalCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var SelectmodalCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    SelectmodalCtrl = $controller('SelectmodalCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(SelectmodalCtrl.awesomeThings.length).toBe(3);
  });
});
