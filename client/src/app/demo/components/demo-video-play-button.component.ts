import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { Store } from '@ngrx/store';
import { select } from '@ngrx/store';
import { Observable } from 'rxjs';
import { actionDemoPlay } from '../store/demo.actions';
import { selectDemoVideoPlayButtonEnabled } from '../store/demo.selectors';

@Component({
  selector: 'kpn-demo-video-play-button',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div
      *ngIf="videoPlayButtonEnabled$ | async"
      (click)="play()"
      class="play-button"
    >
      <mat-icon svgIcon="play"></mat-icon>
    </div>
  `,
  styles: [
    `
      .play-button {
        z-index: 9;
        position: absolute;
        width: 1280px;
        height: 720px;
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .play-button mat-icon {
        width: 100px;
        height: 100px;
        color: red;
        animation: pulse 3s infinite ease-out;
      }

      @keyframes pulse {
        0% {
          opacity: 10%;
        }
        50% {
          opacity: 100%;
        }
        100% {
          opacity: 10%;
        }
      }
    `,
  ],
})
export class DemoVideoPlayButtonComponent {
  videoPlayButtonEnabled$: Observable<boolean>;

  constructor(private store: Store) {
    this.videoPlayButtonEnabled$ = this.store.pipe(
      select(selectDemoVideoPlayButtonEnabled)
    );
  }

  play(): void {
    this.store.dispatch(actionDemoPlay());
  }
}
