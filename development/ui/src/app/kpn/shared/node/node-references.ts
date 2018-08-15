// this class is generated, please do not modify

import {Reference} from '../common/reference';

export class NodeReferences {

  constructor(public networkReferences?: Array<Reference>,
              public routeReferences?: Array<Reference>) {
  }

  public static fromJSON(jsonObject): NodeReferences {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeReferences();
    instance.networkReferences = jsonObject.networkReferences ? jsonObject.networkReferences.map(json => Reference.fromJSON(json)) : [];
    instance.routeReferences = jsonObject.routeReferences ? jsonObject.routeReferences.map(json => Reference.fromJSON(json)) : [];
    return instance;
  }
}

