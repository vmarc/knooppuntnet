// this class is generated, please do not modify

import {List} from 'immutable';
import {TilePoiGroup} from './tile-poi-group';

export class TilePoiConfiguration {
  readonly groups: List<TilePoiGroup>;

  constructor(groups: List<TilePoiGroup>) {
    this.groups = groups;
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
