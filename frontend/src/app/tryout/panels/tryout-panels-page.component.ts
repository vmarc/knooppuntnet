import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { SpinnerComponent } from '@app/spinner';
import { AngularSplitModule } from 'angular-split';
import { TryoutPanelsAnalysisComponent } from './tryout-panels-analysis.component';
import { TryoutPanelsConfigurationComponent } from './tryout-panels-configuration.component';
import { TryoutPanelsMenuComponent } from './tryout-panels-menu.component';
import { TryoutPanelsMonitorComponent } from './tryout-panels-monitor.component';
import { TryoutPanelsPlannerComponent } from './tryout-panels-planner.component';
import { TryoutPanelsSearchComponent } from './tryout-panels-search.component';
import { TryoutPanelsService } from './tryout-panels.service';

@Component({
  selector: 'kpn-tryout-panels-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <header>
      <mat-toolbar>
        <button mat-button routerLink="/" class="toolbar-app-name">
          <div i18n="@@toolbar.title">knooppuntnet</div>
        </button>
        <kpn-spinner />
      </mat-toolbar>
    </header>
    <main>
      <as-split direction="horizontal" disabled="false" unit="percent" class="panels">
        <as-split-area size="40">
          <div class="sidebar">
            <div class="sidebar-panel">
              @switch (service.sidebar()) {
                @case ('menu') {
                  <kpn-tryout-panels-menu />
                }
                @case ('search') {
                  <kpn-tryout-panels-search />
                }
                @case ('planner') {
                  <kpn-tryout-panels-planner />
                }
                @case ('configuration') {
                  <kpn-tryout-panels-configuration />
                }
                @case ('analysis') {
                  <kpn-tryout-panels-analysis />
                }
                @case ('monitor') {
                  <kpn-tryout-panels-monitor />
                }
              }
            </div>
          </div>
        </as-split-area>
        <as-split-area size="60">
          <p>Map</p>
        </as-split-area>
      </as-split>
    </main>
  `,
  styles: `
    .toolbar-app-name {
      margin-left: 8px;
      font-size: 20px;
      font-weight: 400;
      letter-spacing: 0.0125em;
    }

    mat-toolbar {
      border-bottom: solid 1px lightgray;
    }

    .mat-toolbar-row,
    .mat-toolbar-single-row {
      height: 47px;
    }

    .panels {
      height: calc(100vh - 48px);
    }

    .sidebar {
      display: flex;
      height: 100%;
    }

    .sidebar-panel {
      background-color: rgb(250, 250, 250);
      width: 100%;
    }

    p {
      padding: 1em;
    }
  `,
  standalone: true,
  imports: [
    MatButton,
    MatToolbar,
    RouterLink,
    SpinnerComponent,
    AngularSplitModule,
    TryoutPanelsMenuComponent,
    TryoutPanelsSearchComponent,
    TryoutPanelsPlannerComponent,
    TryoutPanelsConfigurationComponent,
    TryoutPanelsAnalysisComponent,
    TryoutPanelsMonitorComponent,
  ],
})
export class TryoutPanelsPageComponent {
  readonly service = inject(TryoutPanelsService);
}
