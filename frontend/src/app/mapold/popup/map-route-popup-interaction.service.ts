import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { State } from '@app/state/state';
import { MapRoutePopupInteraction } from './map-route-popup-interaction';

@Injectable()
export class MapRoutePopupInteractionService {
  private readonly state = inject(State);
  readonly interaction = new MapRoutePopupInteraction(this.state);
}
