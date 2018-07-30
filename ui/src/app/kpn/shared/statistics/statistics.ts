// this class is generated, please do not modify

import {Statistic} from './statistic';

export class Statistics {

  constructor(public map?: Map<string, Statistic>) {
  }

  public static fromJSON(jsonObject): Statistics {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Statistics();
    instance.map = jsonObject.map;
    return instance;
  }
}

