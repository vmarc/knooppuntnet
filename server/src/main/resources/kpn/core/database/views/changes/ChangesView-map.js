if (doc._id.indexOf("change:") === 0) {
  if (doc.changeSetSummary) {
    var key = doc.changeSetSummary.key;
    var timestamp = key.timestamp;
    var year = timestamp.substring(0, 4);
    var month = timestamp.substring(5, 7);
    var day = timestamp.substring(8, 10);
    var time = timestamp.substring(11, 19);

    var impacted = 0;
    if (doc.changeSetSummary.happy === true || doc.changeSetSummary.investigate === true) {
      impacted = 1;
    }

    var changeSetId = key.changeSetId.toString();
    var replicationNumber = key.replicationNumber.toString();

    emit(["change-set", year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
    if (impacted > 0) {
      emit(["impacted:change-set", year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
    }

    if (doc.changeSetSummary.subsetAnalyses) {
      for (var i = 0; i < doc.changeSetSummary.subsetAnalyses.length; i++) {
        var subsetAnalysis = doc.changeSetSummary.subsetAnalyses[i];
        var subsetImpacted = 0;
        if (subsetAnalysis.happy || subsetAnalysis.investigate) {
          subsetImpacted = 1;
        }
        var subset = subsetAnalysis.subset;
        var subsetString = subset; // for backward compatibility of Subset serialization
        if (subset.country) {
          subsetString = subset.country + ":" + subset.networkType;
        }
        emit([subsetString + ":change-set", year, month, day, time, changeSetId, replicationNumber], [1, subsetImpacted]);
        if (subsetImpacted > 0) {
          emit([subsetString + ":impacted:change-set", year, month, day, time, changeSetId, replicationNumber], [1, subsetImpacted]);
        }
      }
    }
  }
  else if (doc.locationChange) {
    var key = doc.locationChange.key;
    var timestamp = key.timestamp;
    var year = timestamp.substring(0, 4);
    var month = timestamp.substring(5, 7);
    var day = timestamp.substring(8, 10);
    var time = timestamp.substring(11, 19);

    var impacted = 0;
    if (doc.locationChange.happy === true || doc.locationChange.investigate === true) {
      impacted = 1;
    }

    var locationName = doc.locationChange.locationName;
    var changeSetId = key.changeSetId.toString();
    var replicationNumber = key.replicationNumber.toString();

    emit(["location", locationName, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
    if (impacted === 1) {
      emit(["impacted:location", locationName, year, month, day, time, changeSetId, replicationNumber], [1, 1]);
    }
  }
  else if (doc.networkChange) {
    var key = doc.networkChange.key;
    var timestamp = key.timestamp;
    var year = timestamp.substring(0, 4);
    var month = timestamp.substring(5, 7);
    var day = timestamp.substring(8, 10);
    var time = timestamp.substring(11, 19);

    var impacted = 0;
    if (doc.networkChange.happy === true || doc.networkChange.investigate === true) {
      impacted = 1;
    }

    var networkId = key.elementId.toString();
    var changeSetId = key.changeSetId.toString();
    var replicationNumber = key.replicationNumber.toString();

    emit(["network", networkId, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
    if (impacted === 1) {
      emit(["impacted:network", networkId, year, month, day, time, changeSetId, replicationNumber], [1, 1]);
    }
  }
  else if (doc.routeChange) {
    var key = doc.routeChange.key;
    var timestamp = key.timestamp;
    var year = timestamp.substring(0, 4);
    var month = timestamp.substring(5, 7);
    var day = timestamp.substring(8, 10);
    var time = timestamp.substring(11, 19);

    var impacted = 0;
    if (doc.routeChange.happy === true || doc.routeChange.investigate === true) {
      impacted = 1;
    }

    var routeId = key.elementId.toString();
    var changeSetId = key.changeSetId.toString();
    var replicationNumber = key.replicationNumber.toString();

    emit(["route", routeId, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
    if (impacted === 1) {
      emit(["impacted:route", routeId, year, month, day, time, changeSetId, replicationNumber], [1, 1]);
    }
  }
  else if (doc.nodeChange) {
    var key = doc.nodeChange.key;
    var timestamp = key.timestamp;
    var year = timestamp.substring(0, 4);
    var month = timestamp.substring(5, 7);
    var day = timestamp.substring(8, 10);
    var time = timestamp.substring(11, 19);

    var impacted = 0;
    if (doc.nodeChange.happy === true || doc.nodeChange.investigate === true) {
      impacted = 1;
    }

    var nodeId = key.elementId.toString();
    var changeSetId = key.changeSetId.toString();
    var replicationNumber = key.replicationNumber.toString();

    emit(["node", nodeId, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
    if (impacted === 1) {
      emit(["impacted:node", nodeId, year, month, day, time, changeSetId, replicationNumber], [1, 1]);
    }
  }
}
