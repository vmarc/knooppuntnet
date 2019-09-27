if (doc) {
  if (doc.node && doc.node.active === true && doc.node.ignored === false) {
    if (doc.node.location) {
      var name = "";
      var networkType = "";
      if (doc.node.rcnName.length > 0) {
        name = doc.node.rcnName;
        networkType = "rcn";
      } else if (doc.node.rwnName.length > 0) {
        name = doc.node.rwnName;
        networkType = "rwn";
      } else if (doc.node.rhnName.length > 0) {
        name = doc.node.rhnName;
        networkType = "rhn";
      } else if (doc.node.rmnName.length > 0) {
        name = doc.node.rmnName;
        networkType = "rmn";
      } else if (doc.node.rpnName.length > 0) {
        name = doc.node.rpnName;
        networkType = "rpn";
      } else if (doc.node.rinName.length > 0) {
        name = doc.node.rinName;
        networkType = "rin";
      }
      if (networkType.length > 0) {
        emit(["node", networkType].concat(doc.node.location.names), [name, doc.node.id]);
      }
    }
  } else if (doc.route && doc.route.active === true && doc.route.ignored === false) {
    var routeAnalysis = doc.route.analysis;
    if (routeAnalysis) {
      var s = doc.route.summary;
      var locationAnalysis = routeAnalysis.locationAnalysis;
      if (locationAnalysis) {
        emit(["route", s.networkType].concat(locationAnalysis.location.names), [s.name, s.id]);
      }
    }
  }
}
