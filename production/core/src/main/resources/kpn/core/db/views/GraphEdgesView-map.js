var emitPath = function (networkType, routeId, pathType, pathIndex, path) {
  if (path) {
    var key = [
      networkType,
      routeId,
      pathType,
      pathIndex
    ];
    var value = [
      path.startNodeId,
      path.endNodeId,
      path.meters
    ];
    emit(key, value);
  }
};

var emitPaths = function (networkType, routeId, pathType, paths) {
  if (paths) {
    for (i = 0; i < paths.length; i++) {
      emitPath(networkType, routeId, pathType, i + 1, paths[i]);
    }
  }
};

if (doc && doc.route && doc.route.analysis && doc.route.active === true && doc.route.ignored === false) {

  var networkType = doc.route.summary.networkType;
  var routeId = doc.route.summary.id;
  var routeMap = doc.route.analysis.map;

  emitPath(networkType, routeId, "forward", 1, routeMap.forwardPath);
  emitPath(networkType, routeId, "backward", 1, routeMap.backwardPath);
  emitPaths(networkType, routeId, "start", routeMap.startTentaclePaths);
  emitPaths(networkType, routeId, "end", routeMap.endTentaclePaths);
}
