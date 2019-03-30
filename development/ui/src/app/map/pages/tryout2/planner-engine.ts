import Coordinate from 'ol/View';

export interface PlannerEngine {
  nodeSelected(nodeId: string, nodeName: string, coordinate: Coordinate): void;
  undo(): void;
  redo(): void;
}
