MainModule.controller('FisaLunaraController', [ '$scope', '$http',
		function($scope, $http) {
			$scope.facturiSiBonuri = "";
			$scope.luna = new Date();
	
			function getFacturiSiBonuri() {
				$http({
					method : "GET",
					url : "/TestPSBD/FisaLunara",
					params : {
						luna: $scope.luna
					}
				}).then(function success(response) {
					console.log(response.data);
					$scope.facturiSiBonuri = response.data;
				}, function error(response) {
					$scope.raspuns = "Eroare";
				});
			}

			getFacturiSiBonuri();
		} ]);