import {List, Range} from "immutable";
import {Plan} from "../../map/planner/plan/plan";
import {PlanRoute} from "../../map/planner/plan/plan-route";
import {PdfPlan} from "./pdf-plan";
import {PdfPlanNode} from "./pdf-plan-node";

export class PdfPlanBuilder {

  static fromPlan(plan: Plan): PdfPlan {

    const allRoutes: List<PlanRoute> = plan.legs.flatMap(leg => leg.routes);
    const allMeters = allRoutes.map(route => route.meters);

    let cumul = 0;
    const cumulativeDistances = allMeters.map(distance => {
      cumul += distance;
      return cumul;
    });

    const nodes = Range(0, allRoutes.size + 1).map(nodeIndex => {
      if (nodeIndex == 0) {
        return new PdfPlanNode(plan.source.nodeName, "" + allMeters.get(0) + " m", "START");
      }
      const routeIndex = nodeIndex - 1;

      const route = allRoutes.get(routeIndex);
      const nodeName = route.sink.nodeName;
      const cumulativeDistance = cumulativeDistances.get(routeIndex);
      const km = Math.round(cumulativeDistance / 100) / 10;
      const kmString = parseFloat(km.toFixed(1)) + " km";
      let distance = "END";
      if (routeIndex < allRoutes.size - 1) {
        distance = "" + allMeters.get(routeIndex + 1) + " m";
      }

      return new PdfPlanNode(nodeName, distance, kmString);
    });

    return new PdfPlan(nodes.toList());
  }

}
