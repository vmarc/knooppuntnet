import {combineLatest} from "rxjs";
import {Observable} from "rxjs";
import {tap} from "rxjs/operators";
import {map} from "rxjs/operators";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlannerCommandMoveRouteViaPoint} from "../../commands/planner-command-move-route-via-point";
import {PlannerContext} from "../../context/planner-context";
import {FeatureId} from "../../features/feature-id";
import {PlanFlag} from "../../plan/plan-flag";
import {PlanFlagType} from "../../plan/plan-flag-type";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";

export class MoveRouteViaPointToNode {

  constructor(private readonly context: PlannerContext) {
  }

  move(viaNode: PlanNode, viaLegIndex: number): void {

    const oldLeg = this.context.plan.legs.get(viaLegIndex);
    const newLeg1$ = this.buildNewLeg1(oldLeg.sourceNode, viaNode);
    const newLeg2$ = this.buildNewLeg2(viaNode, oldLeg.sinkNode, oldLeg.sinkFlag);

    combineLatest([newLeg1$, newLeg2$]).pipe(
      tap(([newLeg1, newLeg2]) => {
        const command = new PlannerCommandMoveRouteViaPoint(
          oldLeg.featureId,
          newLeg1.featureId,
          newLeg2.featureId
        );
        this.context.execute(command);
      })
    ).subscribe();
  }

  private buildNewLeg1(sourceNode: PlanNode, sinkNode: PlanNode): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(planLegDetail => {
        if (planLegDetail) {
          const lastRoute = planLegDetail.routes.last(null);
          if (lastRoute) {
            const legKey = PlanUtil.key(source, sink);
            const sinkFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), sinkNode.coordinate);
            const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, planLegDetail.routes);
            this.context.legs.add(newLeg);
            return newLeg;
          }
        }
        return null;
      })
    );
  }

  private buildNewLeg2(sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag): Observable<PlanLeg> {
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
