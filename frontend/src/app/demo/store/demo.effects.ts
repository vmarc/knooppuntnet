import { inject } from '@angular/core';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { concatLatestFrom } from '@ngrx/effects';
import { Actions } from '@ngrx/effects';
import { createEffect } from '@ngrx/effects';
import { ofType } from '@ngrx/effects';
import { Store } from '@ngrx/store';
import { select } from '@ngrx/store';
import { tap } from 'rxjs/operators';
import { mergeMap } from 'rxjs/operators';
import { filter } from 'rxjs/operators';
import { DemoService } from '../demo.service';
import { actionDemoVideoPlayerAvailable } from './demo.actions';
import { actionDemoCanPlay } from './demo.actions';
import { actionDemoUpdateProgress } from './demo.actions';
import { actionDemoPause } from './demo.actions';
import { actionDemoPlay } from './demo.actions';
import { actionDemoEnd } from './demo.actions';
import { actionDemoControlPlay } from './demo.actions';
import { selectDemoPlaying } from './demo.selectors';
import { selectDemoVideo } from './demo.selectors';
import { selectDemoEnabled } from './demo.selectors';

@Injectable()
export class DemoEffects {
  private readonly actions$ = inject(Actions);
  private readonly store = inject(Store);
  private readonly router = inject(Router);
  private readonly demoService = inject(DemoService);

  // noinspection JSUnusedGlobalSymbols
  initialStartVideo = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoVideoPlayerAvailable),
        mergeMap(() => this.store.pipe(filter(selectDemoEnabled), select(selectDemoVideo))),
        tap((video: string) => {
          this.demoService.setSource(`/videos/en/${video}.mp4`);
        })
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  canPlay = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoCanPlay),
        tap(() => this.demoService.play())
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  progressUpdate = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoUpdateProgress),
        tap((action) => this.demoService.setProgress(action.progress))
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  pause = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoPause),
        tap(() => this.demoService.pause())
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  play = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoPlay),
        tap(() => this.demoService.play())
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  end = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoEnd),
        tap(() => this.demoService.end())
      );
    },
    { dispatch: false }
  );

  // noinspection JSUnusedGlobalSymbols
  controlPlay = createEffect(
    () => {
      return this.actions$.pipe(
        ofType(actionDemoControlPlay),
        concatLatestFrom(() => [
          this.store.select(selectDemoVideo),
          this.store.select(selectDemoPlaying),
        ]),
        tap(([action, video, playing]) => {
          if (action.video === video) {
            if (playing) {
              this.demoService.pause();
            } else {
              this.demoService.play();
            }
          } else {
            this.router.navigate(['/demo', action.video]);
          }
        })
      );
    },
    { dispatch: false }
  );
}
