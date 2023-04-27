// this file is generated, please do not modify

import { LegEnd } from './leg-end';
import { PlanRoute } from './plan-route';

export interface PlanLegDetail {
  readonly source: LegEnd;
  readonly sink: LegEnd;
  readonly routes: PlanRoute[];
}
