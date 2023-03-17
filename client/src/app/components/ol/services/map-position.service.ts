import { Injectable } from '@angular/core';
import { actionPlannerPosition } from '@app/planner/store/planner-actions';
import { Store } from '@ngrx/store';
import { Coordinate } from 'ol/coordinate';
import View from 'ol/View';
import { distinct } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapPosition } from '../domain/map-position';

@Injectable()
export class MapPositionService {
  private view: View;
  private currentMapPosition$ = new BehaviorSubject<MapPosition>(null);

  constructor(private store: Store) {
    this.currentMapPosition$
      .pipe(distinct(), debounceTime(50))
      .subscribe((mapPosition) => {
        if (mapPosition) {
          this.store.dispatch(actionPlannerPosition(mapPosition));
        }
      });
  }

  private updatePositionHandler = () => this.updateMapPosition();

  install(view: View): void {
    this.uninstall();
    this.view = view;
    this.view.on('change:resolution', this.updatePositionHandler);
    this.view.on('change:center', this.updatePositionHandler);
  }

  uninstall(): void {
    if (this.view) {
      this.view.un('change:resolution', this.updatePositionHandler);
      this.view.un('change:center', this.updatePositionHandler);
      this.view = null;
    }
  }

  private updateMapPosition(): void {
    const center: Coordinate = this.view.getCenter();
    if (center) {
      const zoom = this.view.getZoom();
      const z = Math.round(zoom);
      const mapPosition = new MapPosition(z, center[0], center[1], 0);
      this.currentMapPosition$.next(mapPosition);
    }
  }
}
