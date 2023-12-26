import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';

export interface Page2State {
  page: string | null;
}

const initialState: Page2State = {
  page: null,
};

export const Page2Store = signalStore(
  withState<Page2State>(initialState),
  withMethods((state) => {
    const http = inject(HttpClient);
    return {
      load: () => {
        http.get('/api/page2', { responseType: 'text' }).subscribe((page) => {
          patchState(state, { page });
        });
      },
    };
  }),
  withHooks({
    onInit({ load }) {
      load();
    },
  })
);
