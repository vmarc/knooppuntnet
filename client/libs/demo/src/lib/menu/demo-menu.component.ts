import { NgIf } from '@angular/common';
import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconModule } from '@angular/material/icon';
import { Store } from '@ngrx/store';
import { DemoDisabledComponent } from '../components/demo-disabled.component';
import { selectDemoEnabled } from '../store/demo.selectors';

@Component({
  selector: 'kpn-demo-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="enabled(); then comment; else disabled"></div>

    <ng-template #comment>
      <p i18n="@@demo.select-video">
        Select a video on the left by clicking its play button.
      </p>
    </ng-template>

    <ng-template #disabled>
      <kpn-demo-disabled />
    </ng-template>

    <div class="video-icon">
      <mat-icon svgIcon="video" />
    </div>
  `,
  styles: [
    `
      .video-icon {
        padding-top: 5em;
        display: flex;
        justify-content: center;
      }

      .video-icon mat-icon {
        width: 200px;
        height: 200px;
        color: #f8f8f8;
      }
    `,
  ],
  standalone: true,
  imports: [NgIf, DemoDisabledComponent, MatIconModule, AsyncPipe],
})
export class DemoMenuComponent {
  readonly enabled = this.store.selectSignal(selectDemoEnabled);

  constructor(private store: Store) {}
}
