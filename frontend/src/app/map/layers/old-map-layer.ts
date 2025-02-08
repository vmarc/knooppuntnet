import { RouteType } from '@api/common/route-type';
import BaseLayer from 'ol/layer/Base';

export class OldMapLayer {
  static build(id: string, name: string, layer: BaseLayer): OldMapLayer {
    return new OldMapLayer(id, name, -Infinity, Infinity, layer, null);
  }

  constructor(
    public id: string, // e.g. 'osm'
    public name: string, // e.g. 'OpenStreetMap'
    public minZoom: number,
    public maxZoom: number,
    // public mapTile: MapTile,
    public layer: BaseLayer,
    public routeType?: RouteType
    // public mapMode?: MapMode
  ) {}
}
