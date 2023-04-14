import { PlanNode } from '@api/common/planner';
import { Coordinate } from 'ol/coordinate';
import { RouteFeature } from '../features/route-feature';
import { PlannerHighlighter } from './planner-highlighter';

export class PlannerHighlighterMock implements PlannerHighlighter {
  node: PlanNode = null;
  routeFeature: RouteFeature = null;

  mouseDown(coordinate: Coordinate): void {}

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
