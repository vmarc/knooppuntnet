import { NgClass } from '@angular/common';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { Component } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { input } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { DemoPageService } from '../demo-page.service';

@Component({
  selector: 'kpn-demo-video-control',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="video-control" [ngClass]="{ selected: selected() }">
      <div class="control-text">
        <div class="title">
          <ng-content />
        </div>
        <div class="duration">
          {{ duration() }} <span i18n="@@demo.duration.seconds">seconds</span>
        </div>
      </div>

      @if (service.enabled()) {
        <a (click)="play()" class="play-pause-button" [ngClass]="{ buttonSelected: selected() }">
          <mat-icon [svgIcon]="icon()" />
        </a>
      } @else {
        <span class="play-pause-button-disabled">
          <mat-icon [svgIcon]="icon()" />
        </span>
      }
    </div>
  `,
  styles: `
    .video-control {
      display: flex;
      border-bottom: 1px solid lightgrey;
      height: 4em;
      padding: 1em;
    }

    .control-text {
      flex-grow: 1;
      display: flex;
      flex-direction: column;
    }

    .selected {
      background-color: #fafafa;
    }

    .title {
      flex-grow: 1;
      padding-right: 10px;
    }

    .duration {
      color: lightgray;
    }

    .play-pause-button {
      color: lightgray;
      cursor: pointer;
    }

    .play-pause-button:hover {
      color: grey;
    }

    .play-pause-button-disabled {
      color: lightgray;
    }

    .buttonSelected {
      color: red;
    }

    .buttonSelected.play-pause-button:hover {
      color: red;
    }

    .play-pause-button mat-icon {
      width: 3em;
      height: 3em;
    }

    .play-pause-button-disabled mat-icon {
      width: 3em;
      height: 3em;
    }
  `,
  standalone: true,
  imports: [NgClass, MatIconModule],
})
export class DemoVideoControlComponent {
  name = input.required<string>();
  duration = input.required<string>();

  protected readonly service = inject(DemoPageService);

  protected readonly selected = computed(() => this.service.video() === this.name());
  protected readonly playing = computed(() => this.selected() && this.service.playing());
  protected readonly icon = computed(() => (this.playing() ? 'pause' : 'play'));

  play(): void {
    this.service.playVideo(this.name());
  }
}
