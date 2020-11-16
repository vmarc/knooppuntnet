import {Observable} from 'rxjs';
import {LegEnd} from '../../../kpn/api/common/planner/leg-end';
import {NetworkType} from '../../../kpn/api/custom/network-type';
import {PlanLegData} from './plan-leg-data';

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegData>;
}
