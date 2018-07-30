// this class is generated, please do not modify

import {Country} from './country';
import {Fact} from './fact';
import {Tags} from './data/tags';
import {Timestamp} from './timestamp';

export class NodeInfo {

  constructor(public id?: number,
              public active?: boolean,
              public display?: boolean,
              public ignored?: boolean,
              public orphan?: boolean,
              public country?: Country,
              public name?: string,
              public rcnName?: string,
              public rwnName?: string,
              public latitude?: string,
              public longitude?: string,
              public lastUpdated?: Timestamp,
              public tags?: Tags,
              public facts?: Array<Fact>) {
  }

  public static fromJSON(jsonObject): NodeInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NodeInfo();
    instance.id = jsonObject.id;
    instance.active = jsonObject.active;
    instance.display = jsonObject.display;
    instance.ignored = jsonObject.ignored;
    instance.orphan = jsonObject.orphan;
    instance.country = jsonObject.country;
    instance.name = jsonObject.name;
    instance.rcnName = jsonObject.rcnName;
    instance.rwnName = jsonObject.rwnName;
    instance.latitude = jsonObject.latitude;
    instance.longitude = jsonObject.longitude;
    instance.lastUpdated = jsonObject.lastUpdated;
    instance.tags = jsonObject.tags;
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    return instance;
  }
}

