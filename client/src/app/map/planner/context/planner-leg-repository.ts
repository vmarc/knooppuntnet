import {Observable} from "rxjs";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";

export interface PlannerLegRepository {

  planLeg(networkType: string, legId: string, source: PlanNode, sink: PlanNode): Observable<PlanLeg>;

}
