// this class is generated, please do not modify

import {List} from 'immutable';
import {DirectionsPath} from './directions-path';

export class Directions {

  constructor(readonly paths: List<DirectionsPath>) {
  }

  public static fromJSON(jsonObject): Directions {
    if (!jsonObject) {
      return undefined;
    }
    return new Directions(
      jsonObject.paths ? List(jsonObject.paths.map(json => DirectionsPath.fromJSON(json))) : List()
    );
  }
}
