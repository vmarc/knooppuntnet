function emitRouteReferences(networkType, routeReference, nodes) {
  for (var nodeIndex = 0; nodeIndex < nodes.length; nodeIndex++) {
    var node = nodes[nodeIndex];
    var key = [
      networkType,
      node.id,
      routeReference.routeName,
      routeReference.routeId
    ];
    emit(key, 1);
  }
}

if (doc && doc.route && doc.route.analysis && doc.route.active === true) {

  var analysis = doc.route.analysis;
  var summary = doc.route.summary;

  var routeReference = {
    routeId: summary.id,
    routeName: summary.name
  };

  emitRouteReferences(summary.networkType, routeReference, analysis.startNodes);
  emitRouteReferences(summary.networkType, routeReference, analysis.startTentacleNodes);
  emitRouteReferences(summary.networkType, routeReference, analysis.endNodes);
  emitRouteReferences(summary.networkType, routeReference, analysis.endTentacleNodes);
}
