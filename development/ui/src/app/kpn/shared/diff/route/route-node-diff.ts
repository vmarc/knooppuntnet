// this class is generated, please do not modify

import {Ref} from '../../common/ref';

export class RouteNodeDiff {

  constructor(public title?: string,
              public added?: Array<Ref>,
              public removed?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): RouteNodeDiff {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteNodeDiff();
    instance.title = jsonObject.title;
    instance.added = jsonObject.added ? jsonObject.added.map(json => Ref.fromJSON(json)) : [];
    instance.removed = jsonObject.removed ? jsonObject.removed.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

