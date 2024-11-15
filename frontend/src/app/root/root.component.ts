import { NgClass } from '@angular/common';
import { NgTemplateOutlet } from '@angular/common';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { AngularSplitModule } from 'angular-split';
import { MapComponent } from '../map/map.component';
import { RootService } from './root.service';
import { ToolbarComponent } from './toolbar.component';

@Component({
  selector: 'kpn-root',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <header>
      <kpn-toolbar />
    </header>
    <div class="page-contents">
      <main>
        @if (small()) {
          <div [ngClass]="{ hidden: hideText() }">
            <ng-container *ngTemplateOutlet="text"></ng-container>
          </div>
          <div [ngClass]="{ hidden: hideMap() }">
            <kpn-map />
          </div>
        } @else {
          <as-split direction="horizontal" disabled="false" unit="percent" class="split-panels">
            <as-split-area size="40">
              <ng-container *ngTemplateOutlet="text"></ng-container>
            </as-split-area>
            <as-split-area size="60">
              <kpn-map />
            </as-split-area>
          </as-split>
        }
        <ng-template #text>
          <ng-content />
        </ng-template>
      </main>
    </div>
  `,
  styles: `
    .split-panels {
      height: calc(100vh - 48px);
    }

    header {
      position: fixed;
      top: 0;
      left: 0;
      width: 100%;
      height: 48px;
      z-index: 1001;
    }

    .page-contents {
      margin-top: 48px;
      display: flex;
      min-height: calc(100vh - 48px);
      flex-direction: column;
    }

    main {
      flex: 1;
    }

    .hidden {
      display: none;
    }
  `,
  standalone: true,
  imports: [
    AngularSplitModule,
    NgTemplateOutlet,
    ToolbarComponent,
    NgClass,
    MapComponent,
    MapComponent,
  ],
})
export class RootPageComponent {
  private readonly rootService = inject(RootService);
  readonly small = this.rootService.small;
  readonly hideText = computed(() => this.rootService.activePanel() !== 'text');
  readonly hideMap = computed(() => this.rootService.activePanel() !== 'map');
}
