import { OldOldMapLayer } from './old-old-map-layer';

export class MapLayerChange {
  constructor(
    public oldLayer: OldOldMapLayer,
    public newLayer: OldOldMapLayer
  ) {}
}
