// this class is generated, please do not modify

import {ChangeSetNetwork} from './change-set-network';

export class NetworkChanges {

  constructor(public creates?: Array<ChangeSetNetwork>,
              public updates?: Array<ChangeSetNetwork>,
              public deletes?: Array<ChangeSetNetwork>) {
  }

  public static fromJSON(jsonObject): NetworkChanges {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new NetworkChanges();
    instance.creates = jsonObject.creates ? jsonObject.creates.map(json => ChangeSetNetwork.fromJSON(json)) : [];
    instance.updates = jsonObject.updates ? jsonObject.updates.map(json => ChangeSetNetwork.fromJSON(json)) : [];
    instance.deletes = jsonObject.deletes ? jsonObject.deletes.map(json => ChangeSetNetwork.fromJSON(json)) : [];
    return instance;
  }
}

