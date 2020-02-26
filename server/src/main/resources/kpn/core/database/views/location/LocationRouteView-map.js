if (doc && doc.route && doc.route.active === true) {
  var routeAnalysis = doc.route.analysis;
  var summary = doc.route.summary;
  if (routeAnalysis) {
    var locationNames = [];
    var locationAnalysis = routeAnalysis.locationAnalysis;
    if (locationAnalysis) {
      for (var candidateIndex = 0; candidateIndex < locationAnalysis.candidates.length; candidateIndex++) {
        var candidate = locationAnalysis.candidates[candidateIndex];
        for (var locationNameIndex = 0; locationNameIndex < candidate.location.names.length; locationNameIndex++) {
          var locationName = candidate.location.names[locationNameIndex];
          if (locationNames.indexOf(locationName) === -1) {
            locationNames.push(candidate.location.names[locationNameIndex]);
          }
        }
      }
      for (var nameIndex = 0; nameIndex < locationNames.length; nameIndex++) {
        var name = locationNames[nameIndex];
        emit(
          [
            summary.networkType,
            name,
            summary.name,
            summary.id
          ],
          {
            meters: summary.meters,
            lastUpdated: doc.route.lastUpdated,
            broken: summary.isBroken
          }
        );
      }
    }
  }
}
