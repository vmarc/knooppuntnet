import { NetworkType } from '@api/custom/network-type';

export class NetworkTypes {
  static all: NetworkType[] = [
    NetworkType.cycling,
    NetworkType.hiking,
    NetworkType.horseRiding,
    NetworkType.motorboat,
    NetworkType.canoe,
    NetworkType.inlineSkating,
  ];

  static withName(name: string): NetworkType {
    return NetworkTypes.all.find((networkType) => networkType === name);
  }

  static tagValue(networkType: NetworkType): string {
    if (networkType === NetworkType.cycling) {
      return 'rcn';
    }

    if (networkType === NetworkType.hiking) {
      return 'rwn';
    }

    if (networkType === NetworkType.horseRiding) {
      return 'rhn';
    }

    if (networkType === NetworkType.motorboat) {
      return 'rmn';
    }

    if (networkType === NetworkType.canoe) {
      return 'rpn';
    }

    if (networkType === NetworkType.inlineSkating) {
      return 'rin';
    }

    return '???';
  }

  static letter(networkType: NetworkType): string {
    if (networkType === NetworkType.cycling) {
      return 'c';
    }

    if (networkType === NetworkType.hiking) {
      return 'w';
    }

    if (networkType === NetworkType.horseRiding) {
      return 'h';
    }

    if (networkType === NetworkType.motorboat) {
      return 'm';
    }

    if (networkType === NetworkType.canoe) {
      return 'p';
    }

    if (networkType === NetworkType.inlineSkating) {
      return 'i';
    }

    return '?';
  }
}
