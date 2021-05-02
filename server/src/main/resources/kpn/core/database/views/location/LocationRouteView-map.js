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

        var key = [
          summary.networkType,
          name,
          'all',
          summary.name,
          summary.id
        ];

        var value = {
          meters: summary.meters,
          lastUpdated: doc.route.lastUpdated,
          lastSurvey: doc.route.lastSurvey,
          broken: summary.isBroken,
          accessible: accessible
        };

        emit(key, value);

        if (summary.isBroken) {
          key = [
            summary.networkType,
            name,
            'facts',
            summary.name,
            summary.id
          ];
          emit(key, value);
        }

        if (!accessible) {
          key = [
            summary.networkType,
            name,
            'inaccessible',
            summary.name,
            summary.id
          ];
          emit(key, value);
        }

        if (doc.route.lastSurvey) {
          key = [
            summary.networkType,
            name,
            'survey',
            summary.lastSurvey,
            summary.name,
            summary.id
          ];
          emit(key, value);
        }
      }
    }
  }
}
