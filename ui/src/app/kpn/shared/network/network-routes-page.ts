// this class is generated, please do not modify

import {NetworkRouteInfo} from './network-route-info';
import {NetworkSummary} from './network-summary';
import {NetworkType} from '../network-type';
import {TimeInfo} from '../time-info';

export class NetworkRoutesPage {

  constructor(public timeInfo?: TimeInfo,
              public networkType?: NetworkType,
              public networkSummary?: NetworkSummary,
              public routes?: Array<NetworkRouteInfo>) {
  }

  public static fromJSON(jsonObject): NetworkRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkRoutesPage();
    instance.timeInfo = jsonObject.timeInfo;
    instance.networkType = jsonObject.networkType;
    instance.networkSummary = jsonObject.networkSummary;
    instance.routes = jsonObject.routes ? jsonObject.routes.map(json => NetworkRouteInfo.fromJSON(json)) : [];
    return instance;
  }
}

