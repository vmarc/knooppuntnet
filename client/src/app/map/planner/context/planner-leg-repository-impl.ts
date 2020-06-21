import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LegBuildParams} from "../../../kpn/api/common/planner/leg-build-params";
import {ViaRoute} from "../../../kpn/api/common/planner/via-route";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegBuilder} from "../plan/plan-leg-builder";
import {PlanNode} from "../plan/plan-node";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryImpl implements PlannerLegRepository {

  constructor(private appService: AppService) {
  }

  planLeg(networkType: NetworkType, legId: string, source: PlanNode, sink: PlanNode, viaRoute: ViaRoute): Observable<PlanLeg> {
    const params = new LegBuildParams(
      networkType.name,
      legId,
      +source.nodeId,
      +sink.nodeId,
      viaRoute
    );
    return this.appService.routeLeg(params).pipe(
      map(response => PlanLegBuilder.toPlanLeg(source, sink, response.result, viaRoute))
    );
  }
}
