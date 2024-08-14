import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatToolbar } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { SpinnerComponent } from '@app/spinner';
import { ResizableDirective } from './resizeable.directive';
import { TryoutPanelsMenuComponent } from './tryout-panels-menu.component';

@Component({
  selector: 'kpn-tryout-panels-page-1',
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
    <main class="page-contents">
      <div class="container">
        <kpn-tryout-panels-menu />
        <div class="div1" appResizable>
          <p>Sidebar contents</p>
        </div>
        <div class="div2" [resizableMinWidth]="30" appResizable>
          <p>Text details</p>
        </div>
        <div class="div3" appResizable>
          <p>Map</p>
        </div>
      </div>
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

    .page-contents {
      display: flex;
      min-height: calc(100vh - 48px);
      flex-direction: column;
    }

    .container {
      display: flex;
      justify-content: flex-start;
      flex-direction: row;
    }

    .div1 {
      position: relative;
      width: 20vw;
      height: calc(100vh - 48px);
      flex-grow: 0;
      background-color: rgb(250, 250, 250);
    }

    .div2 {
      position: relative;
      width: 40vw;
      height: calc(100vh - 48px);
      flex-grow: 0;
    }

    .div3 {
      position: relative;
      width: 40vw;
      height: calc(100vh - 48px);
      flex-grow: 0;
    }

    p {
      padding: 1em;
    }
  `,
  standalone: true,
  imports: [
    ResizableDirective,
    MatButton,
    MatToolbar,
    RouterLink,
    SpinnerComponent,
    TryoutPanelsMenuComponent,
  ],
})
export class TryoutPanelsPage1Component {}
