import {NetworkType} from "../../../../../kpn/shared/network-type";

export class RouteAccessibleData {

  constructor(readonly networkType: NetworkType,
              readonly accessible: boolean,
              readonly color: string) {
  }

}
