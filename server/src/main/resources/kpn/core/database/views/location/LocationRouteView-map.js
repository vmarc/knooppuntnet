if (doc && doc.route && doc.route.active === true) {
  var summary = doc.route.summary;
  var analysis = doc.route.analysis;
  if (analysis) {
    var locationAnalysis = analysis.locationAnalysis;
    if (locationAnalysis) {
      for (var nameIndex = 0; nameIndex < locationAnalysis.locationNames.length; nameIndex++) {
        var accessible = true;
        for (var routeFactIndex = 0; routeFactIndex < doc.route.facts.length; routeFactIndex++) {
          var routeFact = doc.route.facts[routeFactIndex];
          if ("RouteUnaccessible" === routeFact) {
            accessible = false;
          }
        }
        var name = locationAnalysis.locationNames[nameIndex];
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
            lastSurvey: doc.route.lastSurvey,
            broken: summary.isBroken,
            accessible: accessible
          }
        );
      }
    }
  }
}
