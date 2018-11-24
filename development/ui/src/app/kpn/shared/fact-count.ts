// this class is generated, please do not modify

import {Fact} from './fact';

export class FactCount {
  readonly fact: Fact;
  readonly count: number;

  constructor(fact: Fact,
              count: number) {
    this.fact = fact;
    this.count = count;
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
