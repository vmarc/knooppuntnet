import { Coordinate } from 'ol/coordinate';
import { HooverFeature } from './hoover-feature';

export class HooverState {
  constructor(
    readonly routes: Array<HooverFeature>,
    readonly coordinate: Coordinate
  ) {}
}
