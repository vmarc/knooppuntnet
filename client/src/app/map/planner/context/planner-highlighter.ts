import {PlanNode} from '@api/common/planner/plan-node';
import {Coordinate} from 'ol/coordinate';
import {RouteFeature} from '../features/route-feature';

export interface PlannerHighlighter {

  mouseDown(coordinate: Coordinate): void;

  highlightNode(node: PlanNode): void;

  highlightRoute(routeFeature: RouteFeature): void;

  reset(): void;
}
