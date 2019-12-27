import Coordinate from "ol/coordinate";

export interface PlannerOverlay {

  setPosition(coordinate: Coordinate): void;

}
