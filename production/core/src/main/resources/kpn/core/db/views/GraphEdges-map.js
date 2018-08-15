if (doc && doc.route) {
  var key = {
    networkType: doc.route.summary.networkType,
    routeId: doc.route.summary.id
  };
  var value = {
    startNodeId: doc.route.analysis.startNodes[0].id,
    endNodeId: doc.route.analysis.endNodes[0].id,
    meters: doc.route.summary.meters
  };
  emit(key, value);
}
