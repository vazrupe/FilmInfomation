'use strict';

describe('Controller: AddfilmCtrl', function () {

  // load the controller's module
  beforeEach(module('filmApp'));

  var AddfilmCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    AddfilmCtrl = $controller('AddfilmCtrl', {
      $scope: scope
      // place here mocked dependencies
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(AddfilmCtrl.awesomeThings.length).toBe(3);
  });
});
