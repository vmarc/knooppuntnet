// this class is generated, please do not modify

import { LocationNodesType } from '../../custom/location-nodes-type';

export class LocationNodesParameters {
  constructor(
    readonly locationNodesType: LocationNodesType,
    readonly itemsPerPage: number,
    readonly pageIndex: number
  ) {}

  static fromJSON(jsonObject: any): LocationNodesParameters {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationNodesParameters(
      jsonObject.locationNodesType,
      jsonObject.itemsPerPage,
      jsonObject.pageIndex
    );
  }
}
