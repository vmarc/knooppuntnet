// this class is generated, please do not modify

import {NodeIntegrityCheck} from '../node-integrity-check';
import {Ref} from '../common/ref';
import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class NetworkNodeInfo2 {

  constructor(public id?: number,
              public name?: string,
              public number?: string,
              public latitude?: string,
              public longitude?: string,
              public connection?: boolean,
              public definedInRelation?: boolean,
              public definedInRoute?: boolean,
              public timestamp?: Timestamp,
              public routeReferences?: Array<Ref>,
              public integrityCheck?: NodeIntegrityCheck,
              public tags?: Tags) {
  }

  public static fromJSON(jsonObject): NetworkNodeInfo2 {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkNodeInfo2();
    instance.id = jsonObject.id;
    instance.name = jsonObject.name;
    instance.number = jsonObject.number;
    instance.latitude = jsonObject.latitude;
    instance.longitude = jsonObject.longitude;
    instance.connection = jsonObject.connection;
    instance.definedInRelation = jsonObject.definedInRelation;
    instance.definedInRoute = jsonObject.definedInRoute;
    instance.timestamp = Timestamp.fromJSON(jsonObject.timestamp);
    instance.routeReferences = jsonObject.routeReferences ? jsonObject.routeReferences.map(json => Ref.fromJSON(json)) : [];
    instance.integrityCheck = NodeIntegrityCheck.fromJSON(jsonObject.integrityCheck);
    instance.tags = Tags.fromJSON(jsonObject.tags);
    return instance;
  }
}

