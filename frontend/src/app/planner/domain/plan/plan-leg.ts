import { LegEnd } from '@api/common/planner/leg-end';
import { PlanNode } from '@api/common/planner/plan-node';
import { PlanRoute } from '@api/common/planner/plan-route';
import { Util } from '@app/shared/components/util';
import { PlanFlag } from './plan-flag';

export class PlanLeg {
  constructor(
    readonly featureId: string,
    readonly key: string,
    readonly source: LegEnd,
    readonly sink: LegEnd,
    readonly sinkFlag: PlanFlag,
    readonly viaFlag: PlanFlag,
    readonly routes: ReadonlyArray<PlanRoute>
  ) {}

  get sourceNode(): PlanNode | undefined {
    return this.routes.length == 0 ? undefined : this.routes[0].sourceNode;
  }

  get sinkNode(): PlanNode | undefined {
    const lastRoute = this.routes.at(-1);
    return lastRoute?.sinkNode;
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
