import { LegEnd } from '@api/common/planner/leg-end';
import { PlanNode } from '@api/common/planner/plan-node';
import { PlanRoute } from '@api/common/planner/plan-route';
import { List } from 'immutable';
import { Util } from '../../../components/shared/util';
import { PlanFlag } from './plan-flag';

export class PlanLeg {
  constructor(
    readonly featureId: string,
    readonly key: string,
    readonly source: LegEnd,
    readonly sink: LegEnd,
    readonly sinkFlag: PlanFlag,
    readonly viaFlag: PlanFlag,
    readonly routes: List<PlanRoute>
  ) {}

  get sourceNode(): PlanNode {
    return this.routes.isEmpty() ? null : this.routes.get(0).sourceNode;
  }

  get sinkNode(): PlanNode {
    const lastRoute = this.routes.last(null);
    return lastRoute === null ? null : lastRoute.sinkNode;
  }

  meters(): number {
    return Util.sum(this.routes.map((route) => route.meters));
  }

  withSinkFlag(sinkFlag: PlanFlag): PlanLeg {
    return new PlanLeg(
      this.featureId,
      this.key,
      this.source,
      this.sink,
      sinkFlag,
      this.viaFlag,
      this.routes
    );
  }
}
