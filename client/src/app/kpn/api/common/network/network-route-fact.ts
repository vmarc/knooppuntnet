// this class is generated, please do not modify

import { List } from 'immutable';
import { Fact } from '../../custom/fact';
import { Ref } from '../common/ref';

export class NetworkRouteFact {
  constructor(readonly fact: Fact, readonly routes: List<Ref>) {}

  public static fromJSON(jsonObject: any): NetworkRouteFact {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRouteFact(
      Fact.fromJSON(jsonObject.fact),
      jsonObject.routes
        ? List(jsonObject.routes.map((json: any) => Ref.fromJSON(json)))
        : List()
    );
  }
}
