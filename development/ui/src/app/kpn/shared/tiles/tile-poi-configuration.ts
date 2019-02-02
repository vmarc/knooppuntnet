// this class is generated, please do not modify

import {List} from 'immutable';
import {TilePoiGroup} from './tile-poi-group';

export class TilePoiConfiguration {

  constructor(readonly groups: List<TilePoiGroup>) {
  }

  public static fromJSON(jsonObject): TilePoiConfiguration {
    if (!jsonObject) {
      return undefined;
    }
    return new TilePoiConfiguration(
      jsonObject.groups ? List(jsonObject.groups.map(json => TilePoiGroup.fromJSON(json))) : List()
    );
  }
}
