import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';

type NewPreferencesState = {
  pageSize: number | null;
};

const initialState: NewPreferencesState = {
  pageSize: 25,
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
    };
  })
);
