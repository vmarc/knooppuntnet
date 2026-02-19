import { Position } from 'geojson';

export interface PlannerElasticBand {
  set(anchor1: Position, anchor2: Position, position: Position): void;

  setInvisible(): void;

  updatePosition(position: Position): void;
}
