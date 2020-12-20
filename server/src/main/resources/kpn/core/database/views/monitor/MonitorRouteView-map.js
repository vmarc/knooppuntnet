if (doc.monitorGroup) {
  emit(['group', doc.monitorGroup.name], 1);
} else if (doc.monitorRoute) {
  var routeId = doc.monitorRoute.id;
  var groupName = doc.monitorRoute.groupName;
  emit(['group-route', groupName, routeId], 1);
}
