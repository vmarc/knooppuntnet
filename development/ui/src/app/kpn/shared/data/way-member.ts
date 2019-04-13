// this class is generated, please do not modify

import {Way} from "./way";

export class WayMember {

  constructor(readonly way: Way,
              readonly role: string) {
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
