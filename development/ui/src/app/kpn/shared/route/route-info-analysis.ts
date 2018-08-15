// this class is generated, please do not modify

import {RouteMap} from './route-map';
import {RouteNetworkNodeInfo} from './route-network-node-info';

export class RouteInfoAnalysis {

  constructor(public startNodes?: Array<RouteNetworkNodeInfo>,
              public endNodes?: Array<RouteNetworkNodeInfo>,
              public startTentacleNodes?: Array<RouteNetworkNodeInfo>,
              public endTentacleNodes?: Array<RouteNetworkNodeInfo>,
              public unexpectedNodeIds?: Array<number>,
              public expectedName?: string,
              public map?: RouteMap,
              public structureStrings?: Array<string>) {
  }

  public static fromJSON(jsonObject): RouteInfoAnalysis {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteInfoAnalysis();
    instance.startNodes = jsonObject.startNodes ? jsonObject.startNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.endNodes = jsonObject.endNodes ? jsonObject.endNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.startTentacleNodes = jsonObject.startTentacleNodes ? jsonObject.startTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.endTentacleNodes = jsonObject.endTentacleNodes ? jsonObject.endTentacleNodes.map(json => RouteNetworkNodeInfo.fromJSON(json)) : [];
    instance.unexpectedNodeIds = jsonObject.unexpectedNodeIds;
    instance.expectedName = jsonObject.expectedName;
    instance.map = RouteMap.fromJSON(jsonObject.map);
    instance.structureStrings = jsonObject.structureStrings;
    return instance;
  }
}

