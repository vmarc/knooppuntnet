import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { SpinnerComponent } from '@app/spinner';
import { AngularSplitModule } from 'angular-split';
import { TryoutPanelsMenuComponent } from './tryout-panels-menu.component';

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
        <as-split-area size="20">
          <div class="sidebar">
            <kpn-tryout-panels-menu />
            <div class="sidebar-panel">
              <p>Sidebar contents</p>
            </div>
          </div>
        </as-split-area>
        <as-split-area size="40">
          <p>Text details</p>
        </as-split-area>
        <as-split-area size="40">
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
    TryoutPanelsMenuComponent,
    AngularSplitModule,
  ],
})
export class TryoutPanelsPageComponent {}
