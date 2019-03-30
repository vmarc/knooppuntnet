import Coordinate from 'ol/View';

export interface PlannerEngine {
  nodeSelected(nodeId: string, nodeName: string, coordinate: Coordinate): void;
  legDragStarted(legId: string, coordinate: Coordinate): boolean;
  legNodeDragStarted(legNodeId: string, nodeId: string, coordinate: Coordinate): boolean;
  dragCancel(): void;
  isDraggingLeg(): boolean;
  isDraggingNode(): boolean;
  endDragLeg(nodeId: string, nodeName: string, coordinate: Coordinate): void;
  endDragNode(nodeId: string, nodeName: string, coordinate: Coordinate): void;
  undo(): void;
  redo(): void;
}
