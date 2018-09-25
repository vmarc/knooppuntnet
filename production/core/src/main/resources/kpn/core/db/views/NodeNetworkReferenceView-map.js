if (doc && doc.network) {

  var attributes = doc.network.attributes;
  var detail = doc.network.detail;

  if (detail) {
    for (var i = 0; i < detail.nodes.length; i++) {
      var node = detail.nodes[i];
      var routes = [];
      for (var j = 0; j < detail.routes.length; j++) {
        var route = detail.routes[j];
        for (var k = 0; k < node.routeReferences.length; k++) {
          if (node.routeReferences[k].id === route.id) {
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
        attributes.id
      ];

      var value = {
        networkType: attributes.networkType,
        networkId: attributes.id,
        networkName: attributes.name,
        nodeConnection: node.connection,
        nodeRoleConnection: node.roleConnection,
        nodeDefinedInRelation: node.definedInRelation,
        nodeIntegrityCheck: nodeIntegrityCheck,
        facts: node.facts,
        routes: routes
      };

      emit(key, value);
    }
  }
}
