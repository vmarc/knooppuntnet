import {Observable} from "rxjs";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanLeg} from "../../../kpn/api/common/planner/plan-leg";
import {RouteLeg} from "../../../kpn/api/common/planner/route-leg";
import {NetworkType} from "../../../kpn/api/custom/network-type";

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, legId: string, source: LegEnd, sink: LegEnd): Observable<PlanLeg>;
}
