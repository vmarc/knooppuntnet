import {NetworkType} from "../shared/network-type";

export class NetworkTypes {

  static rcn = new NetworkType("rcn");
  static rwn = new NetworkType("rwn");
  static rhn = new NetworkType("rhn");
  static rmn = new NetworkType("rmn");
  static rpn = new NetworkType("rpn");
  static rin = new NetworkType("rin");

  static all: Array<NetworkType> = [
    NetworkTypes.rcn,
    NetworkTypes.rwn,
    NetworkTypes.rhn,
    NetworkTypes.rmn,
    NetworkTypes.rpn,
    NetworkTypes.rin
  ];

}
