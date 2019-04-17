import {Observable} from "rxjs";
import {PlanLeg} from "../plan/plan-leg";

export interface PlannerLegRepository {

  planLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<PlanLeg>;

}
