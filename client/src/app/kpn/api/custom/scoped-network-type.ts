import { NetworkScope } from './network-scope';
import { NetworkType } from './network-type';
import { List } from 'immutable';

export class ScopedNetworkType {
  public static all: List<ScopedNetworkType> = List(
    NetworkType.all.flatMap((networkType) =>
      NetworkScope.all.map((scope) =>
        ScopedNetworkType.create(scope, networkType)
      )
    )
  );

  private constructor(
    readonly networkScope: NetworkScope,
    readonly networkType: NetworkType,
    readonly key: string
  ) {}

  public static create(
    networkScope: NetworkScope,
    networkType: NetworkType
  ): ScopedNetworkType {
    const key = networkScope.letter + networkType.letter + 'n';
    return new ScopedNetworkType(networkScope, networkType, key);
  }

  public static fromJSON(jsonObject: any): ScopedNetworkType {
    if (!jsonObject) {
      return undefined;
    }
    return new ScopedNetworkType(
      NetworkScope.fromJSON(jsonObject.networkScope),
      NetworkType.fromJSON(jsonObject.networkType),
      jsonObject.key
    );
  }
}
