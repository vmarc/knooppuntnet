// this class is generated, please do not modify

import {FactLevel} from './fact-level';

export class Fact {

  constructor(public id?: number,
              public name?: string,
              public nlName?: string,
              public level?: FactLevel) {
  }

  public static fromJSON(jsonObject): Fact {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new Fact();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.nlName = jsonObject.nlName;
    instance.level = FactLevel.fromJSON(jsonObject.level);
    return instance;
  }
}

