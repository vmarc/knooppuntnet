// this class is generated, please do not modify

import { Fact } from '../../../custom/fact';

export class FactDiffs {
  constructor(
    readonly resolved: Fact[],
    readonly introduced: Fact[],
    readonly remaining: Fact[]
  ) {}

  static fromJSON(jsonObject: any): FactDiffs {
    if (!jsonObject) {
      return undefined;
    }
    return new FactDiffs(
      jsonObject.resolved,
      jsonObject.introduced,
      jsonObject.remaining
    );
  }
}
