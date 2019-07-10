import {NetworkType} from "../shared/network-type";
import {List} from "immutable";

export class NetworkTypes {

  static cycling = new NetworkType("rcn", "cycling");
  static hiking = new NetworkType("rwn", "hiking");
  static horse = new NetworkType("rhn", "horse");
  static motorboat = new NetworkType("rmn", "motorboat");
  static canoe = new NetworkType("rpn", "canoe");
  static inlineSkating = new NetworkType("rin", "inline-skating");

  static all: List<NetworkType> = List([
    NetworkTypes.cycling,
    NetworkTypes.hiking,
    NetworkTypes.horse,
    NetworkTypes.motorboat,
    NetworkTypes.canoe,
    NetworkTypes.inlineSkating
  ]);

  static withName(name: string): NetworkType {
    return NetworkTypes.all.find(n => n.name == name);
  }

}
