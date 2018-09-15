if (doc && doc.network) {

  var a = doc.network.attributes;
  var d = doc.network.detail;
  if (d) {
    for (var i = 0; i < d.nodes.length; i++) {
      var node = d.nodes[i];
      var routes = [];
      for (var j = 0; j < d.routes.length; j++) {
        var route = d.routes[j];
        for (var k = 0; k < node.routeReferences.length; k++) {
          if (node.routeReferences[k].id == route.id) {
            var routeInfo = {
              routeId: route.id,
              routeName: route.name,
              routeRole: route.role
            };
            routes.push(routeInfo);
          }
        }
      }

      var nodeIntegrityCheck = null;
      if (node.integrityCheck) {
        nodeIntegrityCheck = {
          failed: node.integrityCheck.failed,
          expected: node.integrityCheck.expected,
          actual: node.integrityCheck.actual
        };
      }

      var key = [
        node.id,
        "network",
        a.id
      ];

      var value = {
        networkId: a.id,
        networkType: a.networkType,
        networkName: a.name,
        nodeConnection: node.connection,
        nodeDefinedInRelation: node.definedInRelation,
        nodeIntegrityCheck: nodeIntegrityCheck,
        routes: routes
      };

      emit(key, value);
    }
  }
}
