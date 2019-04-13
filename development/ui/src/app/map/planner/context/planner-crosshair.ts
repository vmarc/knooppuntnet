import Coordinate from "ol/coordinate";

export interface PlannerCrosshair {

  setVisible(visible: boolean): void;

  updatePosition(position: Coordinate): void;
}
