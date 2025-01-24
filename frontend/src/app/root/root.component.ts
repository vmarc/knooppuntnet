import { NgClass } from '@angular/common';
import { NgTemplateOutlet } from '@angular/common';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarFooterComponent } from '@app/components/shared/sidebar';
import { AngularSplitModule } from 'angular-split';
import { NzContentComponent } from 'ng-zorro-antd/layout';
import { NzLayoutComponent } from 'ng-zorro-antd/layout';
import { MapComponent } from '../map/map.component';
import { State } from '@app/state';
import { RootService } from './root.service';
import { ToolbarComponent } from './toolbar/toolbar.component';

@Component({
  selector: 'kpn-root',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-layout>
      <kpn-toolbar />
      <nz-content>
        @if (small()) {
          <div [ngClass]="{ hidden: hideText() }">
            <div class="main content">
              <div class="text-panel-container">
                <div class="text-panel-body">
                  <div>
                    <ng-container *ngTemplateOutlet="text"></ng-container>
                  </div>
                </div>
                <div class="text-panel-footer">
                  <kpn-sidebar-footer />
                </div>
              </div>
            </div>
          </div>
          <div [ngClass]="{ hidden: hideMap() }">
            <div class="map-panel-container content">
              <kpn-map />
            </div>
          </div>
        } @else {
          <div class="main content">
            <as-split direction="horizontal" disabled="false" unit="percent">
              <!-- 40 -->
              <as-split-area size="70">
                <div class="text-panel-container">
                  <div class="text-panel-body">
                    <ng-container *ngTemplateOutlet="text"></ng-container>
                  </div>
                  <div class="text-panel-footer">
                    <kpn-sidebar-footer />
                  </div>
                </div>
              </as-split-area>
              <!-- 60 -->
              <as-split-area size="30">
                <kpn-map />
              </as-split-area>
            </as-split>
          </div>
        }
        <ng-template #text>
          <ng-content />
        </ng-template>
      </nz-content>
    </nz-layout>
  `,
  styles: `
    .content {
      position: fixed;
      left: 0;
      top: 48px;
      width: 100vw;
      height: calc(100vh - 48px);
      display: flex;
    }

    .main {
      flex-direction: column;
    }

    as-split {
      flex: 1;
    }

    .hidden {
      display: none;
    }

    .text-panel-container {
      display: flex;
      flex-direction: column;
      align-items: stretch;
      height: 100%;
      overflow-y: auto;
    }

    .text-panel-body {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: stretch;
      align-content: stretch;
    }

    .text-panel-footer {
      flex: 0;
    }
  `,
  imports: [
    AngularSplitModule,
    NgTemplateOutlet,
    NgClass,
    MapComponent,
    SidebarFooterComponent,
    NzLayoutComponent,
    NzContentComponent,
    ToolbarComponent,
  ],
})
export class RootPageComponent {
  private readonly state = inject(State);
  private readonly rootService = inject(RootService);
  readonly small = this.state.page.small;
  readonly hideText = computed(() => this.state.page.activePanel() !== 'text');
  readonly hideMap = computed(() => this.state.page.activePanel() !== 'map');
}
