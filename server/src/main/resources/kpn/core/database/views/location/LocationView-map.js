if (doc) {
  if (doc.node && doc.node.active === true) {
    if (doc.node.location) {
      for (var i = 0; i < doc.node.names.length; i++) {
        var nodeName = doc.node.names[i];
        var networkType = nodeName.networkType;
        for (var nodeLocationNameIndex = 0; nodeLocationNameIndex < doc.node.location.names.length; nodeLocationNameIndex++) {
          var nodeLocationName = doc.node.location.names[nodeLocationNameIndex];
          emit(["node", networkType, nodeLocationName], [nodeName.name, doc.node.id]);
        }
      }
    }
  } else if (doc.route && doc.route.active === true) {
    var summary = doc.route.summary;
    var routeAnalysis = doc.route.analysis;
    if (routeAnalysis) {
      var locationAnalysis = routeAnalysis.locationAnalysis;
      if (locationAnalysis) {
        for (var nameIndex = 0; nameIndex < locationAnalysis.locationNames.length; nameIndex++) {
          var name = locationAnalysis.locationNames[nameIndex];
          emit(["route", summary.networkType, name], [summary.name, summary.id]);
        }
      }
    }
  }
}
