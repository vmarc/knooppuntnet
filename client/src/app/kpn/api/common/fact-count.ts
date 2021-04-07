// this class is generated, please do not modify

import { Fact } from '../custom/fact';

export class FactCount {
  constructor(readonly fact: Fact, readonly count: number) {}

  public static fromJSON(jsonObject: any): FactCount {
    if (!jsonObject) {
      return undefined;
    }
    return new FactCount(jsonObject.fact, jsonObject.count);
  }
}
