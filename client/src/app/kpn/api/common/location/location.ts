// this class is generated, please do not modify

import {List} from 'immutable';

export class Location {

  constructor(readonly names: List<string>) {
  }

  public static fromJSON(jsonObject: any): Location {
    if (!jsonObject) {
      return undefined;
    }
    return new Location(
      jsonObject.names ? List(jsonObject.names) : List()
    );
  }
}
