if (doc) {

  if (doc.network && doc.network.active === true) {
    var network = doc.network;
    var a = doc.network.attributes;
    var detail = doc.network.detail;
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

    if (detail) {
      var routes = detail.routes;
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

      var nodes = detail.nodes;
      for (var i = 0; i < nodes.length; i++) {
        var node = nodes[i];
        for (var j = 0; j < node.facts.length; j++) {
          var fact = node.facts[j];
          var key = [
            a.country,
            a.networkType,
            fact,
            a.name,
            a.id,
            node.name,
            node.id
          ];
          emit(key, 1);
        }
      }

      var integrityCheckFailed = detail.networkFacts.integrityCheckFailed;
      if (integrityCheckFailed) {
        for (var i = 0; i < integrityCheckFailed.checks.length; i++) {
          var check = integrityCheckFailed.checks[i];
          if (check.failed) {
            var key = [
              a.country,
              a.networkType,
              "IntegrityCheckFailed",
              a.name,
              a.id,
              check.nodeName,
              check.nodeId
            ];
            emit(key, 1);
          }
        }
      }

      var networkExtraMemberNode = detail.networkFacts.networkExtraMemberNode;
      if (networkExtraMemberNode) {
        for (var i = 0; i < networkExtraMemberNode.length; i++) {
          var fact = networkExtraMemberNode[i];
          var key = [
            a.country,
            a.networkType,
            "NetworkExtraMemberNode",
            a.name,
            a.id,
            "" + fact.memberId,
            fact.memberId
          ];
          emit(key, 1);
        }
      }

      var networkExtraMemberWay = detail.networkFacts.networkExtraMemberWay;
      if (networkExtraMemberWay) {
        for (var i = 0; i < networkExtraMemberWay.length; i++) {
          var fact = networkExtraMemberWay[i];
          var key = [
            a.country,
            a.networkType,
            "NetworkExtraMemberWay",
            a.name,
            a.id,
            "" + fact.memberId,
            fact.memberId
          ];
          emit(key, 1);
        }
      }

      var networkExtraMemberRelation = detail.networkFacts.networkExtraMemberRelation;
      if (networkExtraMemberRelation) {
        for (var i = 0; i < networkExtraMemberRelation.length; i++) {
          var fact = networkExtraMemberRelation[i];
          var key = [
            a.country,
            a.networkType,
            "NetworkExtraMemberRelation",
            a.name,
            a.id,
            "" + fact.memberId,
            fact.memberId
          ];
          emit(key, 1);
        }
      }

      var nameMissing = detail.networkFacts.nameMissing;
      if (nameMissing) {
        var key = [
          a.country,
          a.networkType,
          "NameMissing",
          a.name,
          a.id
        ];
        emit(key, 1);
      }
    }
  } else if (doc.node && doc.node.active === true) {
    if (doc.node.orphan === true && doc.node.display === true && doc.node.ignored === false) {
      for (var j = 0; j < doc.node.facts.length; j++) {
        var fact = doc.node.facts[j];
        var networkType = "";
        var name = "";
        if (doc.node.rcnName.length > 0) {
          networkType = "rcn";
          name = doc.node.rcnName;
        } else if (doc.node.rwnName.length > 0) {
          networkType = "rwn";
          name = doc.node.rwnName;
        } else if (doc.node.rhnName.length > 0) {
          networkType = "rhn";
          name = doc.node.rhnName;
        } else if (doc.node.rmnName.length > 0) {
          networkType = "rmn";
          name = doc.node.rmnName;
        } else if (doc.node.rpnName.length > 0) {
          networkType = "rpn";
          name = doc.node.rpnName;
        } else if (doc.node.rinName.length > 0) {
          networkType = "rin";
          name = doc.node.rinName;
        }
        if (networkType.length > 0) {
          var key = [
            doc.node.country,
            networkType,
            fact,
            "OrphanNodes",
            0,
            name,
            doc.node.id
          ];
          emit(key, 1);
        }
      }
    }
  } else if (doc.route && doc.route.summary && doc.route.active === true) {
    if (doc.route.orphan === true && doc.route.display === true && doc.route.ignored === false) {
      for (var j = 0; j < doc.route.facts.length; j++) {
        var fact = doc.route.facts[j];
        var key = [
          doc.route.summary.country,
          doc.route.summary.networkType,
          fact,
          "OrphanRoutes",
          0,
          doc.route.summary.name,
          doc.route.summary.id
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
              0,
              node.name,
              node.id
            ];
            emit(key, 1);
          }
        }
      }
    }
  }
}
