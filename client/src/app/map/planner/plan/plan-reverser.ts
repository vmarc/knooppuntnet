import {PlannerCommand} from "../commands/planner-command";
import {PlannerCommandReverse} from "../commands/planner-command-reverse";
import {PlannerContext} from "../context/planner-context";
import {FeatureId} from "../features/feature-id";
import {Plan} from "./plan";
import {PlanFlag} from "./plan-flag";
import {PlanFlagType} from "./plan-flag-type";

export class PlanReverser {

  constructor(private readonly context: PlannerContext) {
  }

  reverse(oldPlan: Plan): PlannerCommand {

    const newLegs = oldPlan.legs.reverse().map(oldLeg => {
      const isOldStartLeg = oldLeg.sourceNode.featureId === oldPlan.sourceNode.featureId;
      const planFlagType = isOldStartLeg ? PlanFlagType.End : PlanFlagType.Via;
      return this.context.buildLeg(
        oldLeg.sink,
        oldLeg.source,
        oldLeg.sinkNode,
        oldLeg.sourceNode,
        planFlagType
      );
    });
    const sourceNode = oldPlan.sinkNode();
    const sourceFlag = PlanFlag.start(FeatureId.next(), sourceNode.coordinate);
    const newPlan = new Plan(sourceNode, sourceFlag, newLegs);
    return new PlannerCommandReverse(oldPlan, newPlan);
  }

}
