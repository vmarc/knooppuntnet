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
}

export const UserStore = signalStore(
  { providedIn: 'root' },
  withState<UserState>({
    user: null,
    error: null,
  }),
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
