// this class is generated, please do not modify

import { List } from 'immutable';
import { Reference } from '../common/reference';

export class RouteReferences {
  constructor(readonly networkReferences: List<Reference>) {}

  public static fromJSON(jsonObject: any): RouteReferences {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteReferences(
      jsonObject.networkReferences
        ? List(
            jsonObject.networkReferences.map((json: any) =>
              Reference.fromJSON(json)
            )
          )
        : List()
    );
  }
}
