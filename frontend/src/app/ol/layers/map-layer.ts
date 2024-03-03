import { NetworkType } from '@api/custom';
import BaseLayer from 'ol/layer/Base';
import { MapMode } from '../services';
import { MapTile } from '../services/map-tile';

export class MapLayer {
  static build(id: string, name: string, layer: BaseLayer): MapLayer {
    return new MapLayer(id, name, -Infinity, Infinity, 'vector', layer, null, null);
  }

  constructor(
    public id: string, // e.g. 'osm'
    public name: string, // e.g. 'OpenStreetMap'
    public minZoom: number,
    public maxZoom: number,
    public mapTile: MapTile,
    public layer: BaseLayer,
    public networkType?: NetworkType,
    public mapMode?: MapMode
  ) {}
}
