import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';

type NewPreferencesState = {
  pageSize: number | null;
  impact: boolean;
};

const initialState: NewPreferencesState = {
  pageSize: 25,
  impact: true,
};

export const PreferencesStore = signalStore(
  withState(initialState),
  withMethods((store) => {
    return {
      updatePageSize: (pageSize: number): void => {
        patchState(store, {
          pageSize,
        });
      },
      updateImpact: (impact: boolean): void => {
        patchState(store, {
          impact,
        });
      },
    };
  })
);
