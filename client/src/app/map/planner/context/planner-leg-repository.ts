import {Observable} from "rxjs";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {ViaRoute} from "../plan/via-route";

export interface PlannerLegRepository {

  planLeg(networkType: string, legId: string, source: PlanNode, sink: PlanNode): Observable<PlanLeg>;

  planLegViaRoute(networkType: string, legId: string, source: PlanNode, sink: PlanNode, viaRoute: ViaRoute): Observable<PlanLeg>;

}
