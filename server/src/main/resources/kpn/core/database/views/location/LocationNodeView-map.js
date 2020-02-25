if (doc && doc.node && doc.node.active === true && doc.node.location) {
  for (var nodeNameIndex = 0; nodeNameIndex < doc.node.names.length; nodeNameIndex++) {
    var nodeName = doc.node.names[nodeNameIndex];
    var networkType = nodeName.scopedNetworkType.networkType;
    for (var nodeLocationNameIndex = 0; nodeLocationNameIndex < doc.node.location.names.length; nodeLocationNameIndex++) {
      var nodeLocationName = doc.node.location.names[nodeLocationNameIndex];
      emit(
        [
          networkType,
          nodeLocationName,
          nodeName.name,
          doc.node.id
        ],
        [
          doc.node.latitude,
          doc.node.longitude,
          doc.node.lastUpdated,
          doc.node.facts.length
        ]
      );
    }
  }
}
