import { SheriffConfig } from '@softarc/sheriff-core';

// noinspection JSUnusedGlobalSymbols
export const sheriffConfig: SheriffConfig = {
  modules: {
    'src/app/api': 'noTag',
    'src/app/analysis/analysis': 'noTag',
    'src/app/analysis/changeset': 'noTag',
    'src/app/analysis/fact': 'noTag',
    'src/app/analysis/facts': 'noTag',
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
