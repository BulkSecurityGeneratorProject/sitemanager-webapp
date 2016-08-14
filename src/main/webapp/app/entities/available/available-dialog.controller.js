(function() {
    'use strict';

    angular
        .module('siteManagerApp')
        .controller('AvailableDialogController', AvailableDialogController);

    AvailableDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Available', 'User'];

    function AvailableDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Available, User) {
        var vm = this;

        vm.available = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.available.id !== null) {
                Available.update(vm.available, onSaveSuccess, onSaveError);
            } else {
                Available.save(vm.available, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('siteManagerApp:availableUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.beginOfAvailability = false;
        vm.datePickerOpenStatus.endOfAvailability = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
