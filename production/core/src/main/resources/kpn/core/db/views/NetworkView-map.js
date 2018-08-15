if (doc && doc.network && doc.network.attributes) {
  if (doc.network.active === true) {
    var a = doc.network.attributes;
    var key = [
      a.country,
      a.networkType,
      a.name,
      a.id
    ];
    emit(key, a);
  }
}
