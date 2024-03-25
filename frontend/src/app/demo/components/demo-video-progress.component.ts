import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatSliderModule } from '@angular/material/slider';
import { DemoPageService } from '../demo-page.service';

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
  styles: `
    .progress {
      z-index: 3;
      position: absolute;
      padding-top: 720px;
    }

    .progress mat-slider {
      width: 1280px;
    }
  `,
  standalone: true,
  imports: [MatSliderModule],
})
export class DemoVideoProgressComponent {
  private readonly service = inject(DemoPageService);
  readonly progress = computed(() => {
    const time = this.service.time();
    const duration = this.service.duration();
    if (duration > 0) {
      return time / duration;
    }
    return 0;
  });

  updateProgress(event) {
    this.service.setProgress(event.value);
  }
}
