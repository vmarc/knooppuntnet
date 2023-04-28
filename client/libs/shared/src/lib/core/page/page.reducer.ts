import { routerNavigationAction } from '@ngrx/router-store';
import { on } from '@ngrx/store';
import { createReducer } from '@ngrx/store';
import { PageState } from './page.state';
import { initialState } from './page.state';

export const pageReducer = createReducer<PageState>(
  initialState,
  on(routerNavigationAction, (state, action): PageState => {
    const url = action.payload.event.url;
    const showFooter = !(
      url.includes('/map') ||
      url.includes('/demo/') ||
      url.includes('/poi/areas')
    );
    if (state.showFooter === showFooter) {
      return state;
    }
    return {
      ...state,
      showFooter,
    };
  })
);
