import {List} from "immutable";
import Coordinate from "ol/coordinate";
import {MapFeature} from "../features/map-feature";

export interface PlannerEngine {

  handleDownEvent(features: List<MapFeature>, coordinate: Coordinate): boolean;

  handleMoveEvent(features: List<MapFeature>, coordinate: Coordinate): boolean;

  handleDragEvent(features: List<MapFeature>, coordinate: Coordinate): boolean;

  handleUpEvent(features: List<MapFeature>, coordinate: Coordinate): boolean;

}
