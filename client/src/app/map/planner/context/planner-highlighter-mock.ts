import {PlanNode} from '../../../kpn/api/common/planner/plan-node';
import {RouteFeature} from '../features/route-feature';
import {PlannerHighlighter} from './planner-highlighter';

export class PlannerHighlighterMock implements PlannerHighlighter {

  node: PlanNode = null;
  routeFeature: RouteFeature = null;

  highlightNode(node: PlanNode): void {
    this.node = node;
  }

  highlightRoute(routeFeature: RouteFeature): void {
    this.routeFeature = routeFeature;
  }

  reset(): void {
    this.node = null;
    this.routeFeature = null;
  }

}
