import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Bounds } from '@api/common/bounds';
import { MapService } from '@app/map/map.service';
import { State } from '@app/state/state';

@Injectable({
  providedIn: 'root',
})
export class ExplorePageService {
  private readonly state = inject(State);
  private readonly mapService = inject(MapService);
  readonly routes = this.state.explore.routes;

  onInit(): void {
    this.state.map.updateMode('standard');
    const netherlands: Bounds = { minLat: 50.75, minLon: 3.2, maxLat: 53.7, maxLon: 7.22 };
    this.mapService.fitBounds(netherlands);
    this.mapService.execute(() => {
      //
    });
  }
}
