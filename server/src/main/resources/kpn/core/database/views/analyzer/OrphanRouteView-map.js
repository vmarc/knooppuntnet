if (doc && doc.route && doc.route.active === true && doc.route.orphan === true) {
  var accessible = true;
  for (var routeFactIndex = 0; routeFactIndex < doc.route.facts.length; routeFactIndex++) {
    var routeFact = doc.route.facts[routeFactIndex];
    if ("RouteUnaccessible" === routeFact) {
      accessible = false;
    }
  }
  var summary = doc.route.summary;
  var key = [
    summary.country,
    summary.networkType,
    summary.name,
    summary.id
  ];
  var value = {
    meters: summary.meters,
    isBroken: summary.isBroken,
    accessible: accessible,
    lastSurvey: doc.route.lastSurvey,
    lastUpdated: doc.route.lastUpdated
  };
  emit(key, value);
}
