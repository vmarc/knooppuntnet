import {Observable} from "rxjs";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";

export interface PlannerLegRepository {
  planLeg(networkType: NetworkType, legId: string, source: PlanNode, sink: PlanNode): Observable<PlanLeg>;
}
