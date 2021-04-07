// this class is generated, please do not modify

import { Ref } from '../common/ref';

export class MapRouteDetail {
  constructor(
    readonly id: number,
    readonly name: string,
    readonly networkReferences: Array<Ref>
  ) {}

  static fromJSON(jsonObject: any): MapRouteDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new MapRouteDetail(
      jsonObject.id,
      jsonObject.name,
      jsonObject.networkReferences.map((json: any) => Ref.fromJSON(json))
    );
  }
}
