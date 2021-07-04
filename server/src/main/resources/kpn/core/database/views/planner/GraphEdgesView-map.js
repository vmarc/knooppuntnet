var emitPath = function (networkType, routeId, path, oneWay, proposed) {

  if (path) {
    var key = [
      networkType,
      routeId,
      path.pathId
    ];
    var value = [
      path.startNodeId,
      path.endNodeId,
      path.meters,
      proposed
    ];

    emit(key, value);

    if (!oneWay) {
      var backwardKey = [
        networkType,
        routeId,
        100 + path.pathId
      ];
      var backwardValue = [
        path.endNodeId,
        path.startNodeId,
        path.meters,
        proposed
      ];

      emit(backwardKey, backwardValue);
    }
  }
};

var emitPaths = function (networkType, routeId, paths, proposed) {
  if (paths) {
    for (i = 0; i < paths.length; i++) {
      emitPath(networkType, routeId, paths[i], paths[i].oneWay === true, proposed);
    }
  }
};

if (doc && doc.route && doc.route.analysis && doc.route.active === true) {

  var networkType = doc.route.summary.networkType;
  var routeId = doc.route.summary.id;
  var routeMap = doc.route.analysis.map;

  var proposed = 0;
  if (doc.route.tags) {
    for (var i = 0; i < doc.route.tags.tags.length; i++) {
      var tag = doc.route.tags.tags[i];
      var key = tag.key;
      var value = tag.value;
      if (key === "state" && value === "proposed") {
        proposed = 1;
      }
    }
  }

  emitPaths(networkType, routeId, routeMap.freePaths, proposed);

  if (routeMap.forwardPath) {
    emitPath(networkType, routeId, routeMap.forwardPath, routeMap.forwardPath.oneWay, proposed);
    if (routeMap.forwardPath.oneWay === true) {
      if (routeMap.backwardPath) {
        emitPath(networkType, routeId, routeMap.backwardPath, routeMap.backwardPath.oneWay, proposed);
      }
    }
  } else {
    if (routeMap.backwardPath) {
      emitPath(networkType, routeId, routeMap.backwardPath, routeMap.backwardPath.oneWay, proposed);
    }
  }

  emitPaths(networkType, routeId, routeMap.startTentaclePaths, proposed);
  emitPaths(networkType, routeId, routeMap.endTentaclePaths, proposed);
}
