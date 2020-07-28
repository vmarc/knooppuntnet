import {combineLatest, Observable} from "rxjs";
import {map} from "rxjs/operators";
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
      map(([newLeg1, newLeg2]) => new PlannerCommandMoveViaPoint(oldLeg1, oldLeg2, newLeg1, newLeg2))
    ).subscribe(command => this.context.execute(command));
  }

  private buildNewLeg1(sourceNode: PlanNode, sinkNode: PlanNode): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    const sinkFlag = new PlanFlag(PlanFlagType.Via, FeatureId.next(), sinkNode.coordinate);
    return this.context.fetchLeg(source, sink).pipe(
      map(data => this.context.newLeg(data, sinkFlag, null))
    );
  }

  private buildNewLeg2(sourceNode: PlanNode, sinkNode: PlanNode, sinkFlag: PlanFlag): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    return this.context.fetchLeg(source, sink).pipe(
      map(data => this.context.newLeg(data, sinkFlag, null))
    );
  }

}
