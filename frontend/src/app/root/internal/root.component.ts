import { NgClass } from '@angular/common';
import { NgTemplateOutlet } from '@angular/common';
import { AfterViewInit } from '@angular/core';
import { ElementRef } from '@angular/core';
import { viewChild } from '@angular/core';
import { computed } from '@angular/core';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { SidebarFooterComponent } from '@app/shared/components/sidebar/sidebar-footer.component';
import { NzContentComponent } from 'ng-zorro-antd/layout';
import { NzLayoutComponent } from 'ng-zorro-antd/layout';
import { OldMapComponent } from '@app/mapold/old-map.component';
import { State } from '@app/state/state';
import { NzSplitterPanelComponent } from 'ng-zorro-antd/splitter';
import { NzSplitterComponent } from 'ng-zorro-antd/splitter';
import { RootService } from './root.service';
import { ToolbarComponent } from './toolbar/toolbar.component';

@Component({
  selector: 'ui-root',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <nz-layout>
      <ui-toolbar />
      <nz-content>
        @if (small()) {
          <div [ngClass]="{ hidden: hideText() }">
            <div class="main content">
              <div class="text-panel-container">
                <div class="text-panel-body">
                  <div>
                    <ng-container *ngTemplateOutlet="text" />
                  </div>
                </div>
                <div class="text-panel-footer">
                  <ui-sidebar-footer />
                </div>
              </div>
            </div>
          </div>
          <div [ngClass]="{ hidden: hideMap() }">
            <div class="map-panel-container content">
              <ui-old-map />
            </div>
          </div>
        } @else {
          <div class="main content">
            <nz-splitter (nzResize)="resized($event)">
              <nz-splitter-panel nzDefaultSize="40%" [nzCollapsible]="true">
                <div #leftPanel class="text-panel-container">
                  <div class="text-panel-body">
                    <ng-container *ngTemplateOutlet="text" />
                  </div>
                  <div class="text-panel-footer">
                    <ui-sidebar-footer />
                  </div>
                </div>
              </nz-splitter-panel>
              <nz-splitter-panel nzDefaultSize="60%" [nzCollapsible]="true">
                <ui-old-map />
              </nz-splitter-panel>
            </nz-splitter>
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
    OldMapComponent,
    NgClass,
    NgTemplateOutlet,
    NzContentComponent,
    NzLayoutComponent,
    NzSplitterComponent,
    NzSplitterPanelComponent,
    SidebarFooterComponent,
    ToolbarComponent,
  ],
})
export class RootPageComponent implements AfterViewInit {
  private readonly state = inject(State);
  private readonly rootService = inject(RootService);
  readonly small = this.state.page.small;
  readonly hideText = computed(() => this.state.page.activePanel() !== 'text');
  readonly hideMap = computed(() => this.state.page.activePanel() !== 'map');

  private readonly leftPanel = viewChild<ElementRef>('leftPanel');

  ngAfterViewInit(): void {
    const panelWidth = this.leftPanel()?.nativeElement.clientWidth;
    if (panelWidth) {
      this.state.splitState.update(panelWidth, window.innerWidth - panelWidth);
    }
  }

  resized(sizes: number[]) {
    if (sizes.length === 2) {
      this.state.splitState.update(Math.round(sizes[0]), Math.round(sizes[1]));
    }
  }
}
