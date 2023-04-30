import { Injectable } from '@angular/core';
import { PageService } from '@app/components/shared';
import { MapOptions } from 'ol/Map';
import { OpenLayersMap } from '../domain';

@Injectable()
export class NewMapService {
  constructor(private pageService: PageService) {}

  build(mapOptions: MapOptions): OpenLayersMap {
    const openLayersMap = new OpenLayersMap(mapOptions);
    openLayersMap.init(this.pageService);
    return openLayersMap;
  }
}
