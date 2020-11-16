// this class is generated, please do not modify

import {List} from 'immutable';
import {Ref} from '../common/ref';

export class MapRouteDetail {

  constructor(readonly id: number,
              readonly name: string,
              readonly networkReferences: List<Ref>) {
  }

  public static fromJSON(jsonObject: any): MapRouteDetail {
    if (!jsonObject) {
      return undefined;
    }
    return new MapRouteDetail(
      jsonObject.id,
      jsonObject.name,
      jsonObject.networkReferences ? List(jsonObject.networkReferences.map((json: any) => Ref.fromJSON(json))) : List()
    );
  }
}
