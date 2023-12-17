import { HttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { withHooks } from '@ngrx/signals';
import { patchState } from '@ngrx/signals';
import { withMethods } from '@ngrx/signals';
import { withState } from '@ngrx/signals';
import { signalStore } from '@ngrx/signals';
import { tap } from 'rxjs';

export interface Page1State {
  page: string | null;
}

export const Page1Store = signalStore(
  { providedIn: 'root' },
  withState<Page1State>({
    page: null,
  }),
  withMethods((state) => {
    const http = inject(HttpClient);
    return {
      load: () => {
        http
          .get('/api/page1', { responseType: 'text' })
          .pipe(
            tap((page) => {
              patchState(state, { page });
            })
          )
          .subscribe();
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
