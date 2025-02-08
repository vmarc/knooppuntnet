import { PoiId } from '@app/ol/domain/poi-id';
import { Coordinate } from 'ol/coordinate';

export class PoiClick {
  constructor(
    readonly coordinate: Coordinate,
    readonly poiId: PoiId
  ) {}
}
