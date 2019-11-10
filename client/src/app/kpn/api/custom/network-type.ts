import {List} from "immutable";

export class NetworkType {

  static cycling = new NetworkType("rcn", "cycling", "c");
  static hiking = new NetworkType("rwn", "hiking", "w");
  static horseRiding = new NetworkType("rhn", "horse-riding", "h");
  static motorboat = new NetworkType("rmn", "motorboat", "m");
  static canoe = new NetworkType("rpn", "canoe", "p");
  static inlineSkating = new NetworkType("rin", "inline-skating", "i");

  static all: List<NetworkType> = List([
    NetworkType.cycling,
    NetworkType.hiking,
    NetworkType.horseRiding,
    NetworkType.motorboat,
    NetworkType.canoe,
    NetworkType.inlineSkating
  ]);

  private constructor(readonly id: string,
                      readonly name: string,
                      readonly letter: string) {
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
