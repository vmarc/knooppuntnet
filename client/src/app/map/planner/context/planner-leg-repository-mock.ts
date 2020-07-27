import {Map} from "immutable";
import {Observable, of} from "rxjs";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanLegDetail} from "../../../kpn/api/common/planner/plan-leg-detail";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerLegRepository} from "./planner-leg-repository";
import {PlanUtil} from "../plan/plan-util";
import {PlanLegData} from "./plan-leg-data";

export class PlannerLegRepositoryMock implements PlannerLegRepository {

  private planLegDetails: Map<string, PlanLegData> = Map();

  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegData> {
    const key = PlanUtil.key(source, sink);
    return of(this.planLegDetails.get(key));
  }

  add(source: LegEnd, sink: LegEnd, planLegData: PlanLegData): void {
    const key = PlanUtil.key(source, sink);
    this.planLegDetails = this.planLegDetails.set(key, planLegData);
  }

}
