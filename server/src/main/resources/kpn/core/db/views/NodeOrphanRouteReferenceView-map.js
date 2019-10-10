function emitOrphanRouteReferences(routeId, orphanRouteReference, nodes) {
  for (var i = 0; i < nodes.length; i++) {
    var node = nodes[i];
    var key = [
      node.id,
      routeId
    ];
    emit(key, orphanRouteReference);
  }
}

if (doc && doc.route && doc.route.active === true) {

  var route = doc.route;

  if (route.orphan === true && route.analysis) {

    var summary = route.summary;
    var analysis = route.analysis;

    var orphanRouteReference = {
      networkType: summary.networkType,
      routeId: summary.id,
      routeName: summary.name
    };

    emitOrphanRouteReferences(summary.id, orphanRouteReference, analysis.startNodes);
    emitOrphanRouteReferences(summary.id, orphanRouteReference, analysis.startTentacleNodes);
    emitOrphanRouteReferences(summary.id, orphanRouteReference, analysis.endNodes);
    emitOrphanRouteReferences(summary.id, orphanRouteReference, analysis.endTentacleNodes);
  }
}
