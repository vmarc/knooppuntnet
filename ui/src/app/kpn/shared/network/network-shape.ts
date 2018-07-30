// this class is generated, please do not modify

import {Bounds} from '../bounds';

export class NetworkShape {

  constructor(public bounds?: Bounds,
              public coordinates?: string) {
  }

  public static fromJSON(jsonObject): NetworkShape {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkShape();
    instance.bounds = jsonObject.bounds;
    instance.coordinates = jsonObject.coordinates;
    return instance;
  }
}

