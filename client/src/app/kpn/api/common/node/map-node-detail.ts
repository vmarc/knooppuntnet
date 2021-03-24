// this class is generated, please do not modify

import {Timestamp} from '../../custom/timestamp';
import {Ref} from '../common/ref';

export class MapNodeDetail {

  constructor(readonly id: number,
              readonly name: string,
              readonly latitude: string,
              readonly longitude: string,
              readonly lastUpdated: Timestamp,
              readonly networkReferences: Array<Ref>,
              readonly routeReferences: Array<Ref>) {
  }

  static fromJSON(jsonObject: any): MapNodeDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new MapNodeDetail(
      jsonObject.id,
      jsonObject.name,
      jsonObject.latitude,
      jsonObject.longitude,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      jsonObject.networkReferences.map((json: any) => Ref.fromJSON(json)),
      jsonObject.routeReferences.map((json: any) => Ref.fromJSON(json))
    );
  }
}
