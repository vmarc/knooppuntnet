if (doc) {
  if (doc.node && doc.node.active === true && doc.node.location) {
    for (var nodeNameIndex = 0; nodeNameIndex < doc.node.names.length; nodeNameIndex++) {
      var nodeName = doc.node.names[nodeNameIndex];
      var country = doc.node.country;
      var networkType = nodeName.scopedNetworkType.networkType;
      for (var nodeLocationNameIndex = 0; nodeLocationNameIndex < doc.node.location.names.length; nodeLocationNameIndex++) {
        var nodeLocationName = doc.node.location.names[nodeLocationNameIndex];
        for (var nodeFactIndex = 0; nodeFactIndex < doc.node.facts.length; nodeFactIndex++) {
          var nodeFact = doc.node.facts[nodeFactIndex];
          var nodeKey = [
            networkType,
            nodeLocationName,
            "node",
            nodeFact,
            nodeName.name,
            doc.node.id
          ];
          emit(nodeKey, 1);
        }
      }
    }
  } else if (doc.route && doc.route.summary && doc.route.analysis && doc.route.analysis.locationAnalysis && doc.route.active === true) {
    var routeLocationNames = doc.route.analysis.locationAnalysis.locationNames;
    for (var routeLocationNameIndex = 0; routeLocationNameIndex < routeLocationNames.length; routeLocationNameIndex++) {
      var routeLocationName = routeLocationNames[routeLocationNameIndex];
      for (var routeFactIndex = 0; routeFactIndex < doc.route.facts.length; routeFactIndex++) {
        var routeFact = doc.route.facts[routeFactIndex];
        if ("RouteBroken" !== routeFact) {
          var routeKey = [
            doc.route.summary.networkType,
            routeLocationName,
            "route",
            routeFact,
            doc.route.summary.name,
            doc.route.summary.id
          ];
          emit(routeKey, 1);
        }
      }
    }
  }
}
