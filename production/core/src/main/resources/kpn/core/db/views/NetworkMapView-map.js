if (doc && doc.network && doc.network.detail && doc.network.detail.shape && doc.network.active === true) {
  var a = doc.network.attributes;
  var s = doc.network.detail.shape;
  var key = [
    a.country,
    a.networkType,
    a.name,
    a.id
  ];
  emit(key, s);
}
