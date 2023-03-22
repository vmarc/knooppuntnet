import { NetworkType } from '@api/custom/network-type';
import { MapMode } from '@app/components/ol/services/map-mode';
import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';

export class MapLayer {
  static simpleLayer(name: string, layer: BaseLayer): MapLayer {
    return new MapLayer(
      name,
      name,
      -Infinity,
      Infinity,
      layer,
      null,
      null,
      null,
      null
    );
  }

  constructor(
    public name: string,
    public id: string,
    public minZoom: number,
    public maxZoom: number,
    public layer: BaseLayer,
    public networkType?: NetworkType,
    public mapMode?: MapMode,
    public applyMap?: (map: Map) => void,
    public resizeFunction?: () => void
  ) {}

  updateSize(): void {
    if (this.resizeFunction) {
      this.resizeFunction();
    }
  }
}
