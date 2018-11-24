// this class is generated, please do not modify

import {NetworkType} from '../network-type';

export class Reference {
  readonly id: number;
  readonly name: string;
  readonly networkType: NetworkType;
  readonly connection: boolean;

  constructor(id: number,
              name: string,
              networkType: NetworkType,
              connection: boolean) {
    this.id = id;
    this.name = name;
    this.networkType = networkType;
    this.connection = connection;
  }

  public static fromJSON(jsonObject): Reference {
    if (!jsonObject) {
      return undefined;
    }
    return new Reference(
      jsonObject.id,
      jsonObject.name,
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.connection
    );
  }
}
