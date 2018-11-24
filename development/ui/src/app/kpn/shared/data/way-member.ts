// this class is generated, please do not modify

import {Way} from './way';

export class WayMember {
  readonly way: Way;
  readonly role: string;

  constructor(way: Way,
              role: string) {
    this.way = way;
    this.role = role;
  }

  public static fromJSON(jsonObject): WayMember {
    if (!jsonObject) {
      return undefined;
    }
    return new WayMember(
      Way.fromJSON(jsonObject.way),
      jsonObject.role
    );
  }
}
