var emitRef = function (elementType, elementId, referrerType, referrerId, networkType, referrerName, connection) {
  var key = [
    elementType,
    elementId,
    referrerType,
    referrerId
  ];
  var value = [
    networkType,
    referrerName,
    connection
  ];
  emit(key, value);
};

if (doc && doc.route && doc.route.analysis && doc.route.active === true) {
  var s = doc.route.summary;
  var a = doc.route.analysis;
  for (var i = 0; i < a.startNodes.length; i++) {
    emitRef("node", a.startNodes[i].id, "route", s.id, s.networkType, s.name, "false");
  }
  for (var i = 0; i < a.endNodes.length; i++) {
    emitRef("node", a.endNodes[i].id, "route", s.id, s.networkType, s.name, "false");
  }
  for (var i = 0; i < a.startTentacleNodes.length; i++) {
    emitRef("node", a.startTentacleNodes[i].id, "route", s.id, s.networkType, s.name, "false");
  }
  for (var i = 0; i < a.endTentacleNodes.length; i++) {
    emitRef("node", a.endTentacleNodes[i].id, "route", s.id, s.networkType, s.name, "false");
  }
}
else if (doc && doc.network && doc.network.active === true) {
  var a = doc.network.attributes;
  if (doc.network.detail) {
    var d = doc.network.detail;
    for (var i = 0; i < d.nodes.length; i++) {
      var node = d.nodes[i];
      var connection = "false";
      if (node.roleConnection) {
        connection = "true";
      }
      emitRef("node", node.id, "network", a.id, a.networkType, a.name, connection);
    }
    for (var i = 0; i < d.routes.length; i++) {
      emitRef("route", d.routes[i].id, "network", a.id, a.networkType, a.name, "false");
    }
  }
  else {
    for (var i = 0; i < doc.network.nodeRefs.length; i++) {
      var nodeRef = doc.network.nodeRefs[i];
      emitRef("node", nodeRef, "network", a.id, a.networkType, a.name, "false");
    }
    for (var i = 0; i < doc.network.routeRefs.length; i++) {
      var routeRef = doc.network.routeRefs[i];
      emitRef("route", routeRef, "network", a.id, a.networkType, a.name, "false");
    }
  }
}
