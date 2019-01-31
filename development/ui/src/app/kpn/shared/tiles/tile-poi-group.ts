// this class is generated, please do not modify

import {List} from 'immutable';
import {TilePoiDefinition} from './tile-poi-definition';

export class TilePoiGroup {
  readonly name: string;
  readonly definitions: List<TilePoiDefinition>;

  constructor(name: string,
              definitions: List<TilePoiDefinition>) {
    this.name = name;
    this.definitions = definitions;
  }

  public static fromJSON(jsonObject): TilePoiGroup {
    if (!jsonObject) {
      return undefined;
    }
    return new TilePoiGroup(
      jsonObject.name,
      jsonObject.definitions ? List(jsonObject.definitions.map(json => TilePoiDefinition.fromJSON(json))) : List()
    );
  }
}
