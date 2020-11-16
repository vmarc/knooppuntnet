import {Injectable} from '@angular/core';
import {Router} from '@angular/router';
import {Store} from '@ngrx/store';
import {select} from '@ngrx/store';
import {Actions, createEffect, ofType} from '@ngrx/effects';
import {State} from './demo.model';
import {actionDemoPlay} from './demo.actions';
import {actionDemoPause} from './demo.actions';
import {actionDemoEnd} from './demo.actions';
import {actionDemoVideoPlayerAvailable} from './demo.actions';
import {actionDemoStartVideo} from './demo.actions';
import {actionDemoControlPlay} from './demo.actions';
import {actionDemoCanPlay} from './demo.actions';
import {actionDemoUpdateProgress} from './demo.actions';
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
        ofType(actionDemoStartVideo),
        tap(action => this.router.navigate(['/demo', action.video]))
      ),
    {dispatch: false}
  );

  initialStartVideo = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoVideoPlayerAvailable),
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
        ofType(actionDemoCanPlay),
        tap(action => this.demoService.play())
      ),
    {dispatch: false}
  );

  progressUpdate = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoUpdateProgress),
        tap(action => this.demoService.setProgress(action.progress))
      ),
    {dispatch: false}
  );

  pause = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoPause),
        tap(action => this.demoService.pause())
      ),
    {dispatch: false}
  );

  play = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoPlay),
        tap(action => this.demoService.play())
      ),
    {dispatch: false}
  );

  end = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoEnd),
        tap(action => this.demoService.end())
      ),
    {dispatch: false}
  );

  controlPlay = createEffect(
    () =>
      this.actions$.pipe(
        ofType(actionDemoControlPlay),
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
