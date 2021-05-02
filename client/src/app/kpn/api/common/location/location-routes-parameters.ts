// this class is generated, please do not modify

import { LocationRoutesType } from '../../custom/location-routes-type';

export class LocationRoutesParameters {
  constructor(
    readonly locationRoutesType: LocationRoutesType,
    readonly itemsPerPage: number,
    readonly pageIndex: number
  ) {}

  static fromJSON(jsonObject: any): LocationRoutesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationRoutesParameters(
      jsonObject.locationRoutesType,
      jsonObject.itemsPerPage,
      jsonObject.pageIndex
    );
  }
}
