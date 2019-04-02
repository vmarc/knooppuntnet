import Coordinate from 'ol/View';
import {List} from "immutable";
import {PlannerMapFeature} from "./features/planner-map-feature";

export interface PlannerEngine {

  handleDownEvent(coordinate: Coordinate, features: List<PlannerMapFeature>): boolean;

  handleMoveEvent(coordinate: Coordinate, features: List<PlannerMapFeature>): boolean;

  handleDragEvent(coordinate: Coordinate, features: List<PlannerMapFeature>): boolean;

  handleUpEvent(coordinate: Coordinate, features: List<PlannerMapFeature>): boolean;

  handleMouseOut();

  handleMouseEnter();

  // ==========

  undo(): void;

  redo(): void;
}
