import { LegEnd } from '@api/common/planner';
import { RouteType } from '@api/common';
import { Observable } from 'rxjs';
import { PlanLegData } from './plan-leg-data';

export interface PlannerLegRepository {
  planLeg(
    routeType: RouteType,
    source: LegEnd,
    sink: LegEnd,
    proposed: boolean
  ): Observable<PlanLegData>;
}
