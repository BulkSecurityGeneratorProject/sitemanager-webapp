(function() {
    'use strict';

    angular
        .module('siteManagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('available', {
            parent: 'entity',
            url: '/available',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'siteManagerApp.available.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/available/availables.html',
                    controller: 'AvailableController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('available');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('available-detail', {
            parent: 'entity',
            url: '/available/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'siteManagerApp.available.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/available/available-detail.html',
                    controller: 'AvailableDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('available');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Available', function($stateParams, Available) {
                    return Available.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'available',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('available-detail.edit', {
            parent: 'available-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available/available-dialog.html',
                    controller: 'AvailableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Available', function(Available) {
                            return Available.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('available.new', {
            parent: 'available',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available/available-dialog.html',
                    controller: 'AvailableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                beginOfAvailability: null,
                                endOfAvailability: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('available', null, { reload: true });
                }, function() {
                    $state.go('available');
                });
            }]
        })
        .state('available.edit', {
            parent: 'available',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available/available-dialog.html',
                    controller: 'AvailableDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Available', function(Available) {
                            return Available.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('available', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('available.delete', {
            parent: 'available',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/available/available-delete-dialog.html',
                    controller: 'AvailableDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Available', function(Available) {
                            return Available.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('available', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
