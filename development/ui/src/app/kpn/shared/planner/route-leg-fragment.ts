// this class is generated, please do not modify

import {List} from 'immutable';
import {LatLonImpl} from '../lat-lon-impl';
import {RouteLegNode} from './route-leg-node';

export class RouteLegFragment {

  constructor(readonly sink: RouteLegNode,
              readonly meters: number,
              readonly latLons: List<LatLonImpl>) {
  }

  public static fromJSON(jsonObject): RouteLegFragment {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteLegFragment(
      RouteLegNode.fromJSON(jsonObject.sink),
      jsonObject.meters,
      jsonObject.latLons ? List(jsonObject.latLons.map(json => LatLonImpl.fromJSON(json))) : List()
    );
  }
}
