// this class is generated, please do not modify

import {LocationNode} from './location-node';

export class LocationsPage {

  constructor(readonly locationNode: LocationNode) {
  }

  public static fromJSON(jsonObject: any): LocationsPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationsPage(
      LocationNode.fromJSON(jsonObject.locationNode)
    );
  }
}
