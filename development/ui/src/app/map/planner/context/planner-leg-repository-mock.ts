import {List} from "immutable";
import {Observable, of} from "rxjs";
import {ApiResponse} from "../../../kpn/shared/api-response";
import {RouteLeg} from "../../../kpn/shared/planner/route-leg";

class RouteLegInfo {
  constructor(public readonly leg: RouteLeg,
              public readonly sourceNodeId: string,
              public readonly sinkNodeId: string) {
  }
}

export class PlannerLegRepositoryMock {

  private routeLegs: List<RouteLegInfo> = List();

  routeLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<ApiResponse<RouteLeg>> {
    let routeLeg = this.routeLegs.find(routeLegInfo => routeLegInfo.leg.legId == legId);
    if (!routeLeg) {
      routeLeg = this.routeLegs.find(routeLegInfo => routeLegInfo.sourceNodeId == sourceNodeId && routeLegInfo.sinkNodeId == sinkNodeId);
      if (routeLeg) {
        routeLeg = new RouteLegInfo(new RouteLeg(legId, routeLeg.leg.fragments), sourceNodeId, sourceNodeId);
      }
      else {
        routeLeg = new RouteLegInfo(new RouteLeg(legId, List()), sourceNodeId, sourceNodeId);
      }
    }
    const response = new ApiResponse<RouteLeg>(null, null, routeLeg.leg);
    return of(response);
  }

  add(leg: RouteLeg, sourceNodeId: string, sinkNodeId: string): void {
    this.routeLegs = this.routeLegs.push(new RouteLegInfo(leg, sourceNodeId, sinkNodeId));
  }

}
