import {NetworkTypes} from '../../common/network-types';
import {NetworkScope} from './network-scope';
import {NetworkType} from './network-type';
import {List} from 'immutable';

export class ScopedNetworkType {

  public static all: List<ScopedNetworkType> = List(List(NetworkTypes.all).flatMap(networkType =>
    NetworkScope.all.map(scope => ScopedNetworkType.create(scope, networkType))
  ));

  private constructor(readonly networkScope: NetworkScope,
                      readonly networkType: NetworkType,
                      readonly key: string) {
  }

  public static create(networkScope: NetworkScope,
                       networkType: NetworkType): ScopedNetworkType {
    const letter = NetworkTypes.letter(networkType);
    const key = networkScope.letter + letter + 'n';
    return new ScopedNetworkType(networkScope, networkType, key);
  }

  public static fromJSON(jsonObject: any): ScopedNetworkType {
    if (!jsonObject) {
      return undefined;
    }
    return new ScopedNetworkType(
      NetworkScope.fromJSON(jsonObject.networkScope),
      jsonObject.networkType,
      jsonObject.key
    );
  }

}
