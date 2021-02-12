// this class is generated, please do not modify

import {Tags} from '../custom/tags';

export class Poi {

  constructor(readonly elementType: string,
              readonly elementId: number,
              readonly latitude: string,
              readonly longitude: string,
              readonly layers: Array<string>,
              readonly tags: Tags,
              readonly tiles: Array<string>) {
  }

  public static fromJSON(jsonObject: any): Poi {
    if (!jsonObject) {
      return undefined;
    }
    return new Poi(
      jsonObject.elementType,
      jsonObject.elementId,
      jsonObject.latitude,
      jsonObject.longitude,
      jsonObject.layers,
      Tags.fromJSON(jsonObject.tags),
      jsonObject.tiles
    );
  }
}
