import {Observable} from 'rxjs';
import {LegEnd} from '@api/common/planner/leg-end';
import {NetworkType} from '@api/custom/network-type';
import {PlanLegData} from './plan-leg-data';

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegData>;
}
