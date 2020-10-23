import {Injectable} from "@angular/core";
import {Router} from "@angular/router";
import {Store} from '@ngrx/store';
import {Actions, createEffect, ofType} from "@ngrx/effects";
import {State} from "./demo.model";
import {actionDemoPlay} from "./demo.actions";
import {tap} from "rxjs/operators";

@Injectable()
export class DemoEffects {
  constructor(private actions$: Actions,
              private store: Store<State>,
              private router: Router
  ) {
  }

  startVideo = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoPlay),
        tap(action => this.router.navigate(["/demo", action.video]))
      ),
    {dispatch: false}
  );

}
