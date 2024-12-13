import { NetworkType } from '@api/common';

export class NetworkTypes {
  static all: NetworkType[] = [
    'cycling',
    'hiking',
    'horse-riding',
    'motorboat',
    'canoe',
    'inline-skating',
  ];

  static withName(name: string): NetworkType | undefined {
    return NetworkTypes.all.find((networkType) => networkType === name);
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
