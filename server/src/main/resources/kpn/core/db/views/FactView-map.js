if (doc) {
  if (doc.network && doc.network.active === true) {
    var a = doc.network.attributes;
    var facts = doc.network.facts;
    for (var i = 0; i < facts.length; i++) {
      var fact = facts[i];
      var key = [
        a.country,
        a.networkType,
        fact,
        a.name,
        a.id
      ];
      emit(key, 1);
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
            a.id
          ];
          emit(key, 1);
        }
      }

      var nodes = doc.network.detail.nodes;
      for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
        for (var j = 0; j < node.facts.length; j++) {
          var fact = node.facts[j];
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
    }
  } else if (doc.node && doc.node.active === true) {
    if (doc.node.orphan === true) {
      for (var j = 0; j < doc.node.facts.length; j++) {
        var fact = doc.node.facts[j];
        var networkType = "";
        if (doc.node.rcnName.length > 0) {
          networkType = "rcn";
        }
        else if (doc.node.rwnName.length > 0) {
          networkType = "rwn";
        }
        else if (doc.node.rhnName.length > 0) {
          networkType = "rhn";
        }
        else if (doc.node.rmnName.length > 0) {
          networkType = "rmn";
        }
        else if (doc.node.rpnName.length > 0) {
          networkType = "rpn";
        }
        else if (doc.node.rinName.length > 0) {
          networkType = "rin";
        }
        if (networkType.length > 0) {
          var key = [
            doc.node.country,
            networkType,
            fact,
            "OrphanNodes",
            0
          ];
          emit(key, 1);
        }
      }
    }
  } else if (doc.route && doc.route.summary && doc.route.active === true) {
    if (doc.route.orphan === true) {
      for (var j = 0; j < doc.route.facts.length; j++) {
        var fact = doc.route.facts[j];
        var key = [
          doc.route.summary.country,
          doc.route.summary.networkType,
          fact,
          "OrphanRoutes",
          0
        ];
        emit(key, 1);
      }

      var a = doc.route.analysis;
      if (a) {
        var allNodes = [];
        if (a.startNodes) {
          allNodes = allNodes.concat(a.startNodes);
        }
        if (a.endNodes) {
          allNodes = allNodes.concat(a.endNodes);
        }
        if (a.startTentacleNodes) {
          allNodes = allNodes.concat(a.startTentacleNodes);
        }
        if (a.endTentacleNodes) {
          allNodes = allNodes.concat(a.endTentacleNodes);
        }

        for (var i = 0; i < allNodes.length; i++) {
          var node = allNodes[i];
          if (node.networkTypeTagged === false) {
            var key = [
              doc.route.summary.country,
              doc.route.summary.networkType,
              "NodeNetworkTypeNotTagged",
              "NodesInOrphanRoutes",
              0
            ];
            emit(key, 1);
          }
        }
      }
    }
  }
}
