import { computed } from '@angular/core';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withComputed } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';

export type UserState = {
  user: string | null;
  error: string | null;
  errorDetail: string | null;
};

const initialState: UserState = {
  user: null,
  error: null,
  errorDetail: null,
};

export const UserStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withComputed(({ user }) => ({
    loggedIn: computed(() => user() !== null),
  })),
  withMethods((store) => {
    return {
      updateUser: (user: string | null) => {
        patchState(store, { user });
      },
      updateError: (error: string | null) => {
        patchState(store, { error });
      },
      updateErrorDetail: (errorDetail: string | null) => {
        patchState(store, { errorDetail });
      },
      resetError: () => {
        patchState(store, { error: null, errorDetail: null });
      },
    };
  })
);
