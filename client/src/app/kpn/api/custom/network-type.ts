import {List} from "immutable";

export class NetworkType {

  static cycling = new NetworkType("rcn", "cycling");
  static hiking = new NetworkType("rwn", "hiking");
  static horseRiding = new NetworkType("rhn", "horse-riding");
  static motorboat = new NetworkType("rmn", "motorboat");
  static canoe = new NetworkType("rpn", "canoe");
  static inlineSkating = new NetworkType("rin", "inline-skating");

  static all: List<NetworkType> = List([
    NetworkType.cycling,
    NetworkType.hiking,
    NetworkType.horseRiding,
    NetworkType.motorboat,
    NetworkType.canoe,
    NetworkType.inlineSkating
  ]);

  private constructor(readonly id: string,
                      readonly name: string) {
  }

  public static fromJSON(jsonObject): NetworkType {
    if (!jsonObject) {
      return undefined;
    }
    return this.withName(jsonObject);
  }

  static withName(name: string): NetworkType {
    return NetworkType.all.find(n => n.name == name);
  }

}
