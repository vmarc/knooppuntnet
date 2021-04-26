if (doc && doc.node && doc.node.active === true && doc.node.location) {
  for (var nodeNameIndex = 0; nodeNameIndex < doc.node.names.length; nodeNameIndex++) {
    var nodeName = doc.node.names[nodeNameIndex];
    var country = doc.node.country;
    var networkType = nodeName.networkType;
    for (var nodeLocationNameIndex = 0; nodeLocationNameIndex < doc.node.location.names.length; nodeLocationNameIndex++) {
      var nodeLocationName = doc.node.location.names[nodeLocationNameIndex];
      emit(
        [
          networkType,
          country,
          nodeLocationName,
          nodeName.name,
          doc.node.id
        ],
        {
          latitude: doc.node.latitude,
          longitude: doc.node.longitude,
          lastUpdated: doc.node.lastUpdated,
          lastSurvey: doc.node.lastSurvey,
          factCount: doc.node.facts.length
        }
      );
    }
  }
}
