import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {PlanLeg} from "../plan/plan-leg";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryImpl implements PlannerLegRepository {

  constructor(private appService: AppService) {
  }

  planLeg(networkType: string, legId: string, sourceNodeId: string, sinkNodeId: string): Observable<PlanLeg> {
    return this.appService.routeLeg(networkType, legId, sourceNodeId, sinkNodeId).pipe(map(response => {
      return PlanLeg.fromRouteLeg(response.result);
    }));
  }

}
