'use strict';

angular.module('petStoreApp.login', [])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/login', {
            templateUrl: 'login/login.template.html',
            controller: 'LoginCtrl',
            controllerAs: 'vm'
        });
    }])

    .controller('LoginCtrl', ['AuthService', '$location', function (authService, $location) {
        var loginCtrl = this;


        loginCtrl.message = "";

        loginCtrl.login = function (username, password) {
            authService.login(username , password).then(function(response){
                if (response.status==200){
                    $location.path("/pets");
                }
            }, function(response){
                loginCtrl.message = response.data.message;
            });
        }


    }]);