import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {LegEnd} from "../../../../kpn/api/common/planner/leg-end";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlannerCommandMoveFirstLegSource} from "../../commands/planner-command-move-first-leg-source";
import {PlannerContext} from "../../context/planner-context";
import {FeatureId} from "../../features/feature-id";
import {PlanFlag} from "../../plan/plan-flag";
import {PlanFlagType} from "../../plan/plan-flag-type";
import {PlanLeg} from "../../plan/plan-leg";
import {PlanUtil} from "../../plan/plan-util";

export class MoveFirstLegSource {

  constructor(private readonly context: PlannerContext) {
  }

  move(newSourceNode: PlanNode): void {

    const oldLeg = this.context.plan.legs.get(0, null);
    const newSource = PlanUtil.legEndNode(+newSourceNode.nodeId);

    this.buildNewLeg(newSource, oldLeg).subscribe(newLeg => {
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

  private buildNewLeg(source: LegEnd, oldLeg: PlanLeg): Observable<PlanLeg> {
    return this.context.fetchLeg(source, oldLeg.sink).pipe(
      map(data => {
        const sinkFlag = new PlanFlag(PlanFlagType.End, FeatureId.next(), data.sinkNode.coordinate);
        return this.context.newLeg(data, sinkFlag, oldLeg.viaFlag);
      })
    );
  }

}
