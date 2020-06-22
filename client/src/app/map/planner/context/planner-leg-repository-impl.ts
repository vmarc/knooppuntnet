import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AppService} from "../../../app.service";
import {LegBuildParams} from "../../../kpn/api/common/planner/leg-build-params";
import {LegEnd} from "../../../kpn/api/common/planner/leg-end";
import {LegEndNode} from "../../../kpn/api/common/planner/leg-end-node";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {PlanLeg} from "../plan/plan-leg";
import {PlanLegBuilder} from "../plan/plan-leg-builder";
import {PlanNode} from "../plan/plan-node";
import {PlannerLegRepository} from "./planner-leg-repository";

export class PlannerLegRepositoryImpl implements PlannerLegRepository {

  constructor(private appService: AppService) {
  }

  planLeg(networkType: NetworkType, legId: string, source: PlanNode, sink: PlanNode): Observable<PlanLeg> {
    const params = new LegBuildParams(
      networkType.name,
      legId,
      new LegEnd(new LegEndNode(+source.nodeId), null),
      new LegEnd(new LegEndNode(+sink.nodeId), null)
    );
    return this.appService.routeLeg(params).pipe(
      map(response => PlanLegBuilder.toPlanLeg(source, sink, response.result))
    );
  }
}
