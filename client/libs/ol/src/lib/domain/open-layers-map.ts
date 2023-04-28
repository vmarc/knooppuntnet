import { PageService } from '@app/components/shared';
import { Subscriptions } from '@app/util';
import Map from 'ol/Map';
import { MapOptions } from 'ol/PluggableMap';
import { fromEvent } from 'rxjs';

export class OpenLayersMap {
  public readonly map: Map;
  private readonly subscriptions = new Subscriptions();

  constructor(mapOptions: MapOptions) {
    this.map = new Map(mapOptions);
  }

  init(pageService: PageService): void {
    this.subscriptions.add(
      pageService.sidebarOpen.subscribe(() => this.updateSize())
    );
    this.subscriptions.add(
      fromEvent(window, 'webkitfullscreenchange').subscribe(() =>
        this.updateSize()
      )
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
