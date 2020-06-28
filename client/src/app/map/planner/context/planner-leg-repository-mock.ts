import {List} from "immutable";
import {Observable, of} from "rxjs";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {RouteLeg} from "../../../kpn/api/common/planner/route-leg";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanNode} from "../plan/plan-node";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryMock implements PlannerLegRepository {

  private planLegs: List<PlanLeg> = List();

  planLeg(networkType: NetworkType, legId: string, source: LegEnd, sink: LegEnd): Observable<RouteLeg> {

    // const legKey = "";
    //
    // let planLeg = this.planLegs.find(leg => leg.featureId === legId);
    // if (!planLeg) {
    //   const foundLeg = this.planLegs.find(leg => leg.key === legKey);
    //   if (foundLeg) {
    //     planLeg = new PlanLeg(legKey, legId, source, sink, foundLeg.meters, foundLeg.routes);
    //   }
    // }
    // return of(planLeg);
    return of(null);
  }

  add(leg: PlanLeg): void {
    this.planLegs = this.planLegs.push(leg);
  }

}
