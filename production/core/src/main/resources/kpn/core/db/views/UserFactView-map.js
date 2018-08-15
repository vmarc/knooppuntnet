var emitOrphanNodeFact = function (node, networkType, name) {
  var key = [
    node.lastUpdatedBy,
    node.country,
    networkType,
    "node",
    "OrphanNode"
  ];
  var value = [
    name,
    node.id
  ];
  emit(key, value);
};

if (doc && doc.network && doc.network.ignored === false && doc.network.detail) {

  var a = doc.network.attributes;

  for (fact in doc.network.detail.networkFacts) {
    if (fact === "integrityCheckFailed") {
      var checks = doc.network.detail.networkFacts.integrityCheckFailed.checks;
      for (var i = 0; i < checks.length; i++) {
        var check = checks[i];
        var key = [
          a.relationLastUpdatedBy,
          a.country,
          a.networkType,
          "node",
          fact.charAt(0).toUpperCase() + fact.slice(1),
          a.name,
          a.id
        ];
        var value = [
          check.nodeName,
          check.nodeId
        ];
        emit(key, value);
      }
    }
    else {
      var key = [
        a.relationLastUpdatedBy,
        a.country,
        a.networkType,
        "network",
        fact.charAt(0).toUpperCase() + fact.slice(1),
        a.name,
        a.id
      ];
      var value = [];
      emit(key, value);
    }
  }

  for (fact in doc.network.facts) {
    if (fact === "integrityCheckFailed") {
      var checks = doc.network.detail.facts[fact].checks;
      for (var i = 0; i < checks.length; i++) {
        var check = checks[i];
        var key = [
          a.relationLastUpdatedBy,
          a.country,
          a.networkType,
          "node",
          fact.charAt(0).toUpperCase() + fact.slice(1),
          a.name,
          a.id
        ];
        var value = [
          check.nodeName,
          check.nodeId
        ];
        emit(key, value);
      }
    }
    else {
      var key = [
        a.relationLastUpdatedBy,
        a.country,
        a.networkType,
        "network",
        fact.charAt(0).toUpperCase() + fact.slice(1),
        a.name,
        a.id
      ];
      var value = [];
      emit(key, value);
    }
  }
  for (var i = 0; i < doc.network.detail.routes.length; i++) {
    var route = doc.network.detail.routes[i];
    for (var j = 0; j < route.facts.length; j++) {
      var key = [
        route.relationLastUpdatedBy,
        a.country,
        a.networkType,
        "route",
        route.facts[j],
        a.name,
        a.id
      ];
      var value = [
        route.name,
        route.id
      ];
      emit(key, value);
    }
  }
}
else if (doc.node && doc.node.ignored === false && doc.node.orphan === true) {
  if (doc.node.rcnName.length > 0) {
    emitOrphanNodeFact(doc.node, "rcn", doc.node.rcnName)
  }
  if (doc.node.rwnName.length > 0) {
    emitOrphanNodeFact(doc.node, "rwn", doc.node.rwnName)
  }
}
else if (doc.route && doc.route.ignored === false && doc.route.orphan === true) {
  var key = [
    doc.route.summary.user,
    doc.route.summary.country,
    doc.route.summary.networkType,
    "route",
    "OrphanRoute"
  ];
  var value = [
    doc.route.summary.name,
    doc.route.summary.id
  ];
  emit(key, value);
}
