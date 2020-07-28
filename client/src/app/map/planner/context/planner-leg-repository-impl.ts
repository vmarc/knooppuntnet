import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LegBuildParams} from "../../../kpn/api/common/planner/leg-build-params";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerLegRepository} from "./planner-leg-repository";
import {PlanLegData} from "./plan-leg-data";

export class PlannerLegRepositoryImpl implements PlannerLegRepository {

  constructor(private appService: AppService) {
  }

  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegData> {
    const params = new LegBuildParams(networkType.name, source, sink);
    return this.appService.leg(params).pipe(
      map(response => new PlanLegData(source, sink, response.result.routes))
    );
  }
}
