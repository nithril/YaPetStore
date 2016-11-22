angular.module('petStoreApp.services.cart-service', [])

    .factory('CartService', function ($http) {

        var cartService = {
            pets: []
        };

        cartService.add = function (pet) {
            this.pets.push(angular.copy(pet));
        };


        cartService.remove = function (petToRemove) {
            this.pets = _.filter(this.pets, function(pet) { return pet.id != petToRemove.id; });
        };


        return cartService;
    });