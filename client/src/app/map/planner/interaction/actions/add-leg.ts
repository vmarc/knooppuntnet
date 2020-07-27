import {PlannerContext} from "../../context/planner-context";
import {PlanNode} from "../../../../kpn/api/common/planner/plan-node";
import {PlanUtil} from "../../plan/plan-util";
import {PlannerCommandAddLeg} from "../../commands/planner-command-add-leg";
import {PlanLeg} from "../../plan/plan-leg";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {FeatureId} from "../../features/feature-id";

export class AddLeg {

  constructor(private readonly context: PlannerContext) {
  }

  add(sinkNode: PlanNode): void {
    this.buildLeg(sinkNode).subscribe(leg => {
      const command = new PlannerCommandAddLeg(leg.featureId);
      this.context.execute(command);
    });
  }

  private buildLeg(sinkNode: PlanNode): Observable<PlanLeg> {
    const sourceNode = this.context.plan.sinkNode();
    const source = PlanUtil.legEndNode(+sourceNode.nodeId);
    const sink = PlanUtil.legEndNode(+sinkNode.nodeId);
    return this.context.legRepository.planLeg(this.context.networkType, source, sink).pipe(
      map(data => {
        const legKey = PlanUtil.key(source, sink);
        const sinkFlag = PlanUtil.endFlag(data.sinkNode.coordinate);
        const newLeg = new PlanLeg(FeatureId.next(), legKey, source, sink, sinkFlag, null, data.routes);
        this.context.legs.add(newLeg);
        return newLeg;
      })
    );
  }

}
