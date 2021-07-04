// this class is generated, please do not modify

import { List } from 'immutable';
import { LegEnd } from './leg-end';
import { PlanRoute } from './plan-route';

export class PlanLegDetail {
  constructor(
    readonly source: LegEnd,
    readonly sink: LegEnd,
    readonly routes: List<PlanRoute>
  ) {}

  public static fromJSON(jsonObject: any): PlanLegDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanLegDetail(
      LegEnd.fromJSON(jsonObject.source),
      LegEnd.fromJSON(jsonObject.sink),
      jsonObject.routes
        ? List(jsonObject.routes.map((json: any) => PlanRoute.fromJSON(json)))
        : List()
    );
  }
}
