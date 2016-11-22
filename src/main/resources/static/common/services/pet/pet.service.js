angular.module('petStoreApp.services.pet-service', [])

.factory('PetService', function ($http) {

    var petService = {};

    petService.findAll = function () {
        return $http.get("/api/pets").then(function (response) {
            return response.data;
        });
    };

    petService.findOne = function (id) {
        return $http.get("/api/pets/" + pet.id).then(function (response) {
            return response.data;
        });
    };

    petService.create = function (pet) {
        return $http.post("/api/pets" , pet).then(function (response) {
            return response.data;
        });
    };

    petService.delete = function (pet) {
        return $http.delete("/api/pets/" + pet.id).then(function (response) {
            return response.data;
        });
    };

    return petService;
});