// this class is generated, please do not modify

import {Fact} from '../../custom/fact';
import {Ref} from '../common/ref';

export class NetworkNodeFact {

  constructor(readonly fact: Fact,
              readonly nodes: Array<Ref>) {
  }

  public static fromJSON(jsonObject: any): NetworkNodeFact {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkNodeFact(
      Fact.fromJSON(jsonObject.fact),
      jsonObject.nodes.map((json: any) => Ref.fromJSON(json))
    );
  }
}
