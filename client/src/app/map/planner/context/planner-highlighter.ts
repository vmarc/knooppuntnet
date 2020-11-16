import {PlanNode} from '../../../kpn/api/common/planner/plan-node';
import {RouteFeature} from '../features/route-feature';

export interface PlannerHighlighter {

  highlightNode(node: PlanNode): void;

  highlightRoute(routeFeature: RouteFeature): void;

  reset(): void;
}
