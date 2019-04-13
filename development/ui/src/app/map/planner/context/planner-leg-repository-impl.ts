import {Observable} from "rxjs";
import {AppService} from "../../../app.service";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";

export class PlannerLegRepositoryImpl {

  constructor(private appService: AppService) {
  }

  routeLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<ApiResponse<RouteLeg>> {
    return this.appService.routeLeg(networkType, legId, sourceNodeId, sinkNodeId);
  }

}
