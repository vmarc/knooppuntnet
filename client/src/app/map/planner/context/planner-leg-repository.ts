import {Observable} from "rxjs";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {RouteLeg} from "../../../kpn/api/common/planner/route-leg";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, legId: string, source: LegEnd, sink: LegEnd): Observable<RouteLeg>;
}
