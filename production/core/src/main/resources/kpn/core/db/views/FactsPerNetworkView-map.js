if (doc && doc.network) {
  var network = doc.network;
  var a = doc.network.attributes;
  var detail = doc.network.detail;
  var facts = doc.network.facts;
  for (var i = 0; i < facts.length; i++) {
    var fact = facts[i];
    if (fact !== "IntegrityCheckFailed") {
      var key = [
        a.country,
        a.networkType,
        fact,
        a.name,
        a.id
      ];
      emit(key, 1);
    }
  }

  if (detail) {
    var routes = detail.routes;
    for (var i = 0; i < routes.length; i++) {
      var route = routes[i];
      for (var j = 0; j < route.facts.length; j++) {
        var fact = route.facts[j];
        var key = [
          a.country,
          a.networkType,
          fact,
          a.name,
          a.id,
          route.name,
          route.id
        ];
        emit(key, 1);
      }
    }

    var nodes = detail.nodes;
    for (var i = 0; i < nodes.length; i++) {
      var node = nodes[i];
      for (var j = 0; j < node.facts.length; j++) {
        var fact = node.facts[j];
        var key = [
          a.country,
          a.networkType,
          fact,
          a.name,
          a.id,
          node.name,
          node.id
        ];
        emit(key, 1);
      }
    }

    var integrityCheckFailed = detail.networkFacts.integrityCheckFailed;
    if (integrityCheckFailed) {
      for (var i = 0; i < integrityCheckFailed.checks.length; i++) {
        var check = integrityCheckFailed.checks[i];
        if (check.failed) {
          var key = [
            a.country,
            a.networkType,
            "IntegrityCheckFailed",
            a.name,
            a.id,
            check.nodeName,
            check.nodeId
          ];
          emit(key, 1);
        }
      }
    }

    var networkExtraMemberNode = detail.networkFacts.networkExtraMemberNode;
    if (networkExtraMemberNode) {
      for (var i = 0; i < networkExtraMemberNode.length; i++) {
        var fact = networkExtraMemberNode[i];
        var key = [
          a.country,
          a.networkType,
          "NetworkExtraMemberNode",
          a.name,
          a.id,
          "" + fact.memberId,
          fact.memberId
        ];
        emit(key, 1);
      }
    }

    var networkExtraMemberWay = detail.networkFacts.networkExtraMemberWay;
    if (networkExtraMemberWay) {
      for (var i = 0; i < networkExtraMemberWay.length; i++) {
        var fact = networkExtraMemberWay[i];
        var key = [
          a.country,
          a.networkType,
          "NetworkExtraMemberWay",
          a.name,
          a.id,
          "" + fact.memberId,
          fact.memberId
        ];
        emit(key, 1);
      }
    }

    var networkExtraMemberRelation = detail.networkFacts.networkExtraMemberRelation;
    if (networkExtraMemberRelation) {
      for (var i = 0; i < networkExtraMemberRelation.length; i++) {
        var fact = networkExtraMemberRelation[i];
        var key = [
          a.country,
          a.networkType,
          "NetworkExtraMemberRelation",
          a.name,
          a.id,
          "" + fact.memberId,
          fact.memberId
        ];
        emit(key, 1);
      }
    }

    var nameMissing = detail.networkFacts.nameMissing;
    if (nameMissing) {
      var key = [
        a.country,
        a.networkType,
        "NameMissing",
        a.name,
        a.id
      ];
      emit(key, 1);
    }
  }
}
