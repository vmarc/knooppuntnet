import {Observable} from "rxjs";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";

export interface PlannerLegRepository {

  routeLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<ApiResponse<RouteLeg>>;

}
