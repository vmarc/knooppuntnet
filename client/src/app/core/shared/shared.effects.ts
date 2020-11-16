import {Injectable} from '@angular/core';
import {Actions} from '@ngrx/effects';
import {Store} from '@ngrx/store';
import {State} from '../demo/demo.model';
import {Router} from '@angular/router';

@Injectable()
export class SharedEffects {
  constructor(private actions$: Actions,
              private store: Store<State>,
              private router: Router) {
  }
}
