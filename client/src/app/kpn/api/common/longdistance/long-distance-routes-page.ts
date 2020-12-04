// this class is generated, please do not modify

import {List} from 'immutable';
import {LongDistanceRouteDetail} from './long-distance-route-detail';

export class LongDistanceRoutesPage {

  constructor(readonly details: List<LongDistanceRouteDetail>) {
  }

  public static fromJSON(jsonObject: any): LongDistanceRoutesPage {
    if (!jsonObject) {
      return undefined;
    }
    return new LongDistanceRoutesPage(
      jsonObject.details ? List(jsonObject.details.map((json: any) => LongDistanceRouteDetail.fromJSON(json))) : List()
    );
  }
}
