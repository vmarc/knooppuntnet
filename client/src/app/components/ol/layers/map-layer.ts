import { NetworkType } from '@api/custom/network-type';
import { MapMode } from '@app/components/ol/services/map-mode';
import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';

export class MapLayer {
  constructor(
    public name: string,
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

  get minZoom(): number {
    return this.layer.getMinZoom();
  }

  get maxZoom(): number {
    return this.layer.getMaxZoom();
  }
}
