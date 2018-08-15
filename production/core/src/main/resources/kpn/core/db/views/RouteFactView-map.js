if (doc && doc.network) {
  var network = doc.network;
  var a = doc.network.attributes;
  var facts = doc.network.facts;
  for (var i = 0; i < facts.length; i++) {
    var fact = facts[i];
    if (fact !== "IntegrityCheckFailed") {
      var key = [
        a.country,
        a.networkType,
        fact,
        a.name,
        a.id
      ];
      emit(key, 1);
    }
  }

  if (doc.network.detail) {
    var routes = doc.network.detail.routes;
    for (var i = 0; i < routes.length; i++) {
      var route = routes[i];
      for (var j = 0; j < route.facts.length; j++) {
        var fact = route.facts[j];
        var key = [
          a.country,
          a.networkType,
          fact,
          a.name,
          a.id,
          route.name,
          route.id
        ];
        emit(key, 1);
      }
    }
  }
}
