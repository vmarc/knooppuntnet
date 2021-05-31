var emitKey = function (elementType, key) {
  emit([elementType, key.elementId, key.replicationNumber, key.changeSetId], 1);
};

if (doc.changeSetSummary) {
  emitKey("summary", doc.changeSetSummary.key);
} else if (doc.networkChange) {
  emitKey("network", doc.networkChange.key);
} else if (doc.routeChange) {
  emitKey("route", doc.routeChange.key);
} else if (doc.nodeChange) {
  emitKey("node", doc.nodeChange.key);
} else if (doc.locationChangeSetSummary) {
  emitKey("location-summary", doc.locationChangeSetSummary.key);
} else {
  emitKey("unknown", doc._id);
}
