import {PlannerContext} from "../../context/planner-context";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";
import {PlannerCommandRemoveRouteViaPoint} from "../../commands/planner-command-remove-route-via-point";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";

export class RemoveEndLegRouteViaPoint {

  constructor(private readonly context: PlannerContext) {
  }

  remove(oldLeg: PlanLeg): void {
    this.buildNewLeg(oldLeg).pipe(
      map(newLeg => new PlannerCommandRemoveRouteViaPoint(oldLeg, newLeg))
    ).subscribe(command => this.context.execute(command));
  }

  private buildNewLeg(oldLeg: PlanLeg): Observable<PlanLeg> {

    const source = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+oldLeg.sinkNode.nodeId);

    return this.context.fetchLeg(source, sink).pipe(
      map(data => this.context.newLeg(data, oldLeg.sinkFlag, null))
    );
  }
}
