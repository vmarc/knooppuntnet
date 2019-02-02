// this class is generated, please do not modify

import {Fact} from './fact';

export class FactCount {

  constructor(readonly fact: Fact,
              readonly count: number) {
  }

  public static fromJSON(jsonObject): FactCount {
    if (!jsonObject) {
      return undefined;
    }
    return new FactCount(
      Fact.fromJSON(jsonObject.fact),
      jsonObject.count
    );
  }
}
