import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSliderModule } from '@angular/material/slider';
import { Store } from '@ngrx/store';
import { actionDemoUpdateProgress } from '../store/demo.actions';
import { selectDemoProgress } from '../store/demo.selectors';

@Component({
  selector: 'kpn-demo-video-progress',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="progress">
      <mat-slider min="0" max="1" step="0.005" #ngSlider
        ><input
          matSliderThumb
          [value]="progress()"
          (input)="
            updateProgress({
              source: ngSliderThumb,
              parent: ngSlider,
              value: ngSliderThumb.value
            })
          "
          #ngSliderThumb="matSliderThumb"
        />
      </mat-slider>
    </div>
  `,
  styles: [
    `
      .progress {
        z-index: 3;
        position: absolute;
        padding-top: 720px;
      }

      .progress mat-slider {
        width: 1280px;
      }
    `,
  ],
  standalone: true,
  imports: [MatSliderModule, AsyncPipe],
})
export class DemoVideoProgressComponent {
  readonly progress = this.store.selectSignal(selectDemoProgress);

  constructor(private store: Store) {}

  updateProgress(event) {
    this.store.dispatch(actionDemoUpdateProgress({ progress: event.value }));
  }
}
