MainModule.controller('FisaLunaraController', [ '$scope', '$http',
		function($scope, $http) {
			$scope.facturi = "";
			$scope.bonuri = "";
			$scope.luna = new Date();
	
			function getFacturiSiBonuri() {
				$http({
					method : "GET",
					url : "/TestPSBD/FisaLunara",
					params : {
						luna: $scope.luna
					}
				}).then(function success(response) {
					$scope.facturi = response.data.facturi;
					$scope.bonuri = response.data.bonuri;
					separaFacturi();
					console.log($scope.facturi);
				}, function error(response) {
					$scope.raspuns = "Eroare";
				});
			}

			getFacturiSiBonuri();
			
			function separaFacturi() {
				var facturi = $scope.facturi.map(function(obj) {
					return obj.factura_id;
				});
				
				facturi = facturi.filter(function(v,i) { return facturi.indexOf(v) == i; });

				facturi = facturi.map(function(obj) {
					return {
						factura_id: obj,
						cif: $scope.facturi.find(function(element) {
							return element.factura_id == obj;
						}).cif,
						total: $scope.facturi.find(function(element) {
							return element.factura_id == obj;
						}).total,
						tranzactii: []
					}
				})

				for (var i = 0; i < facturi.length; ++i) {
					facturi[i].tranzactii = $scope.facturi.filter(function(item) {
						return facturi[i].factura_id == item.factura_id;
					})
					
					facturi[i].tranzactii = facturi[i].tranzactii.map(function(element) {
						return {
							denumire_produs: element.denumire_produs,
							numar_bucati: element.numar_bucati,
							pret_buc: element.pret_produs,
							suma: parseInt(element.pret_produs) * parseInt(element.numar_bucati)
						}
					})
				}
				
				$scope.facturi = facturi;
			}
			
		} ]);