import {List} from "immutable";
import {Observable, of} from "rxjs";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryMock implements PlannerLegRepository {

  private planLegs: List<PlanLeg> = List();

  planLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<PlanLeg> {
    let planLeg = this.planLegs.find(leg => leg.featureId == legId);
    if (!planLeg) {
      planLeg = this.planLegs.find(leg => leg.source.nodeId == sourceNodeId && leg.sink.nodeId == sinkNodeId);
    }
    return of(planLeg);
  }

  add(leg: PlanLeg): void {
    this.planLegs = this.planLegs.push(leg);
  }

}
