import { RouteType } from '@api/common/route-type';
import BaseLayer from 'ol/layer/Base';
import { MapMode } from '../services/map-mode';
import { MapTile } from '../services/map-tile';

export class OldMapLayer {
  static build(id: string, name: string, layer: BaseLayer): OldMapLayer {
    return new OldMapLayer(id, name, -Infinity, Infinity, 'vector', layer, null, null);
  }

  constructor(
    public id: string, // e.g. 'osm'
    public name: string, // e.g. 'OpenStreetMap'
    public minZoom: number,
    public maxZoom: number,
    public mapTile: MapTile,
    public layer: BaseLayer,
    public routeType?: RouteType,
    public mapMode?: MapMode
  ) {}
}
