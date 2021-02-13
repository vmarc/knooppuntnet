import {NetworkType} from '@api/custom/network-type';

export class NetworkTypes {

  static cycling: NetworkType = 'cycling';
  static hiking: NetworkType = 'hiking';
  static horseRiding: NetworkType = 'horse-riding';
  static motorboat: NetworkType = 'motorboat';
  static canoe: NetworkType = 'canoe';
  static inlineSkating: NetworkType = 'inline-skating';

  static all: NetworkType[] = [
    NetworkTypes.cycling,
    NetworkTypes.hiking,
    NetworkTypes.horseRiding,
    NetworkTypes.motorboat,
    NetworkTypes.canoe,
    NetworkTypes.inlineSkating
  ];

  static withName(name: string): NetworkType {
    return NetworkTypes.all.find(networkType => networkType === name);
  }

  static tagValue(networkType: NetworkType): string {
    if (networkType === 'cycling') {
      return 'rcn';
    }

    if (networkType === 'hiking') {
      return 'rwn';
    }

    if (networkType === 'horse-riding') {
      return 'rhn';
    }

    if (networkType === 'motorboat') {
      return 'rmn';
    }

    if (networkType === 'canoe') {
      return 'rpn';
    }

    if (networkType === 'inline-skating') {
      return 'rin';
    }

    return '???';
  }

  static letter(networkType: NetworkType): string {
    if (networkType === 'cycling') {
      return 'c';
    }

    if (networkType === 'hiking') {
      return 'w';
    }

    if (networkType === 'horse-riding') {
      return 'h';
    }

    if (networkType === 'motorboat') {
      return 'm';
    }

    if (networkType === 'canoe') {
      return 'p';
    }

    if (networkType === 'inline-skating') {
      return 'i';
    }

    return '?';
  }

}
