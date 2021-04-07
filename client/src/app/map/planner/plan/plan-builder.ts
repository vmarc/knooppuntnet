/*
  Builds a Plan from a single PlanDetail.
 */
import { LegEnd } from '@api/common/planner/leg-end';
import { LegEndNode } from '@api/common/planner/leg-end-node';
import { PlanLegDetail } from '@api/common/planner/plan-leg-detail';
import { List } from 'immutable';
import { FeatureId } from '../features/feature-id';
import { Plan } from './plan';
import { PlanFlag } from './plan-flag';
import { PlanFlagType } from './plan-flag-type';
import { PlanLeg } from './plan-leg';

export class PlanBuilder {
  static build(planDetail: PlanLegDetail, planString: string): Plan {
    const planLegFeatureId = FeatureId.next();
    const sourceNode = planDetail.routes[0].sourceNode;
    const sinkNode = planDetail.routes[planDetail.routes.length - 1].sinkNode;
    const source = new LegEnd(new LegEndNode(+sourceNode.nodeId), null);
    const sourceFlag = new PlanFlag(
      PlanFlagType.start,
      FeatureId.next(),
      sourceNode.coordinate
    );
    const sink = new LegEnd(new LegEndNode(+sinkNode.nodeId), null);
    const sinkFlag = new PlanFlag(
      PlanFlagType.end,
      FeatureId.next(),
      sinkNode.coordinate
    );
    const viaFlag: PlanFlag = null;
    const planLeg = new PlanLeg(
      planLegFeatureId,
      planString,
      source,
      sink,
      sinkFlag,
      viaFlag,
      List(planDetail.routes)
    );
    return new Plan(sourceNode, sourceFlag, List([planLeg]));
  }
}
