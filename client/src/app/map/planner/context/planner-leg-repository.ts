import {Observable} from "rxjs";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, legId: string, source: PlanNode, sink: PlanNode, viaRoute: ViaRoute): Observable<PlanLeg>;
}
