MainModule.controller('ProdusController', [ '$scope', '$http',
		function($scope, $http) {
			$scope.denumire = "";
			$scope.pret = "";
			$scope.raspuns = "*RASPUNS*";
			$scope.produse = [];

			$scope.inserare = function() {
				$http({
					method : "POST",
					url : "/TestPSBD/Produs",
					params : {
						"denumireProdus" : $scope.denumire,
						"pretProdus" : $scope.pret
					}
				}).then(function success(response) {
					$scope.raspuns = "Produs inserat cu succes!";
				}, function error(response) {
					$scope.raspuns = "Eroare";
				});
			}

			function getProduse() {
				$http({
					method: "GET",
					url: "/TestPSBD/Produs"
				}).then(function success(response) {
					console.log(response.data);
					$scope.produse = response.data;
				}, function error(response) {
					$scope.raspuns = "Eroare";
				});
			}
			
			$scope.actualizare = function(id){
				$http({
					method: "PUT",
					url: "/TestPSBD/Produs",
					params: {
						"idProdus": id,
						"denumireProdus": document.getElementById("denumire" + id).value,
						"pretProdus": document.getElementById("pret" + id).value
					}
				}).then(function success(response) {
					$scope.raspuns = "Produs actualizat cu succes!";
					getProduse();
				}, function error(response) {
					$scope.raspuns = "Eroare";
				})
				
			}
			
			$scope.stergere = function(id) {
				$http({
					method: "DELETE",
					url: "/TestPSBD/Produs",
					params: {
						"idProdus": id,
					}
				}).then(function success(response) {
					$scope.raspuns = "Produs sters cu succes!";
				}, function error(response) {
					$scope.raspuns = "Eroare";
				})
			}
			getProduse();
		} ]);