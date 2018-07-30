// this class is generated, please do not modify

import {Fact} from '../../fact';

export class FactDiffs {

  constructor(public resolved?: Array<Fact>,
              public introduced?: Array<Fact>,
              public remaining?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): FactDiffs {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new FactDiffs();
    instance.resolved = jsonObject.resolved ? jsonObject.resolved.map(json => Fact.fromJSON(json)) : [];
    instance.introduced = jsonObject.introduced ? jsonObject.introduced.map(json => Fact.fromJSON(json)) : [];
    instance.remaining = jsonObject.remaining ? jsonObject.remaining.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

