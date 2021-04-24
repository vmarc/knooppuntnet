if (doc && doc.route && doc.route.analysis && doc.route.active === true) {

  var analysis = doc.route.analysis;
  var summary = doc.route.summary;

  var routeReference = {
    routeId: summary.id,
    routeName: summary.name
  };

  var allNodeIds = [];
  var allNodes = [];

  for (var i = 0; i < analysis.map.freeNodes.length; i++) {
    var node = analysis.map.freeNodes[i];
    if (allNodeIds.indexOf(node.id) < 0) {
      allNodeIds.push(node.id);
      allNodes.push(node);
    }
  }

  for (i = 0; i < analysis.map.startNodes.length; i++) {
    node = analysis.map.startNodes[i];
    if (allNodeIds.indexOf(node.id) < 0) {
      allNodeIds.push(node.id);
      allNodes.push(node);
    }
  }

  for (i = 0; i < analysis.map.startTentacleNodes.length; i++) {
    node = analysis.map.startTentacleNodes[i];
    if (allNodeIds.indexOf(node.id) < 0) {
      allNodeIds.push(node.id);
      allNodes.push(node);
    }
  }

  for (i = 0; i < analysis.map.endNodes.length; i++) {
    node = analysis.map.endNodes[i];
    if (allNodeIds.indexOf(node.id) < 0) {
      allNodeIds.push(node.id);
      allNodes.push(node);
    }
  }

  for (i = 0; i < analysis.map.endTentacleNodes.length; i++) {
    node = analysis.map.endTentacleNodes[i];
    if (allNodeIds.indexOf(node.id) < 0) {
      allNodeIds.push(node.id);
      allNodes.push(node);
    }
  }

  for (i = 0; i < allNodes.length; i++) {
    node = allNodes[i];
    var key = [
      summary.networkType,
      summary.networkScope,
      node.id,
      routeReference.routeName,
      routeReference.routeId
    ];
    emit(key, 1);
  }
}
