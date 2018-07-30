// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class NodeRouteReferenceDiffs {

  constructor(public removed?: Array<Ref>,
              public added?: Array<Ref>,
              public remaining?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): NodeRouteReferenceDiffs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeRouteReferenceDiffs();
    instance.removed = jsonObject.removed ? jsonObject.removed.map(json => Ref.fromJSON(json)) : [];
    instance.added = jsonObject.added ? jsonObject.added.map(json => Ref.fromJSON(json)) : [];
    instance.remaining = jsonObject.remaining ? jsonObject.remaining.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

