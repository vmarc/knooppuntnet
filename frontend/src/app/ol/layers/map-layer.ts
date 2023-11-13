import { NetworkType } from '@api/custom';
import BaseLayer from 'ol/layer/Base';
import { MapMode } from '../services';

export class MapLayer {
  static simpleLayer(name: string, layer: BaseLayer): MapLayer {
    return new MapLayer(name, name, -Infinity, Infinity, layer, null, null);
  }

  constructor(
    public name: string, // e.g. 'hiking'
    public id: string, // e.g. 'hiking-surface-bitmap'
    public minZoom: number,
    public maxZoom: number,
    public layer: BaseLayer,
    public networkType?: NetworkType,
    public mapMode?: MapMode
  ) {}
}
