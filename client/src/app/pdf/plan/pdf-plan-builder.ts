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
      if (nodeIndex === 0) {
        return new PdfPlanNode(plan.sourceNode.nodeName, PdfPlanBuilder.distanceToString(allMeters.get(0)), "START");
      }
      const routeIndex = nodeIndex - 1;

      const route = allRoutes.get(routeIndex);
      const nodeName = route.sinkNode.nodeName;
      const cumulativeDistance = PdfPlanBuilder.distanceToString(cumulativeDistances.get(routeIndex));
      let distance = "END";
      if (routeIndex < allRoutes.size - 1) {
        distance = PdfPlanBuilder.distanceToString(allMeters.get(routeIndex + 1));
      }

      return new PdfPlanNode(nodeName, distance, cumulativeDistance);
    });

    return new PdfPlan(nodes.toList());
  }

  private static distanceToString(distance: number): string {
    if (distance >= 1000) {
      const km = Math.round(distance / 100) / 10;
      return parseFloat(km.toFixed(1)) + " km";
    }
    return `${distance} m`;
  }

}
