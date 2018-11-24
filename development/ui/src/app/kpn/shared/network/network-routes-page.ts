// this class is generated, please do not modify

import {List} from 'immutable';
import {NetworkRouteInfo} from './network-route-info';
import {NetworkSummary} from './network-summary';
import {NetworkType} from '../network-type';
import {TimeInfo} from '../time-info';

export class NetworkRoutesPage {
  readonly timeInfo: TimeInfo;
  readonly networkType: NetworkType;
  readonly networkSummary: NetworkSummary;
  readonly routes: List<NetworkRouteInfo>;

  constructor(timeInfo: TimeInfo,
              networkType: NetworkType,
              networkSummary: NetworkSummary,
              routes: List<NetworkRouteInfo>) {
    this.timeInfo = timeInfo;
    this.networkType = networkType;
    this.networkSummary = networkSummary;
    this.routes = routes;
  }

  public static fromJSON(jsonObject): NetworkRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      NetworkType.fromJSON(jsonObject.networkType),
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      jsonObject.routes ? List(jsonObject.routes.map(json => NetworkRouteInfo.fromJSON(json))) : List()
    );
  }
}
