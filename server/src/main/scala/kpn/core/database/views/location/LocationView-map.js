if (doc) {
  if (doc.node && doc.node.active === true) {
    if (doc.node.location) {
      var nodeNames = doc.node.names;
      for (var i = 0; i < nodeNames.length; i++) {
        var nodeName = doc.node.names[i];
        var networkType = nodeName.scopedNetworkType.networkType;
        emit(["node", networkType].concat(doc.node.location.names), [nodeName.name, doc.node.id]);
      }
    }
  } else if (doc.route && doc.route.active === true) {
    var routeAnalysis = doc.route.analysis;
    if (routeAnalysis) {
      var s = doc.route.summary;
      var locationAnalysis = routeAnalysis.locationAnalysis;
      if (locationAnalysis) {
        emit(["route", s.networkType].concat(locationAnalysis.location.names), [s.name, s.id]);
      } else {
        emit(["route-without-location", s.networkType], [s.name, s.id]);
      }
    }
  }
}
