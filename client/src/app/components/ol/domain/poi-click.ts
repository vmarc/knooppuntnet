import {Coordinate} from 'ol/coordinate';
import {PoiId} from './poi-id';

export class PoiClick {
  constructor(readonly coordinate: Coordinate,
              readonly poiId: PoiId) {
  }
}
