// this class is generated, please do not modify

import {Bounds} from '../bounds';
import {LocationSummary} from './location-summary';

export class LocationMapPage {

  constructor(readonly summary: LocationSummary,
              readonly bounds: Bounds,
              readonly geoJson: string) {
  }

  public static fromJSON(jsonObject: any): LocationMapPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationMapPage(
      LocationSummary.fromJSON(jsonObject.summary),
      Bounds.fromJSON(jsonObject.bounds),
      jsonObject.geoJson
    );
  }
}
