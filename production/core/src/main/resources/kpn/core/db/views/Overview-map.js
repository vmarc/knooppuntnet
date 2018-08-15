var emitCount = function (key, index, count) {
  var value = [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0];
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
  }

  if (country === "be") {
    if (networkType === "rwn") {
      return 5;
    }
    if (networkType === "rcn") {
      return 6;
    }
    if (networkType === "rhn") {
      return 7;
    }
    if (networkType === "rmn") {
      return 8;
    }
    if (networkType === "rpn") {
      return 9;
    }
  }
  if (country === "de") {
    if (networkType === "rwn") {
      return 10;
    }
    if (networkType === "rcn") {
      return 11;
    }
    if (networkType === "rhn") {
      return 12;
    }
    if (networkType === "rmn") {
      return 13;
    }
    if (networkType === "rpn") {
      return 14;
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

        var routeFactMap = {};
        if (doc.network.detail) {
          for (var i = 0; i < doc.network.detail.routes.length; i++) {
            var facts = doc.network.detail.routes[i].facts;
            for (var j = 0; j < facts.length; j++) {
              var fact = facts[j];
              var count = routeFactMap[fact];
              if (count) {
                count = count + 1;
              }
              else {
                count = 1;
              }
              routeFactMap[fact] = count;
            }
          }

          for (fact in routeFactMap) {
            emitCount(fact + "NetworkCount", index, 1);
            emitCount(fact + "Count", index, routeFactMap[fact]);
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
