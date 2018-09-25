var emitCount = function (key, index, count) {
  var value = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
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
  return -1;
};

var subsetIndex = function (element) {
  return calculateSubsetIndex(element.country, element.networkType);
};

if (doc) {

  if (doc.network && doc.network.attributes) {

    var index = subsetIndex(doc.network.attributes);

    if (index >= 0) {

      if (doc.network.active === true && doc.network.ignored === false) {

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
              }
              else {
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
              }
              else {
                count = 1;
              }
              factMap[fact] = count;
            }
          }

          for (fact in factMap) {
            emitCount(fact + "NetworkCount", index, 1);
            emitCount(fact + "Count", index, factMap[fact]);
          }

          var integrityCheck = doc.network.detail.networkFacts.integrityCheck;
          if (integrityCheck) {
            emitCount("IntegrityCheckNetworkCount", index, 1);
            emitCount("IntegrityCheckCount", index, integrityCheck.count);
            emitCount("IntegrityCheckFailedCount", index, integrityCheck.failed);
          }
        }
      }
    }
  }
  else if (doc.node) {
    if (doc.node.orphan === true && doc.node.display === true && doc.node.active === true && doc.node.ignored === false) {
      if (doc.node.rcnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rcn");
        emitCount("OrphanNodeCount", index, 1);
      }
      if (doc.node.rwnName.length > 0) {
        var index = calculateSubsetIndex(doc.node.country, "rwn");
        emitCount("OrphanNodeCount", index, 1);
      }
    }
  }
  else if (doc.route && doc.route.summary) {
    if (doc.route.orphan === true && doc.route.display === true && doc.route.active === true && doc.route.ignored === false) {
      var index = subsetIndex(doc.route.summary);
      emitCount("OrphanRouteCount", index, 1);
    }
  }
}
