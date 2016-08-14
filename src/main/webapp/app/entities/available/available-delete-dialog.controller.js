(function() {
    'use strict';

    angular
        .module('siteManagerApp')
        .controller('AvailableDeleteController',AvailableDeleteController);

    AvailableDeleteController.$inject = ['$uibModalInstance', 'entity', 'Available'];

    function AvailableDeleteController($uibModalInstance, entity, Available) {
        var vm = this;

        vm.available = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Available.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
