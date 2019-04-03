import Coordinate from 'ol/coordinate';
import {List} from "immutable";
import {PlannerMapFeature} from "./features/planner-map-feature";

export interface PlannerEngine {

  handleDownEvent(features: List<PlannerMapFeature>, coordinate: Coordinate): boolean;

  handleMoveEvent(features: List<PlannerMapFeature>, coordinate: Coordinate): boolean;

  handleDragEvent(features: List<PlannerMapFeature>, coordinate: Coordinate): boolean;

  handleUpEvent(features: List<PlannerMapFeature>, coordinate: Coordinate): boolean;

  handleMouseOut();

  handleMouseEnter();

}
