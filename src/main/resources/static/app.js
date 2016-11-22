'use strict';

angular.module('petStoreApp', [
    'ngRoute',
    'ngAnimate',
    'ngSanitize',
    'ngAria',

    'ngMaterial',
    'truncate',

    'petStoreApp.services.auth-service',
    'petStoreApp.login',
    'petStoreApp.pets',
    'petStoreApp.pet'

]).config(['$locationProvider', '$routeProvider', function ($locationProvider, $routeProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.otherwise({redirectTo: '/pets'});
}])
    // Configure an interceptor to catch 401 and redirect to login view
    .config(['$httpProvider', function ($httpProvider) {
        $httpProvider.interceptors.push(function ($q, $location) {
            return {
                'responseError': function (rejection) {
                    if (rejection.status == 401) {
                        $location.path("/login");
                    }
                    return $q.reject(rejection);
                },
                'response': function (response) {
                    if (response.status == 401) {
                        $location.path("/login");
                    }
                    ;
                    return response || $q.when(response);
                }
            };
        });
    }]);
