angular.module('petStoreApp.services.toast-service', [])

.factory('ToastService', ['$http', '$mdToast', function ($http, $mdToast) {

    var toastService = {};

    toastService.toast = function (content) {
        var toast = $mdToast.simple()
            .position("bottom right")
            .textContent(content)
            .hideDelay(3000);
        $mdToast.show(toast);
    };

    return toastService;
}]);