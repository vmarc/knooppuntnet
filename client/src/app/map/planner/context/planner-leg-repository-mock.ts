import {List} from "immutable";
import {Observable, of} from "rxjs";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryMock implements PlannerLegRepository {

  private planLegs: List<PlanLeg> = List();

  planLeg(networkType: NetworkType, legId: string, source: PlanNode, sink: PlanNode, viaRoute: ViaRoute): Observable<PlanLeg> {
    let planLeg = this.planLegs.find(leg => leg.featureId === legId);
    if (!planLeg) {
      const foundLeg = this.planLegs.find(leg => leg.source.nodeId === source.nodeId && leg.sink.nodeId === sink.nodeId && this.legViaRoute(leg, viaRoute));
      if (foundLeg) {
        planLeg = new PlanLeg(legId, source, sink, null, foundLeg.meters, foundLeg.routes);
      }
    }
    return of(planLeg);
  }

  add(leg: PlanLeg): void {
    this.planLegs = this.planLegs.push(leg);
  }

  private legViaRoute(leg: PlanLeg, viaRoute: ViaRoute): boolean {
    if (viaRoute === null) {
      return leg.viaRoute === null;
    }
    return leg.viaRoute.routeId === viaRoute.routeId && leg.viaRoute.pathId === viaRoute.pathId;
  }
}
