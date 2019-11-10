// this class is generated, please do not modify

import {List} from "immutable";
import {NetworkRouteRow} from "./network-route-row";
import {NetworkSummary} from "./network-summary";
import {NetworkType} from "../../custom/network-type";
import {TimeInfo} from "../time-info";

export class NetworkRoutesPage {

  constructor(readonly timeInfo: TimeInfo,
              readonly networkType: NetworkType,
              readonly networkSummary: NetworkSummary,
              readonly routes: List<NetworkRouteRow>) {
  }

  public static fromJSON(jsonObject): NetworkRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRoutesPage(
      TimeInfo.fromJSON(jsonObject.timeInfo),
      NetworkType.fromJSON(jsonObject.networkType),
      NetworkSummary.fromJSON(jsonObject.networkSummary),
      jsonObject.routes ? List(jsonObject.routes.map(json => NetworkRouteRow.fromJSON(json))) : List()
    );
  }
}
