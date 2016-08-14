(function() {
    'use strict';
    angular
        .module('siteManagerApp')
        .factory('Available', Available);

    Available.$inject = ['$resource', 'DateUtils'];

    function Available ($resource, DateUtils) {
        var resourceUrl =  'api/availables/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.beginOfAvailability = DateUtils.convertLocalDateFromServer(data.beginOfAvailability);
                        data.endOfAvailability = DateUtils.convertLocalDateFromServer(data.endOfAvailability);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.beginOfAvailability = DateUtils.convertLocalDateToServer(data.beginOfAvailability);
                    data.endOfAvailability = DateUtils.convertLocalDateToServer(data.endOfAvailability);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.beginOfAvailability = DateUtils.convertLocalDateToServer(data.beginOfAvailability);
                    data.endOfAvailability = DateUtils.convertLocalDateToServer(data.endOfAvailability);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
