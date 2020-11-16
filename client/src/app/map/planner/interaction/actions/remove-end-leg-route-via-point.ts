import {PlannerContext} from '../../context/planner-context';
import {PlanLeg} from '../../plan/plan-leg';
import {PlanUtil} from '../../plan/plan-util';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {PlannerCommandReplaceLeg} from '../../commands/planner-command-replace-leg';

export class RemoveEndLegRouteViaPoint {

  constructor(private readonly context: PlannerContext) {
  }

  remove(oldLeg: PlanLeg): void {
    this.buildNewLeg(oldLeg).pipe(
      map(newLeg => new PlannerCommandReplaceLeg(oldLeg, newLeg))
    ).subscribe(
      command => this.context.execute(command),
      error => this.context.errorDialog(error)
    );
  }

  private buildNewLeg(oldLeg: PlanLeg): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+oldLeg.sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+oldLeg.sinkNode.nodeId);
    return this.context.fetchLeg(source, sink).pipe(
      map(data => PlanUtil.leg(data, oldLeg.sinkFlag, null))
    );
  }
}
