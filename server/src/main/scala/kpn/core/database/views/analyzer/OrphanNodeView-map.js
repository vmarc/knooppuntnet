if (doc && doc.node) {
  var n = doc.node;
  if (n.active === true && n.orphan === true) {
    if (n.rwnName.length > 0) {
      var key = [
        n.country,
        "rwn",
        n.id
      ];
      emit(key, n);
    }
    if (n.rcnName.length > 0) {
      var key = [
        n.country,
        "rcn",
        n.id
      ];
      emit(key, n);
    }
    if (n.rhnName.length > 0) {
      var key = [
        n.country,
        "rhn",
        n.id
      ];
      emit(key, n);
    }
    if (n.rmnName.length > 0) {
      var key = [
        n.country,
        "rmn",
        n.id
      ];
      emit(key, n);
    }
    if (n.rpnName.length > 0) {
      var key = [
        n.country,
        "rpn",
        n.id
      ];
      emit(key, n);
    }
    if (n.rinName.length > 0) {
      var key = [
        n.country,
        "rin",
        n.id
      ];
      emit(key, n);
    }
  }
}
