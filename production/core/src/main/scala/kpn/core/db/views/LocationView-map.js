if (doc) {
  if (doc.node && doc.node.active === true && doc.node.ignored === false) {
    if (doc.node.location) {
      var name = "";
      var networkType = "";
      if (doc.node.rcnName.length > 0) {
        name = doc.node.rcnName;
        networkType = "cycling";
      } else if (doc.node.rwnName.length > 0) {
        name = doc.node.rwnName;
        networkType = "hiking";
      } else if (doc.node.rhnName.length > 0) {
        name = doc.node.rhnName;
        networkType = "horse-riding";
      } else if (doc.node.rmnName.length > 0) {
        name = doc.node.rmnName;
        networkType = "motorboat";
      } else if (doc.node.rpnName.length > 0) {
        name = doc.node.rpnName;
        networkType = "canoe";
      } else if (doc.node.rinName.length > 0) {
        name = doc.node.rinName;
        networkType = "inline-skating";
      }
      if (networkType.length > 0) {
        emit(["node", networkType].concat(doc.node.location.names), [name, doc.node.id]);
      }
    }
  } else if (doc.route && doc.route.active === true && doc.route.ignored === false) {
    var routeAnalysis = doc.route.analysis;
    if (routeAnalysis) {
      var s = doc.route.summary;
      var routeNetworkType = "";
      if (s.networkType === "rcn") {
        routeNetworkType = "cycling";
      } else if (s.networkType === "rwn") {
        routeNetworkType = "hiking";
      } else if (s.networkType === "rhn") {
        routeNetworkType = "horse-riding";
      } else if (s.networkType === "rmn") {
        routeNetworkType = "motorboat";
      } else if (s.networkType === "rpn") {
        routeNetworkType = "canoe";
      } else if (s.networkType === "rin") {
        routeNetworkType = "inline-skating";
      }
      if (routeNetworkType.length > 0) {
        var locationAnalysis = routeAnalysis.locationAnalysis;
        if (locationAnalysis) {
          emit(["route", routeNetworkType].concat(locationAnalysis.location.names), [s.name, s.id]);
        }
        else {
          emit(["route-without-location", routeNetworkType], [s.name, s.id]);
        }
      }
    }
  }
}
