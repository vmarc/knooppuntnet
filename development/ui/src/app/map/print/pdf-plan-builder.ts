import {List, Range} from "immutable";
import {Plan} from "../planner/plan/plan";
import {PlanLegFragment} from "../planner/plan/plan-leg-fragment";
import {PdfPlan} from "./pdf-plan";
import {PdfPlanNode} from "./pdf-plan-node";

export class PdfPlanBuilder {

  static fromPlan(plan: Plan): PdfPlan {

    const allFragments: List<PlanLegFragment> = plan.legs.flatMap(leg => leg.fragments);
    const allMeters = allFragments.map(fragment => fragment.meters);

    let cumul = 0;
    const cumulativeDistances = allMeters.map(distance => {
      cumul += distance;
      return cumul;
    });

    const nodes = Range(0, allFragments.size + 1).map(nodeIndex => {
      if (nodeIndex == 0) {
        return new PdfPlanNode(plan.source.nodeName, "" + allMeters.get(0) + " m", "START");
      }
      const fragmentIndex = nodeIndex - 1;

      const fragment = allFragments.get(fragmentIndex);
      const nodeName = fragment.sink.nodeName;
      const cumulativeDistance = cumulativeDistances.get(fragmentIndex);
      const km = Math.round(cumulativeDistance / 100) / 10;
      const kmString = parseFloat(km.toFixed(1)) + " km";
      let distance = "END";
      if (fragmentIndex < allFragments.size - 1) {
        distance = "" + allMeters.get(fragmentIndex + 1) + " m";
      }

      return new PdfPlanNode(nodeName, distance, kmString);
    });

    return new PdfPlan(nodes.toList());
  }

}
