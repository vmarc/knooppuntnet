import {Coordinate} from "ol/coordinate";

export interface PlannerElasticBand {

  set(anchor1: Coordinate, anchor2: Coordinate, position: Coordinate): void;

  setInvisible(): void;

  updatePosition(position: Coordinate): void;
}
