// this class is generated, please do not modify

import {List} from 'immutable';
import {Tags} from './data/tags';

export class Poi {

  constructor(readonly elementType: string,
              readonly elementId: number,
              readonly latitude: string,
              readonly longitude: string,
              readonly layers: List<string>,
              readonly tags: Tags) {
  }

  public static fromJSON(jsonObject): Poi {
    if (!jsonObject) {
      return undefined;
    }
    return new Poi(
      jsonObject.elementType,
      jsonObject.elementId,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.layers ? List(jsonObject.layers) : List(),
      Tags.fromJSON(jsonObject.tags)
    );
  }
}
