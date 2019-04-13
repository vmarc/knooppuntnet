import {List} from "immutable";
import {Observable, of} from "rxjs";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";

export class PlannerLegRepositoryMock {

  private routeLegs: List<RouteLeg> = List();

  routeLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<ApiResponse<RouteLeg>> {
    let routeLeg = this.routeLegs.find(leg => leg.legId == legId);
    if (!routeLeg) {
      routeLeg = new RouteLeg(legId, List());
    }
    const response = new ApiResponse<RouteLeg>(null, null, routeLeg);
    return of(response);
  }

}
