// this class is generated, please do not modify

import { List } from 'immutable';
import { Ref } from '../common';

export class LocationPage {
  constructor(readonly routeRefs: List<Ref>) {}

  static fromJSON(jsonObject: any): LocationPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LocationPage(
      jsonObject.routeRefs
        ? List(jsonObject.routeRefs.map((json: any) => Ref.fromJSON(json)))
        : List()
    );
  }
}
