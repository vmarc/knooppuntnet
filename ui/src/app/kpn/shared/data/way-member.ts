// this class is generated, please do not modify

import {Way} from './way';

export class WayMember {

  constructor(public way?: Way,
              public role?: string) {
  }

  public static fromJSON(jsonObject): WayMember {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new WayMember();
    instance.way = jsonObject.way;
    instance.role = jsonObject.role;
    return instance;
  }
}

