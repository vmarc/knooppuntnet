var emitFact = function (fact, country, networkType, count) {
  var key = [
    fact,
    country,
    networkType
  ];
  emit(key, count);
};

var emitNetworkFact = function (factName, country, networkType) {
  emitFact(factName + "NetworkCount", country, networkType, 1); // number of networks in which the fact is found
  emitFact(factName + "Count", country, networkType, 1); // number of occurances of the fact
};

if (doc) {

  if (doc.network && doc.network.attributes && doc.network.active === true) {

    var country = doc.network.attributes.country;
    if (country) {
      var networkType = doc.network.attributes.networkType;

      emitFact("NetworkCount", country, networkType, 1);
      emitFact("RouteCount", country, networkType, doc.network.attributes.routeCount);
      emitFact("NodeCount", country, networkType, doc.network.attributes.nodeCount);
      emitFact("MeterCount", country, networkType, doc.network.attributes.meters);

      var facts = doc.network.facts;
      for (var i = 0; i < facts.length; i++) {
        emitNetworkFact(facts[i], country, networkType);
      }

      var factMap = {};
      var detail = doc.network.detail;
      if (detail) {
        var routes = detail.routes;
        for (var i = 0; i < routes.length; i++) {
          var facts = routes[i].facts;
          for (var j = 0; j < facts.length; j++) {
            var fact = facts[j];
            var count = factMap[fact];
            if (count) {
              count = count + 1;
            } else {
              count = 1;
            }
            factMap[fact] = count;
          }
        }

        var nodes = detail.nodes;
        for (var i = 0; i < nodes.length; i++) {
          var facts = nodes[i].facts;
          for (var j = 0; j < facts.length; j++) {
            var fact = facts[j];
            var count = factMap[fact];
            if (count) {
              count = count + 1;
            } else {
              count = 1;
            }
            factMap[fact] = count;
          }
        }

        for (fact in factMap) {
          emitFact(fact + "NetworkCount", country, networkType, 1);
          emitFact(fact + "Count", country, networkType, factMap[fact]);
        }

        var integrityCheck = detail.networkFacts.integrityCheck;
        if (integrityCheck) {
          emitFact("IntegrityCheckNetworkCount", country, networkType, 1);
          emitFact("IntegrityCheckCount", country, networkType, integrityCheck.count);
          emitFact("IntegrityCheckFailedCount", country, networkType, integrityCheck.failed);
        }

        var networkExtraMemberNode = detail.networkFacts.networkExtraMemberNode;
        if (networkExtraMemberNode) {
          emitFact("NetworkExtraMemberNodeCount", country, networkType, networkExtraMemberNode.length);
        }

        var networkExtraMemberWay = detail.networkFacts.networkExtraMemberWay;
        if (networkExtraMemberWay) {
          emitFact("NetworkExtraMemberWayCount", country, networkType, networkExtraMemberWay.length);
        }

        var networkExtraMemberRelation = detail.networkFacts.networkExtraMemberRelation;
        if (networkExtraMemberRelation) {
          emitFact("NetworkExtraMemberRelationCount", country, networkType, networkExtraMemberRelation.length);
        }

        var nameMissing = detail.networkFacts.nameMissing;
        if (nameMissing) {
          emitFact("NameMissingCount", country, networkType, 1);
        }
      }
    }
  } else if (doc.node && doc.node.active === true) {
    if (doc.node.orphan === true && doc.node.active === true) {
      var country = doc.node.country;
      if (country) {
        var nodeNames = doc.node.names;
        for (var k = 0; k < nodeNames.length; k++) {
          var nodeName = doc.node.names[k];
          var networkType = nodeName.scopedNetworkType.networkType;
          emitFact("OrphanNodeCount", country, networkType, 1);
          for (var j = 0; j < doc.node.facts.length; j++) {
            var fact = doc.node.facts[j];
            emitFact(fact + "Count", country, networkType, 1);
          }
        }
      }
    }
  } else if (doc.route && doc.route.summary && doc.route.active === true) {
    if (doc.route.orphan === true && doc.route.active === true) {
      var country = doc.route.summary.country;
      if (country) {
        var networkType = doc.route.summary.networkType;

        emitFact("OrphanRouteCount", country, networkType, 1);
        emitFact("RouteCount", country, networkType, 1);
        emitFact("NodeCount", country, networkType, doc.route.summary.nodeNames.length);
        emitFact("MeterCount", country, networkType, doc.route.summary.meters);

        for (var j = 0; j < doc.route.facts.length; j++) {
          var fact = doc.route.facts[j];
          emitFact(fact + "Count", country, networkType, 1);
        }

        var a = doc.route.analysis;
        if (a) {
          var allNodes = [];
          if (a.map.startNodes) {
            allNodes = allNodes.concat(a.map.startNodes);
          }
          if (a.map.endNodes) {
            allNodes = allNodes.concat(a.map.endNodes);
          }
          if (a.map.startTentacleNodes) {
            allNodes = allNodes.concat(a.map.startTentacleNodes);
          }
          if (a.map.endTentacleNodes) {
            allNodes = allNodes.concat(a.map.endTentacleNodes);
          }
          for (var i = 0; i < allNodes.length; i++) {
            var node = allNodes[i];
            emitFact("NodeNetworkTypeNotTaggedCount", country, networkType, 1);
          }
        }
      }
    }
  }
}
