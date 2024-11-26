import { OldMapLayer } from './old-map-layer';

export class MapLayerChange {
  constructor(
    public oldLayer: OldMapLayer,
    public newLayer: OldMapLayer
  ) {}
}
