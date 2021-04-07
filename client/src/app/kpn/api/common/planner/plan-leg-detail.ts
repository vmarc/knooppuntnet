// this class is generated, please do not modify

import { LegEnd } from './leg-end';
import { PlanRoute } from './plan-route';

export class PlanLegDetail {
  constructor(
    readonly source: LegEnd,
    readonly sink: LegEnd,
    readonly routes: Array<PlanRoute>
  ) {}

  public static fromJSON(jsonObject: any): PlanLegDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new PlanLegDetail(
      LegEnd.fromJSON(jsonObject.source),
      LegEnd.fromJSON(jsonObject.sink),
      jsonObject.routes.map((json: any) => PlanRoute.fromJSON(json))
    );
  }
}
