function emitRouteReferences(routeId, routeReference, nodes) {
  for (var i = 0; i < nodes.length; i++) {
    var node = nodes[i];
    var key = [
      node.id,
      routeId
    ];
    emit(key, routeReference);
  }
}

if (doc && doc.route && doc.route.active === true) {

  var route = doc.route;

  if (route.analysis) {

    var summary = route.summary;
    var analysis = route.analysis;

    var reference = {
      networkType: summary.networkType,
      networkScope: summary.networkScope,
      id: summary.id,
      name: summary.name
    };

    emitRouteReferences(summary.id, reference, analysis.map.startNodes);
    emitRouteReferences(summary.id, reference, analysis.map.startTentacleNodes);
    emitRouteReferences(summary.id, reference, analysis.map.endNodes);
    emitRouteReferences(summary.id, reference, analysis.map.endTentacleNodes);
  }
}
