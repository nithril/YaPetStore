'use strict';

angular.module('petStoreApp.pets', ['petStoreApp.services.pet-service', 'petStoreApp.services.toast-service', 'petStoreApp.services.cart-service'])

    .config(['$routeProvider', function ($routeProvider) {
        $routeProvider.when('/pets', {
            templateUrl: 'pets/pets.template.html',
            controller: 'PetsCtrl',
            controllerAs: 'vm'
        });
    }])

    .controller('PetsCtrl', ['PetService', 'ToastService', 'CartService',  '$mdDialog', 'AuthService', '$location',
        function (petService, toastService, cartService, $mdDialog, authService, $location) {
        var petsCtrl = this;

        petsCtrl.pets = [];

        petsCtrl.findAll = function () {
            petService.findAll().then(function (data) {
                petsCtrl.pets = data;
            });
        };

        petsCtrl.delete = function ($event, pet) {
            var confirm = $mdDialog.confirm()
                .title('Would you like to delete ' + pet.name + '?')
                .ariaLabel('Confirm pet deletion')
                .targetEvent($event)
                .ok('Yes!')
                .cancel('No');

            // Confirm the deletion
            $mdDialog.show(confirm).then(function () {
                petService.delete(pet).then(function () {
                    // Refresh the list
                    petsCtrl.findAll();
                    toastService.toast(pet.name + ' has been deleted');
                }, function (response) {
                    if (response.status == 403) {
                        toastService.toast('Forbidden');
                    } else {
                        toastService.toast('An error occured...');
                    }
                });
            });
        };

        petsCtrl.addToCart = function(pet) {
            cartService.add(pet);
            toastService.toast(pet.name + ' has been added to your cart');
        };


        petsCtrl.create = function ($event) {
            $mdDialog.show({
                controller: 'PetCtrl',
                controllerAs: 'vm',
                templateUrl: '/pets/pet-create.template.html',
                parent: angular.element(document.body),
                targetEvent: $event,
                clickOutsideToClose: true

            }).then(function () {
                petsCtrl.findAll();
            });
        };

        // TODO: move this function into a dedicated component
        petsCtrl.logout = function () {
            authService.logout().then(function (response) {
                if (response.status == 200) {
                    $location.path("/login");
                }
            }, function (response) {
                toastService.toast(response.data.message);
            });
        }


        // Find all pets
        petsCtrl.findAll();


    }]);