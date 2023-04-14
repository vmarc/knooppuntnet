import { LegEnd } from '@api/common/planner';
import { NetworkType } from '@api/custom';
import { Observable } from 'rxjs';
import { PlanLegData } from './plan-leg-data';

export interface PlannerLegRepository {
  planLeg(
    networkType: NetworkType,
    source: LegEnd,
    sink: LegEnd,
    proposed: boolean
  ): Observable<PlanLegData>;
}
