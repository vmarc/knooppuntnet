// this class is generated, please do not modify

import {Fact} from '../../../custom/fact';

export class FactDiffs {

  constructor(readonly resolved: Fact[],
              readonly introduced: Fact[],
              readonly remaining: Fact[]) {
  }

  public static fromJSON(jsonObject: any): FactDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new FactDiffs(
      jsonObject.resolved.map((json: any) => Fact.fromJSON(json)),
      jsonObject.introduced.map((json: any) => Fact.fromJSON(json)),
      jsonObject.remaining.map((json: any) => Fact.fromJSON(json))
    );
  }
}
