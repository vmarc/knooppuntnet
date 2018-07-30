// this class is generated, please do not modify

import {NodeInfo} from '../node-info';
import {NodeReferences} from './node-references';

export class MapDetailNode {

  constructor(public info?: NodeInfo,
              public references?: NodeReferences) {
  }

  public static fromJSON(jsonObject): MapDetailNode {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new MapDetailNode();
    instance.info = jsonObject.info;
    instance.references = jsonObject.references;
    return instance;
  }
}

