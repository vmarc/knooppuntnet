import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {State} from './demo.state';
import * as DemoActions from './demo.actions';
import {tap} from 'rxjs/operators';
import {mergeMap} from 'rxjs/operators';
import {withLatestFrom} from 'rxjs/operators';
import {filter} from 'rxjs/operators';
import {DemoService} from './demo.service';
import {selectDemoVideoSource} from './demo.selectors';
import {selectCurrentVideoState} from './demo.selectors';
import {selectDemoEnabled} from './demo.selectors';

@Injectable()
export class DemoEffects {
  constructor(private actions$: Actions,
              private store: Store<State>,
              private router: Router,
              private demoService: DemoService) {
  }

  startVideo = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.startVideo),
        tap(action => this.router.navigate(['/demo', action.video]))
      ),
    {dispatch: false}
  );

  initialStartVideo = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.videoPlayerAvailable),
        mergeMap(() =>
          this.store.pipe(
            filter(selectDemoEnabled),
            select(selectDemoVideoSource)
          )
        ),
        tap((videoSource: string) => {
          this.demoService.setSource(videoSource);
        })
      ),
    {dispatch: false}
  );

  canPlay = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.canPlay),
        tap(action => this.demoService.play())
      ),
    {dispatch: false}
  );

  progressUpdate = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.updateProgress),
        tap(action => this.demoService.setProgress(action.progress))
      ),
    {dispatch: false}
  );

  pause = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.pause),
        tap(action => this.demoService.pause())
      ),
    {dispatch: false}
  );

  play = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.play),
        tap(action => this.demoService.play())
      ),
    {dispatch: false}
  );

  end = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.end),
        tap(action => this.demoService.end())
      ),
    {dispatch: false}
  );

  controlPlay = createEffect(
    () =>
      this.actions$.pipe(
        ofType(DemoActions.controlPlay),
        withLatestFrom(this.store.pipe(select(selectCurrentVideoState))),
        tap(([action, videoState]) => {
          if (action.video == videoState.video) {
            if (videoState.playing) {
              this.demoService.pause();
            } else {
              this.demoService.play();
            }
          } else {
            this.router.navigate(['/demo', action.video]);
          }
        })
      ),
    {dispatch: false}
  );

}
