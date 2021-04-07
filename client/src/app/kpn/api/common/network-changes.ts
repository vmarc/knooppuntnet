// this class is generated, please do not modify

import { ChangeSetNetwork } from './change-set-network';

export class NetworkChanges {
  constructor(
    readonly creates: Array<ChangeSetNetwork>,
    readonly updates: Array<ChangeSetNetwork>,
    readonly deletes: Array<ChangeSetNetwork>
  ) {}

  static fromJSON(jsonObject: any): NetworkChanges {
    if (!jsonObject) {
      return undefined;
    }
    return new NetworkChanges(
      jsonObject.creates.map((json: any) => ChangeSetNetwork.fromJSON(json)),
      jsonObject.updates.map((json: any) => ChangeSetNetwork.fromJSON(json)),
      jsonObject.deletes.map((json: any) => ChangeSetNetwork.fromJSON(json))
    );
  }
}
