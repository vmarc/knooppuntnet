import {PlanUtil} from "../../plan/plan-util";
import {PlannerCommandRemoveViaPoint} from "../../commands/planner-command-remove-via-point";
import {PlannerContext} from "../../context/planner-context";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PlanFlag} from "../../plan/plan-flag";
import {FeatureId} from "../../features/feature-id";

export class RemoveViaLegRouteViaPoint {

  constructor(private readonly context: PlannerContext) {
  }

  remove(oldLeg1: PlanLeg): void {

    const oldLeg2 = this.context.plan.legs.find(leg => {
      return leg.sourceNode.nodeId === oldLeg1.sinkNode.nodeId;
    });

    if (oldLeg2 != null) {
      this.buildNewLeg(oldLeg1.sourceNode, oldLeg2.sinkNode, oldLeg2.sinkFlag).subscribe(newLeg => {
        const command = new PlannerCommandRemoveViaPoint(
          oldLeg1.featureId,
          oldLeg2.featureId,
          newLeg.featureId
        );
        this.context.execute(command);
      });
    }
  }

  private buildNewLeg(sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);

    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(planLegDetail => {
        if (planLegDetail) {
          const lastRoute = planLegDetail.routes.last(null);
          if (lastRoute) {
            const legKey = PlanUtil.key(source, sink);
            const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, planLegDetail.routes);
            this.context.legs.add(newLeg);
            return newLeg;
          }
        }
        return null;
      })
    );
  }
}
