// this class is generated, please do not modify

import {LocationChange} from "./location-change";

export class LocationChangeDoc {

  constructor(readonly _id: string,
              readonly locationChange: LocationChange,
              readonly _rev: string) {
  }

  public static fromJSON(jsonObject: any): LocationChangeDoc {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationChangeDoc(
      jsonObject._id,
      LocationChange.fromJSON(jsonObject.locationChange),
      jsonObject._rev
    );
  }
}
