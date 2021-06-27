var emitRef = function (elementType, elementId, referrerType, referrerId, networkType, networkScope, referrerName) {
  var key = [
    elementType,
    elementId,
    referrerType,
    referrerId
  ];
  var value = [
    networkType,
    networkScope,
    referrerName
  ];
  emit(key, value);
};

if (doc && doc.route && doc.route.analysis && doc.route.active === true) {
  var s = doc.route.summary;
  var a = doc.route.analysis;
  for (var i = 0; i < a.map.startNodes.length; i++) {
    emitRef("node", a.map.startNodes[i].id, "route", s.id, s.networkType, s.networkScope, s.name);
  }
  for (var i = 0; i < a.map.endNodes.length; i++) {
    emitRef("node", a.map.endNodes[i].id, "route", s.id, s.networkType, s.networkScope, s.name);
  }
  for (var i = 0; i < a.map.startTentacleNodes.length; i++) {
    emitRef("node", a.map.startTentacleNodes[i].id, "route", s.id, s.networkType, s.networkScope, s.name);
  }
  for (var i = 0; i < a.map.endTentacleNodes.length; i++) {
    emitRef("node", a.map.endTentacleNodes[i].id, "route", s.id, s.networkType, s.networkScope, s.name);
  }
}
else if (doc && doc.network && doc.network.active === true) {
  var a = doc.network.attributes;
  if (doc.network.detail) {
    var d = doc.network.detail;
    for (var i = 0; i < d.nodes.length; i++) {
      var node = d.nodes[i];
      emitRef("node", node.id, "network", a.id, a.networkType, a.networkScope, a.name);
    }
    for (var i = 0; i < d.routes.length; i++) {
      emitRef("route", d.routes[i].id, "network", a.id, a.networkType, a.networkScope, a.name);
    }
  }
  else {
    for (var i = 0; i < doc.network.nodeRefs.length; i++) {
      var nodeRef = doc.network.nodeRefs[i];
      emitRef("node", nodeRef, "network", a.id, a.networkType, a.networkScope, a.name);
    }
    for (var i = 0; i < doc.network.routeRefs.length; i++) {
      var routeRef = doc.network.routeRefs[i];
      emitRef("route", routeRef, "network", a.id, a.networkType, a.networkScope, a.name);
    }
  }
}
