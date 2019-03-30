import Coordinate from 'ol/View';

export interface PlannerEngine {
  nodeSelected(nodeId: string, nodeName: string, coordinate: Coordinate): void;
  legDragStarted(legId: string, coordinate: Coordinate): boolean;
  legNodeDragStarted(legNodeId: string, nodeId: string, coordinate: Coordinate): boolean;
  undo(): void;
  redo(): void;
}
