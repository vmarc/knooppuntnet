import { Coordinate } from '@api/custom/coordinate';
import { PoiId } from '@app/ol/domain/poi-id';

export class PoiClick {
  constructor(
    readonly coordinate: Coordinate,
    readonly poiId: PoiId
  ) {}
}
