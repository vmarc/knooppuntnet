if (doc) {
  if (doc.node && doc.node.active === true) {
    if (doc.node.location) {
      for (var i = 0; i < doc.node.names.length; i++) {
        var nodeName = doc.node.names[i];
        var networkType = nodeName.scopedNetworkType.networkType;
        for (var nodeLocationNameIndex = 0; nodeLocationNameIndex < doc.node.location.names.length; nodeLocationNameIndex++) {
          var nodeLocationName = doc.node.location.names[nodeLocationNameIndex];
          emit(["node", networkType, nodeLocationName], [nodeName.name, doc.node.id]);
        }
      }
    }
  } else if (doc.route && doc.route.active === true) {
    var routeAnalysis = doc.route.analysis;
    if (routeAnalysis) {
      var locationNames = [];
      var locationAnalysis = routeAnalysis.locationAnalysis;
      if (locationAnalysis) {
        for (var candidateIndex = 0; candidateIndex < locationAnalysis.candidates.length; candidateIndex++) {
          var candidate = locationAnalysis.candidates[candidateIndex];
          for (var locationNameIndex = 0; locationNameIndex < candidate.location.names.length; locationNameIndex++) {
            var locationName = candidate.location.names[locationNameIndex];
            if (locationNames.indexOf(locationName) === -1) {
              locationNames.push(candidate.location.names[locationNameIndex]);
            }
          }
        }
        for (var nameIndex = 0; nameIndex < locationNames.length; nameIndex++) {
          var name = locationNames[nameIndex];
          emit(["route", doc.route.summary.networkType, name], [doc.route.summary.name, doc.route.summary.id]);
        }
      }
    }
  }
}
