import { Coordinate } from '@api/custom/coordinate';
import { MapRoutePopupRoute } from './map-route-popup-route';

export class MapRoutePopupState {
  constructor(
    readonly routes: Array<MapRoutePopupRoute>,
    readonly coordinate: Coordinate
  ) {}
}
