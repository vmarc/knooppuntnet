import { PoiId } from '@app/components/ol/domain';
import { Coordinate } from 'ol/coordinate';

export class PoiClick {
  constructor(readonly coordinate: Coordinate, readonly poiId: PoiId) {}
}
