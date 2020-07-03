// this class is generated, please do not modify

import {List} from "immutable";
import {PlanRoute} from "./plan-route";

export class PlanLegDetail {

  constructor(readonly routes: List<PlanRoute>) {
  }

  public static fromJSON(jsonObject: any): PlanLegDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanLegDetail(
      jsonObject.routes ? List(jsonObject.routes.map((json: any) => PlanRoute.fromJSON(json))) : List()
    );
  }
}
