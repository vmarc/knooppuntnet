import {List} from "immutable";
import {Observable, of} from "rxjs";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryMock implements PlannerLegRepository {

  private planLegs: List<PlanLeg> = List();

  planLeg(networkType: string, legId: string, source: PlanNode, sink: PlanNode): Observable<PlanLeg> {
    let planLeg = this.planLegs.find(leg => leg.featureId === legId);
    if (!planLeg) {
      const foundLeg = this.planLegs.find(leg => leg.source.nodeId === source.nodeId && leg.sink.nodeId === sink.nodeId);
      if (foundLeg) {
        planLeg = new PlanLeg(legId, source, sink, foundLeg.meters, foundLeg.routes);
      }
    }
    return of(planLeg);
  }

  add(leg: PlanLeg): void {
    this.planLegs = this.planLegs.push(leg);
  }

}
