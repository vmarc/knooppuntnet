import { computed } from '@angular/core';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withComputed } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';

export interface UserState {
  user: string | null;
  error: string | null;
  errorDetail: string | null;
}

const initialState: UserState = {
  user: null,
  error: null,
  errorDetail: null,
};

export const UserStore = signalStore(
  { providedIn: 'root' },
  withState<UserState>(initialState),
  withComputed(({ user }) => ({
    loggedIn: computed(() => user !== null),
  })),
  withMethods((state) => {
    return {
      updateUser: (user: string | null) => {
        patchState(state, { user });
      },
      updateError: (error: string | null) => {
        patchState(state, { error });
      },
      updateErrorDetail: (errorDetail: string | null) => {
        patchState(state, { errorDetail });
      },
      resetError: () => {
        patchState(state, { error: null, errorDetail: null });
      },
    };
  }),
  withHooks({
    onInit() {
      console.log('UserStore.onInit()');
    },
    onDestroy() {
      console.log('UserStore.onDestroy()');
    },
  })
);
