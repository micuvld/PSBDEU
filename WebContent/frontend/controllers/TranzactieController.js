MainModule.controller('TranzactieController', [ '$scope', '$http', function($scope, $http) {
		getProduse();
		function Tranzactie() {
			this.produs;
			this.numarBucati;
		}
		
		$scope.tranzactii = [new Tranzactie()];
		$scope.produse = [];
		
		$scope.dataFormular = "";
		$scope.cifFormular = "";
		$scope.tipFormular = "";
		
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
		

		$scope.addTranzactie = function () {
			$scope.tranzactii.push(new Tranzactie());
		}
		
		$scope.trimiteFormular = function() {
			//$scope.dataFormular.setDate($scope.dataFormular.getDate() + 1);
			console.log($scope.dataFormular);
			
			var jsonToSend = {
				data: $scope.dataFormular,
				cif: $scope.cifFormular,
				numarBucati: $scope.tranzactii.map(function(currentElement, index, arr) {
					return currentElement.numarBucati;
				}),
				idProduse: $scope.tranzactii.map(function(currentElement, index, arr) {
					return parseInt(currentElement.produs.id);
				}),
				tip: $scope.tipFormular
			}
			console.log(jsonToSend);
			
			$http({
				method: "POST",
				url: "/TestPSBD/Tranzactie",
				data: jsonToSend
			})
		}


		} ]);