import {Observable} from "rxjs";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanLegDetail} from "../../../kpn/api/common/planner/plan-leg-detail";
import {NetworkType} from "../../../kpn/api/custom/network-type";

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegDetail>;
}
