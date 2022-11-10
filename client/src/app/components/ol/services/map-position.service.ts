import { Injectable } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Params } from '@angular/router';
import { Router } from '@angular/router';
import { Coordinate } from 'ol/coordinate';
import { toLonLat } from 'ol/proj';
import View from 'ol/View';
import { distinct } from 'rxjs';
import { BehaviorSubject } from 'rxjs';
import { debounceTime } from 'rxjs/operators';
import { MapPosition } from '../domain/map-position';

@Injectable()
export class MapPositionService {
  private view: View;
  private currentMapPosition$ = new BehaviorSubject<MapPosition>(null);
  mapPosition$ = new BehaviorSubject<MapPosition>(null); // debounced

  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    this.currentMapPosition$
      .pipe(distinct(), debounceTime(50))
      .subscribe((mapPosition) => {
        if (mapPosition) {
          this.mapPosition$.next(mapPosition);
          this.updateUrl(mapPosition);
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

  private updateUrl(mapPosition: MapPosition): void {
    const center: Coordinate = [mapPosition.x, mapPosition.y];
    const zoom = mapPosition.zoom;

    const c = toLonLat(center);
    const lng = c[0].toFixed(8);
    const lat = c[1].toFixed(8);
    const z = Math.round(zoom);

    const position = `${lat},${lng},${z}`;
    const queryParams: Params = { position };

    this.router.navigate([], {
      relativeTo: this.activatedRoute,
      queryParams,
      replaceUrl: true, // do not push a new entry to the browser history
      queryParamsHandling: 'merge', // preserve other query params if there are any
    });
  }
}
