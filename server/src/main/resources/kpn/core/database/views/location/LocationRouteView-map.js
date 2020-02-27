if (doc && doc.route && doc.route.active === true) {
  var summary = doc.route.summary;
  var analysis = doc.route.analysis;
  if (analysis) {
    var locationAnalysis = analysis.locationAnalysis;
    if (locationAnalysis) {
      for (var nameIndex = 0; nameIndex < locationAnalysis.locationNames.length; nameIndex++) {
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
            broken: summary.isBroken
          }
        );
      }
    }
  }
}
