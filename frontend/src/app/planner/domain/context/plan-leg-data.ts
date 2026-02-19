import { PlanRoute } from '@api/common/planner/plan-route';
import { PlanNode } from '@api/common/planner/plan-node';
import { LegEnd } from '@api/common/planner/leg-end';

export class PlanLegData {
  constructor(
    readonly source: LegEnd,
    readonly sink: LegEnd,
    readonly routes: ReadonlyArray<PlanRoute>
  ) {}

  get sinkNode(): PlanNode {
    const lastRoute = this.routes.at(-1);
    if (lastRoute) {
      return lastRoute.sinkNode;
    }
    return null;
  }
}
