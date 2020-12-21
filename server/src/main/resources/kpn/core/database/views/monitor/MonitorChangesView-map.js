if (doc.monitorRouteChange) {
  var key = doc.monitorRouteChange.key;
  var routeId = key.elementId;
  var timestamp = key.timestamp;
  var year = timestamp.substring(0, 4);
  var month = timestamp.substring(5, 7);
  var day = timestamp.substring(8, 10);
  var time = timestamp.substring(11, 19);

  var impacted = 0;
  if (doc.monitorRouteChange.happy === true || doc.monitorRouteChange.investigate === true) {
    impacted = 1;
  }

  var changeSetId = key.changeSetId.toString();
  var replicationNumber = key.replicationNumber.toString();

  emit(["change", year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
  if (impacted > 0) {
    emit(["impacted:change", year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
  }

  var groupName = doc.monitorRouteChange.groupName;

  emit(["group", groupName, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
  if (impacted > 0) {
    emit(["impacted:group", groupName, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
  }

  emit(["route", routeId, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
  if (impacted > 0) {
    emit(["impacted:route", routeId, year, month, day, time, changeSetId, replicationNumber], [1, impacted]);
  }
}
