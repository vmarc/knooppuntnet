import {PlannerContext} from "../../context/planner-context";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";
import {PlannerCommandRemoveRouteViaPoint} from "../../commands/planner-command-remove-route-via-point";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {FeatureId} from "../../features/feature-id";

export class RemoveEndLegRouteViaPoint {

  constructor(private readonly context: PlannerContext) {
  }

  remove(oldLeg: PlanLeg): void {
    this.buildNewLeg(oldLeg).subscribe(newLeg => {
      const command = new PlannerCommandRemoveRouteViaPoint(oldLeg.featureId, newLeg.featureId);
      this.context.execute(command);
    });
  }

  private buildNewLeg(oldLeg: PlanLeg): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+oldLeg.sinkNode.nodeId);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, oldLeg.sinkFlag, null, data.routes);
        this.context.legs.add(newLeg);
        return newLeg;
      })
    );
  }
}
