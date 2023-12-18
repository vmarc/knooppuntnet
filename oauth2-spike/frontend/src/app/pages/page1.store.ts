import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';

export interface Page1State {
  page: string | null;
}

const initialState: Page1State = {
  page: null,
};

export const Page1Store = signalStore(
  withState<Page1State>(initialState),
  withMethods((state) => {
    const http = inject(HttpClient);
    return {
      load: () => {
        http.get('/api/page1', { responseType: 'text' }).subscribe((page) => {
          patchState(state, { page });
        });
      },
    };
  }),
  withHooks({
    onInit({ load }) {
      console.log('Page1Store.onInit()');
      load();
    },
    onDestroy() {
      console.log('Page1Store.onDestroy()');
    },
  })
);
