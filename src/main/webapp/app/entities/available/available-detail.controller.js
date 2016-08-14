(function() {
    'use strict';

    angular
        .module('siteManagerApp')
        .controller('AvailableDetailController', AvailableDetailController);

    AvailableDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Available', 'User'];

    function AvailableDetailController($scope, $rootScope, $stateParams, previousState, entity, Available, User) {
        var vm = this;

        vm.available = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('siteManagerApp:availableUpdate', function(event, result) {
            vm.available = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
