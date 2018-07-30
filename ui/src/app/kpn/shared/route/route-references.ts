// this class is generated, please do not modify

import {Reference} from '../common/reference';

export class RouteReferences {

  constructor(public networkReferences?: Array<Reference>) {
  }

  public static fromJSON(jsonObject): RouteReferences {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteReferences();
    instance.networkReferences = jsonObject.networkReferences ? jsonObject.networkReferences.map(json => Reference.fromJSON(json)) : [];
    return instance;
  }
}

