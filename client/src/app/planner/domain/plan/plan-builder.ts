/*
  Builds a Plan from a single PlanDetail.
 */
import { PlanLegDetail } from '@api/common/planner';
import { List } from 'immutable';
import { FeatureId } from '../features/feature-id';
import { Plan } from './plan';
import { PlanFlag } from './plan-flag';
import { PlanFlagType } from './plan-flag-type';
import { PlanLeg } from './plan-leg';
import { PlanUtil } from './plan-util';

export class PlanBuilder {
  static build(planDetails: PlanLegDetail[], planString: string): Plan {
    const sourceNode = planDetails[0].routes[0].sourceNode;
    const sourceFlag = new PlanFlag(
      PlanFlagType.start,
      FeatureId.next(),
      sourceNode.coordinate
    );

    const legs: List<PlanLeg> = List(planDetails).map((planDetail, index) => {
      const lastLeg = index === planDetails.length - 1;

      const featureId = FeatureId.next();
      const key = PlanUtil.key(planDetail.source, planDetail.sink);

      const sinkFlag = new PlanFlag(
        lastLeg ? PlanFlagType.end : PlanFlagType.via,
        FeatureId.next(),
        planDetail.routes[planDetail.routes.length - 1].sinkNode.coordinate
      );

      const viaFlag = new PlanFlag( // TODO ???
        lastLeg ? PlanFlagType.end : PlanFlagType.via,
        FeatureId.next(),
        planDetail.routes[planDetail.routes.length - 1].sinkNode.coordinate
      );

      return new PlanLeg(
        featureId,
        key,
        planDetail.source,
        planDetail.sink,
        sinkFlag,
        viaFlag,
        List(planDetail.routes)
      );
    });

    return new Plan(sourceNode, sourceFlag, legs);
  }
}
