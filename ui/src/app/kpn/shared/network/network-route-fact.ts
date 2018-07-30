// this class is generated, please do not modify

import {Fact} from '../fact';
import {Ref} from '../common/ref';

export class NetworkRouteFact {

  constructor(public fact?: Fact,
              public routes?: Array<Ref>) {
  }

  public static fromJSON(jsonObject): NetworkRouteFact {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkRouteFact();
    instance.fact = Fact.fromJSON(jsonObject.fact);
    instance.routes = jsonObject.routes ? jsonObject.routes.map(json => Ref.fromJSON(json)) : [];
    return instance;
  }
}

