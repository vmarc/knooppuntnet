import BaseLayer from 'ol/layer/Base';
import Map from 'ol/Map';

export class MapLayer {
  constructor(
    public name: string,
    public layer: BaseLayer,
    public applyMap?: (map: Map) => void,
    public resizeFunction?: () => void
  ) {}

  updateSize(): void {
    if (this.resizeFunction) {
      this.resizeFunction();
    }
  }
}
