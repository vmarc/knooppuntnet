import {Injectable} from '@angular/core';
import {ofType} from '@ngrx/effects';
import {createEffect} from '@ngrx/effects';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {tap} from 'rxjs/operators';
import {AppState} from '../../store/app.state';
import {actionPage6a} from './page6.actions';

@Injectable()
export class Page6Effects {

  constructor(private actions$: Actions,
              private store: Store<AppState>,
  ) {
  }

  mapFocusEffect = createEffect(() =>
      this.actions$.pipe(
        ofType(actionPage6a),
        tap(action => console.log('EFFECT'))
      ),
    {dispatch: false}
  );
}
