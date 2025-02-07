import { SheriffConfig } from '@softarc/sheriff-core';

// noinspection JSUnusedGlobalSymbols
export const sheriffConfig: SheriffConfig = {
  modules: {
    'src/app/analysis/location': 'noTag',
    'src/app/analysis/network': 'noTag',
    'src/app/shared/components': 'noTag',
  },
  enableBarrelLess: true,
  depRules: {
    root: 'noTag',
    noTag: 'noTag',
  },
};
