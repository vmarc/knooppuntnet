import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LegBuildParams} from "../../../kpn/api/common/planner/leg-build-params";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {PlanLegDetail} from "../../../kpn/api/common/planner/plan-leg-detail";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryImpl implements PlannerLegRepository {

  constructor(private appService: AppService) {
  }

  planLeg(networkType: NetworkType, source: LegEnd, sink: LegEnd): Observable<PlanLegDetail> {
    const params = new LegBuildParams(networkType.name, source, sink);
    return this.appService.leg(params).pipe(
      map(response => response.result)
    );
  }
}
