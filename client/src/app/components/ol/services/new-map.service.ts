import { OpenLayersMap } from '@app/components/ol/domain/open-layers-map';
import { Injectable } from '@angular/core';
import { PageService } from '@app/components/shared/page.service';
import { MapOptions } from 'ol/PluggableMap';

@Injectable()
export class NewMapService {
  constructor(private pageService: PageService) {}

  build(mapOptions: MapOptions): OpenLayersMap {
    const openLayersMap = new OpenLayersMap(mapOptions);
    openLayersMap.init(this.pageService);
    return openLayersMap;
  }
}
