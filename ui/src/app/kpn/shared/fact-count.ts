// this class is generated, please do not modify

import {Fact} from './fact';

export class FactCount {

  constructor(public fact?: Fact,
              public count?: number) {
  }

  public static fromJSON(jsonObject): FactCount {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new FactCount();
    instance.fact = jsonObject.fact;
    instance.count = jsonObject.count;
    return instance;
  }
}

