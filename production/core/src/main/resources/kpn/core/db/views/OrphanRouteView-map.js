if (doc && doc.route) {
  var r = doc.route;
  if (r.active === true && (r.orphan === true || r.ignored === true)) {
    var key = [
      r.ignored,
      r.orphan,
      r.display,
      r.summary.country,
      r.summary.networkType,
      r.summary.id
    ];
    emit(key, r.summary);
  }
}
