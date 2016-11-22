'use strict';

angular.module('petStoreApp.pet', ['petStoreApp.services.pet-service', 'petStoreApp.services.toast-service'])

    .controller('PetCtrl', ['PetService', 'ToastService', '$mdDialog', function (petService, toastService, $mdDialog) {
        var petCtrl = this;

        petCtrl.pet = {};

        petCtrl.save = function () {
            petService.create(petCtrl.pet).then(function () {
                toastService.toast(petCtrl.pet.name + ' is created!');
                $mdDialog.hide();
            }, function (response) {
                toastService.toast(response.data.message);
            });
        };

        petCtrl.cancel = function () {
            $mdDialog.cancel();
        };
    }]);