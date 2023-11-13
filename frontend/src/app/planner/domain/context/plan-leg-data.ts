import { PlanRoute } from '@api/common/planner';
import { PlanNode } from '@api/common/planner';
import { LegEnd } from '@api/common/planner';
import { List } from 'immutable';

export class PlanLegData {
  constructor(
    readonly source: LegEnd,
    readonly sink: LegEnd,
    readonly routes: List<PlanRoute>
  ) {}

  get sinkNode(): PlanNode {
    const lastRoute = this.routes.last(null);
    if (lastRoute) {
      return lastRoute.sinkNode;
    }
    return null;
  }
}
