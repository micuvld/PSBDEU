MainModule.controller('FisaLunaraController', [ '$scope', '$http',
		function($scope, $http) {
			$scope.facturi = "";
			$scope.bonuri = "";
			$scope.luna = new Date();
			$scope.facturaCurenta = "";
			$scope.facturaCurentaId = -1;
			$scope.facturaCurentaCif = -1;
	
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
					separaBonuri();
					console.log($scope.facturi);
					console.log($scope.bonuri);
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
			
			function separaBonuri(){
				var bonuri = $scope.bonuri.map(function(obj){
					return obj.bon_id;
				});

				bonuri = bonuri.filter(function(v,i) { return bonuri.indexOf(v) == i; });
				
				bonuri = bonuri.map(function(obj){
					return{
						bon_id: obj,
						cif: $scope.bonuri.find(function(element) {
							return element.bon_id == obj;
						}).cif,
						total: $scope.bonuri.find(function(element){
							return element.bon_id == obj;
						}).total,
						tranzactii: []
				}
					})
				console.log(bonuri);	
				for(var i = 0; i<bonuri.length; ++i){
					bonuri[i].tranzactii = $scope.bonuri.filter(function(item){
						return bonuri[i].bon_id == item.bon_id;
					})
					bonuri[i].tranzactii = bonuri[i].tranzactii.map(function(element){
						return{
							denumire_produs: element.denumire_produs,
							numar_bucati: element.numar_bucati,
							pret_buc: element.pret_produs,
							suma: parseInt(element.pret_produs) * parseInt(element.numar_bucati)
						}
					})
				}
				$scope.bonuri = bonuri;
				console.log("Bonuri:", $scope.bonuri);
				console.log("Facturi:", $scope.facturi);
				
			}
			
		
			$scope.updateFiseLunare = function() {
				var jsonToSend = {
						data: $scope.luna,
						factura_id: $scope.facturaCurentaId,
						cif: $scope.facturaCurentaCif,
						numarBucati: $scope.tranzactii.map(function(currentElement, index, arr) {
							return currentElement.numarBucati;
						}),
						idProduse: $scope.tranzactii.map(function(currentElement, index, arr) {
							return parseInt(currentElement.produs.id);
						}),
						tip: $scope.tipFormular
					}
					console.log(jsonToSend);
					
//					$http({
//						method: "POST",
//						url: "/TestPSBD/Tranzactie",
//						data: jsonToSend
//					})
			}
			
			$scope.setFacturaCurenta = function(factura) {
				console.log('a');
				$scope.factura = factura;
				console.log($scope.factura);
			}
			
		} ]);