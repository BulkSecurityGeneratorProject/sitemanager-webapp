(function() {
    'use strict';

    angular
        .module('siteManagerApp')
        .controller('AvailableController', AvailableController);

    AvailableController.$inject = ['$scope', '$state', 'Available'];

    function AvailableController ($scope, $state, Available) {
        var vm = this;
        
        vm.availables = [];

        loadAll();

        function loadAll() {
            Available.query(function(result) {
                vm.availables = result;
            });
        }
    }
})();
