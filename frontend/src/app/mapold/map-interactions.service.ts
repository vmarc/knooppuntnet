import { inject } from '@angular/core';
import { effect } from '@angular/core';
import { Injectable } from '@angular/core';
import { State } from '@app/state/state';
import Map from 'ol/Map';
import { MapSubject } from '../ol/services/map-subject';
import { PlannerMapService } from '../planner/pages/planner/planner-map.service';
import { MapRoutePopupInteractionService } from './popup/map-route-popup-interaction.service';

@Injectable()
export class MapInteractionsService {
  private readonly state = inject(State);
  private readonly exploreInteractionService = inject(MapRoutePopupInteractionService);
  private readonly plannerMapService = inject(PlannerMapService);

  private _map: Map;

  constructor() {
    effect(() => {
      const subject = this.state.map.subject();
      console.log(`map subject: ${subject}`);
      this.updateInteractions(subject);
    });
  }

  init(map: Map): void {
    this._map = map;
    this.updateInteractions(this.state.map.subject());
  }

  private updateInteractions(subject: MapSubject): void {
    if (this._map) {
      if (subject === 'explore') {
        this._map.addInteraction(this.exploreInteractionService.interaction);
        //
      } else {
        this._map.removeInteraction(this.exploreInteractionService.interaction);
      }

      if (subject === 'plan') {
        // this._map.addInteraction(this.plannerMapService.interaction); // compile
      } else {
        // this._map.removeInteraction(this.plannerMapService.interaction); // compile
      }
    }
  }
}
