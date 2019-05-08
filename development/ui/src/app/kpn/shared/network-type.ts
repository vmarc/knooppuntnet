export class NetworkType {

  constructor(public readonly oldName: string,
              public readonly name: string) {
  }

  public static fromJSON(jsonObject): NetworkType {
    if (!jsonObject) {
      return undefined;
    }
    if (jsonObject == "rcn") {
      return new NetworkType("rcn", "cycling");
    }
    if (jsonObject == "rwn") {
      return new NetworkType("rwn", "hiking");
    }
    if (jsonObject == "rhn") {
      return new NetworkType("rhn", "horse");
    }
    if (jsonObject == "rmn") {
      return new NetworkType("rmn", "motorboat");
    }
    if (jsonObject == "rpn") {
      return new NetworkType("rpn", "canoe");
    }
    if (jsonObject == "rin") {
      return new NetworkType("rin", "inline-skating");
    }
    return undefined;
  }
}
