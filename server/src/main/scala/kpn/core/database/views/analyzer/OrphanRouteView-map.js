if (doc && doc.route && doc.route.active === true) {
  var r = doc.route;
  if (r.active === true && r.orphan === true) {
    var key = [
      r.orphan,
      r.summary.country,
      r.summary.networkType,
      r.summary.id
    ];
    emit(key, r.summary);
  }
}
