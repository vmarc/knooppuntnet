// this class is generated, please do not modify

import { Fact } from '../../custom/fact';
import { Ref } from '../common/ref';

export class NetworkRouteFact {
  constructor(readonly fact: Fact, readonly routes: Array<Ref>) {}

  public static fromJSON(jsonObject: any): NetworkRouteFact {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkRouteFact(
      jsonObject.fact,
      jsonObject.routes.map((json: any) => Ref.fromJSON(json))
    );
  }
}
