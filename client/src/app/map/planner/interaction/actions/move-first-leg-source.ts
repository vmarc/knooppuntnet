import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlannerCommandMoveFirstLegSource} from "../../commands/planner-command-move-first-leg-source";
import {PlannerContext} from "../../context/planner-context";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";

export class MoveFirstLegSource {

  constructor(private readonly context: PlannerContext) {
  }

  move(newSourceNode: PlanNode): void {
    const oldLeg = this.context.plan.legs.first(null);
    if (oldLeg) {
      this.buildNewLeg(newSourceNode, oldLeg).subscribe(newLeg => {
        if (newLeg) {
          const oldSourceNode = this.context.plan.sourceNode;
          const oldSourceFlag = this.context.plan.sourceFlag;
          const newSourceFlag = oldSourceFlag.withCoordinate(newSourceNode.coordinate);
          const command = new PlannerCommandMoveFirstLegSource(
            oldLeg,
            oldSourceNode,
            oldSourceFlag,
            newLeg,
            newSourceNode,
            newSourceFlag
          );
          this.context.execute(command);
        }
      });
    }
  }

  private buildNewLeg(newSourceNode: PlanNode, oldLeg: PlanLeg): Observable<PlanLeg> {
    const source = PlanUtil.legEndNode(+newSourceNode.nodeId);
    return this.context.fetchLeg(source, oldLeg.sink).pipe(
      map(data => PlanUtil.leg(data, oldLeg.sinkFlag, oldLeg.viaFlag))
    );
  }

}
