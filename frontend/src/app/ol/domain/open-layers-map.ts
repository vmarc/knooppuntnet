import { effect } from '@angular/core';
import { PageService } from '@app/components/shared';
import { Subscriptions } from '@app/util';
import { MapOptions } from 'ol/Map';
import Map from 'ol/Map';
import { fromEvent } from 'rxjs';

export class OpenLayersMap {
  public readonly map: Map;
  private readonly subscriptions = new Subscriptions();

  constructor(mapOptions: MapOptions, pageService: PageService) {
    this.map = new Map(mapOptions);
    effect(() => {
      pageService.sidebarOpen();
      this.updateSize();
    });
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() => this.updateSize())
    );
  }

  destroy(): void {
    this.subscriptions.unsubscribe();
    this.map.dispose();
    this.map.setTarget(null);
  }

  private updateSize(): void {
    if (this.map) {
      setTimeout(() => {
        this.map.updateSize();
      }, 0);
    }
  }
}
