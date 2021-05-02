if (doc && doc.node && doc.node.active === true && doc.node.location) {
  for (var nodeNameIndex = 0; nodeNameIndex < doc.node.names.length; nodeNameIndex++) {
    var nodeName = doc.node.names[nodeNameIndex];
    var country = doc.node.country;
    var networkType = nodeName.networkType;
    for (var nodeLocationNameIndex = 0; nodeLocationNameIndex < doc.node.location.names.length; nodeLocationNameIndex++) {
      var nodeLocationName = doc.node.location.names[nodeLocationNameIndex];
      var value = {
        longName: nodeName.longName,
        latitude: doc.node.latitude,
        longitude: doc.node.longitude,
        lastUpdated: doc.node.lastUpdated,
        lastSurvey: doc.node.lastSurvey,
        factCount: doc.node.facts.length
      };
      emit(
        [
          networkType,
          country,
          nodeLocationName,
          'all',
          nodeName.name,
          doc.node.id
        ],
        value
      );
      if (doc.node.facts.length > 0) {
        emit(
          [
            networkType,
            country,
            nodeLocationName,
            'facts',
            nodeName.name,
            doc.node.id
          ],
          value
        );
      }

      if (doc.node.lastSurvey) {
        emit(
          [
            networkType,
            country,
            nodeLocationName,
            'survey',
            doc.node.lastSurvey,
            nodeName.name,
            doc.node.id
          ],
          value
        );
      }
    }
  }
}
