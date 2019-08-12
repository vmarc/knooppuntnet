import {NetworkType} from "../../../../kpn/shared/network-type";

export class IntegrityIndicatorData {
  constructor(readonly color: string,
              readonly networkType: NetworkType,
              readonly actual: number,
              readonly expected: number) {
  }

}
