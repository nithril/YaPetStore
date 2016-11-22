angular.module('petStoreApp.services.auth-service', [])

    .factory('AuthService', function ($http) {

        var authService = {};

        authService.login = function (username, password) {
            return $http.post("/api/auth/login", {username: username, password: password});
        };

        authService.logout = function (id) {
            return $http.get("/api/auth/logout");
        };

        return authService;
    });