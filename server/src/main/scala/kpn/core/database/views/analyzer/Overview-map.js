var emitCount = function (key, index, count) {
  var value = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
  value[index] = count;
  emit(key, value);
};

var emitNetworkFact = function (factName, index) {
  emitCount(factName + "NetworkCount", index, 1); // number of networks in which the fact is found
  emitCount(factName + "Count", index, 1); // number of occurances of the fact
};

var calculateSubsetIndex = function (country, networkType) {
  if (country === "nl") {
    if (networkType === "rwn") {
      return 0;
    }
    if (networkType === "rcn") {
      return 1;
    }
    if (networkType === "rhn") {
      return 2;
    }
    if (networkType === "rmn") {
      return 3;
    }
    if (networkType === "rpn") {
      return 4;
    }
    if (networkType === "rin") {
      return 5;
    }
  }

  if (country === "be") {
    if (networkType === "rwn") {
      return 6;
    }
    if (networkType === "rcn") {
      return 7;
    }
    if (networkType === "rhn") {
      return 8;
    }
    if (networkType === "rmn") {
      return 9;
    }
    if (networkType === "rpn") {
      return 10;
    }
    if (networkType === "rin") {
      return 11;
    }
  }
  if (country === "de") {
    if (networkType === "rwn") {
      return 12;
    }
    if (networkType === "rcn") {
      return 13;
    }
    if (networkType === "rhn") {
      return 14;
    }
    if (networkType === "rmn") {
      return 15;
    }
    if (networkType === "rpn") {
      return 16;
    }
    if (networkType === "rin") {
      return 17;
    }
  }
  if (country === "fr") {
    if (networkType === "rwn") {
      return 18;
    }
    if (networkType === "rcn") {
      return 19;
    }
    if (networkType === "rhn") {
      return 20;
    }
    if (networkType === "rmn") {
      return 21;
    }
    // if (networkType === "rpn") {
    //   return 22;
    // }
    // if (networkType === "rin") {
    //   return 23;
    // }
  }
  return -1;
};

var subsetIndex = function (element) {
  return calculateSubsetIndex(element.country, element.networkType);
};

if (doc) {

  if (doc.network && doc.network.attributes && doc.network.active === true) {

    var index = subsetIndex(doc.network.attributes);

    if (index >= 0) {

      if (doc.network.active === true) {

        emitCount("NetworkCount", index, 1);
        emitCount("RouteCount", index, doc.network.attributes.routeCount);
        emitCount("NodeCount", index, doc.network.attributes.nodeCount);
        emitCount("MeterCount", index, doc.network.attributes.meters);

        var facts = doc.network.facts;
        for (var i = 0; i < facts.length; i++) {
          emitNetworkFact(facts[i], index);
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
            emitCount(fact + "NetworkCount", index, 1);
            emitCount(fact + "Count", index, factMap[fact]);
          }

          var integrityCheck = detail.networkFacts.integrityCheck;
          if (integrityCheck) {
            emitCount("IntegrityCheckNetworkCount", index, 1);
            emitCount("IntegrityCheckCount", index, integrityCheck.count);
            emitCount("IntegrityCheckFailedCount", index, integrityCheck.failed);
          }

          var networkExtraMemberNode = detail.networkFacts.networkExtraMemberNode;
          if (networkExtraMemberNode) {
            emitCount("NetworkExtraMemberNodeCount", index, networkExtraMemberNode.length);
          }

          var networkExtraMemberWay = detail.networkFacts.networkExtraMemberWay;
          if (networkExtraMemberWay) {
            emitCount("NetworkExtraMemberWayCount", index, networkExtraMemberWay.length);
          }

          var networkExtraMemberRelation = detail.networkFacts.networkExtraMemberRelation;
          if (networkExtraMemberRelation) {
            emitCount("NetworkExtraMemberRelationCount", index, networkExtraMemberRelation.length);
          }

          var nameMissing = detail.networkFacts.nameMissing;
          if (nameMissing) {
            emitCount("NameMissingCount", index, 1);
          }
        }
      }
    }
  } else if (doc.node && doc.node.active === true) {
    if (doc.node.orphan === true && doc.node.active === true) {
      var networkType = "";
      if (doc.node.rcnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rcn");
        emitCount("OrphanNodeCount", index, 1);
        for (var j = 0; j < doc.node.facts.length; j++) {
          var fact = doc.node.facts[j];
          emitCount(fact + "Count", index, 1);
        }
      }
      if (doc.node.rwnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rwn");
        emitCount("OrphanNodeCount", index, 1);
        for (var j = 0; j < doc.node.facts.length; j++) {
          var fact = doc.node.facts[j];
          emitCount(fact + "Count", index, 1);
        }
      }
      if (doc.node.rhnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rhn");
        emitCount("OrphanNodeCount", index, 1);
        for (var j = 0; j < doc.node.facts.length; j++) {
          var fact = doc.node.facts[j];
          emitCount(fact + "Count", index, 1);
        }
      }
      if (doc.node.rmnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rmn");
        emitCount("OrphanNodeCount", index, 1);
        for (var j = 0; j < doc.node.facts.length; j++) {
          var fact = doc.node.facts[j];
          emitCount(fact + "Count", index, 1);
        }
      }
      if (doc.node.rpnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rpn");
        emitCount("OrphanNodeCount", index, 1);
        for (var j = 0; j < doc.node.facts.length; j++) {
          var fact = doc.node.facts[j];
          emitCount(fact + "Count", index, 1);
        }
      }
      if (doc.node.rinName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rin");
        emitCount("OrphanNodeCount", index, 1);
        for (var j = 0; j < doc.node.facts.length; j++) {
          var fact = doc.node.facts[j];
          emitCount(fact + "Count", index, 1);
        }
      }
    }
  } else if (doc.route && doc.route.summary && doc.route.active === true) {
    if (doc.route.orphan === true && doc.route.active === true) {
      var index = subsetIndex(doc.route.summary);
      emitCount("OrphanRouteCount", index, 1);
      emitCount("RouteCount", index, 1);
      emitCount("NodeCount", index, doc.route.summary.nodeNames.length);
      emitCount("MeterCount", index, doc.route.summary.meters);

      for (var j = 0; j < doc.route.facts.length; j++) {
        var fact = doc.route.facts[j];
        emitCount(fact + "Count", index, 1);
      }

      var a = doc.route.analysis;
      if (a) {
        var allNodes = [];
        if (a.startNodes) {
          allNodes = allNodes.concat(a.startNodes);
        }
        if (a.endNodes) {
          allNodes = allNodes.concat(a.endNodes);
        }
        if (a.startTentacleNodes) {
          allNodes = allNodes.concat(a.startTentacleNodes);
        }
        if (a.endTentacleNodes) {
          allNodes = allNodes.concat(a.endTentacleNodes);
        }
        for (var i = 0; i < allNodes.length; i++) {
          var node = allNodes[i];
          emitCount("NodeNetworkTypeNotTaggedCount", index, 1);
        }
      }
    }
  }
}
