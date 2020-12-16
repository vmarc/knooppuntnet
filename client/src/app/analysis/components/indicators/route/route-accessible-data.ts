import {NetworkType} from '@api/custom/network-type';

export class RouteAccessibleData {

  constructor(readonly networkType: NetworkType,
              readonly accessible: boolean,
              readonly color: string) {
  }

}
