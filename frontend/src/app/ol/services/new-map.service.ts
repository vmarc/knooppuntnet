import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { PageService } from '@app/components/shared';
import { MapOptions } from 'ol/Map';
import { OpenLayersMap } from '../domain';

@Injectable({
  providedIn: 'root',
})
export class NewMapService {
  private readonly pageService = inject(PageService);

  build(mapOptions: MapOptions): OpenLayersMap {
    return new OpenLayersMap(mapOptions, this.pageService);
  }
}
