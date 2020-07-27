import {combineLatest, Observable} from "rxjs";
import {map, tap} from "rxjs/operators";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlannerCommandMoveViaPoint} from "../../commands/planner-command-move-via-point";
import {PlannerContext} from "../../context/planner-context";
import {FeatureId} from "../../features/feature-id";
import {PlanFlag} from "../../plan/plan-flag";
import {PlanFlagType} from "../../plan/plan-flag-type";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";

export class MoveNodeViaPointToNode {

  constructor(private readonly context: PlannerContext) {
  }

  move(viaNode: PlanNode, legIndex1: number): void {

    const legs = this.context.plan.legs;

    const oldLeg1 = legs.get(legIndex1);
    const oldLeg2 = legs.get(legIndex1 + 1);

    const newLeg1$ = this.buildNewLeg1(oldLeg1.sourceNode, viaNode);
    const newLeg2$ = this.buildNewLeg2(viaNode, oldLeg2.sinkNode, oldLeg2.sinkFlag);

    combineLatest([newLeg1$, newLeg2$]).pipe(
      tap(([newLeg1, newLeg2]) => {
        const command = new PlannerCommandMoveViaPoint(
          oldLeg1.featureId,
          oldLeg2.featureId,
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
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const sinkFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), sinkNode.coordinate);
        const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, data.routes);
        this.context.legs.add(newLeg);
        return newLeg;
      })
    );
  }

  private buildNewLeg2(sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, data.routes);
        this.context.legs.add(newLeg);
        return newLeg;
      })
    );
  }

}
