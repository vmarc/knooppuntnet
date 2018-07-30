// this class is generated, please do not modify

import {NetworkAttributes} from '../network/network-attributes';
import {SubsetInfo} from './subset-info';

export class SubsetNetworksPage {

  constructor(public subsetInfo?: SubsetInfo,
              public km?: string,
              public networkCount?: number,
              public nodeCount?: number,
              public routeCount?: number,
              public brokenRouteNetworkCount?: number,
              public brokenRouteNetworkPercentage?: string,
              public brokenRouteCount?: number,
              public brokenRoutePercentage?: string,
              public unaccessibleRouteCount?: number,
              public analysisUpdatedTime?: string,
              public networks?: Array<NetworkAttributes>) {
  }

  public static fromJSON(jsonObject): SubsetNetworksPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new SubsetNetworksPage();
    instance.subsetInfo = jsonObject.subsetInfo;
    instance.km = jsonObject.km;
    instance.networkCount = jsonObject.networkCount;
    instance.nodeCount = jsonObject.nodeCount;
    instance.routeCount = jsonObject.routeCount;
    instance.brokenRouteNetworkCount = jsonObject.brokenRouteNetworkCount;
    instance.brokenRouteNetworkPercentage = jsonObject.brokenRouteNetworkPercentage;
    instance.brokenRouteCount = jsonObject.brokenRouteCount;
    instance.brokenRoutePercentage = jsonObject.brokenRoutePercentage;
    instance.unaccessibleRouteCount = jsonObject.unaccessibleRouteCount;
    instance.analysisUpdatedTime = jsonObject.analysisUpdatedTime;
    instance.networks = jsonObject.networks ? jsonObject.networks.map(json => NetworkAttributes.fromJSON(json)) : [];
    return instance;
  }
}

