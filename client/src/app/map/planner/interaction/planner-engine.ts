import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {MapFeature} from "../features/map-feature";

export interface PlannerEngine {

  handleDownEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean;

  handleMoveEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean;

  handleDragEvent(features: List<MapFeature>, coordinate: Coordinate, modifierKeyOnly: boolean): boolean;

  handleUpEvent(features: List<MapFeature>, coordinate: Coordinate, singleClick: boolean, modifierKeyOnly: boolean): boolean;

}
