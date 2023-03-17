import { PdfPlanNode } from '@app/pdf/plan/pdf-plan-node';
import { Plan } from '@app/planner/domain/plan/plan';
import { List } from 'immutable';
import { PdfPlan } from './pdf-plan';

export class PdfPlanBuilder {
  static fromPlan(plan: Plan): PdfPlan {
    let cumulativeDistance = 0;

    const nodes: PdfPlanNode[] = plan.legs
      .toArray()
      .flatMap((leg, legIndex) => {
        const lastLeg = legIndex === plan.legs.size - 1;
        return leg.routes.toArray().flatMap((planRoute, routeIndex) => {
          const lastRoute = routeIndex === leg.routes.size - 1;
          const isPlanSourceNode = legIndex === 0 && routeIndex === 0;
          const distance = PdfPlanBuilder.distanceToString(planRoute.meters);
          const cumulDistance = isPlanSourceNode
            ? 'START'
            : PdfPlanBuilder.distanceToString(cumulativeDistance);
          cumulativeDistance += planRoute.meters;

          const colour = planRoute.segments[0].colour;

          const sourcePdfPlanNode = new PdfPlanNode(
            planRoute.sourceNode.nodeName,
            planRoute.sourceNode.nodeLongName,
            distance,
            cumulDistance,
            colour,
            routeIndex === 0
          );

          if (lastLeg && lastRoute) {
            const cumulativeDistanceString =
              PdfPlanBuilder.distanceToString(cumulativeDistance);
            const sinkPdfPlanNode = new PdfPlanNode(
              planRoute.sinkNode.nodeName,
              planRoute.sinkNode.nodeLongName,
              'END',
              cumulativeDistanceString,
              null,
              true
            );
            return [sourcePdfPlanNode, sinkPdfPlanNode];
          }

          return [sourcePdfPlanNode];
        });
      });
    return new PdfPlan(List(nodes));
  }

  public static distanceToString(distance: number): string {
    if (distance >= 1000) {
      const km = Math.round(distance / 100) / 10;
      return parseFloat(km.toFixed(1)) + ' km';
    }
    return `${distance} m`;
  }
}
