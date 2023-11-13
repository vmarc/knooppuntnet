import { AsyncPipe } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink, RouterOutlet } from '@angular/router';
import { SpinnerComponent } from '@app/spinner';
import { Observable } from 'rxjs';
import { PageService } from '..';

@Component({
  selector: 'kpn-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-toolbar [class]="toolbarBackgroundColor$ | async">
      <button mat-icon-button (click)="toggleSidebarOpen()">
        <mat-icon svgIcon="menu" />
      </button>
      <button mat-button routerLink="/" class="toolbar-app-name">
        <div i18n="@@toolbar.title">knooppuntnet</div>
      </button>
      <kpn-spinner />
      <span class="toolbar-spacer"></span>
      <ng-content />
    </mat-toolbar>
  `,
  styles: [
    `
      :host {
        display: block;
        border-bottom: solid 1px lightgray;
      }

      .toolbar-spacer {
        flex: 1 1 auto;
      }

      .toolbar-app-name {
        margin-left: 8px;
        font-size: 20px;
        font-weight: 400;
        letter-spacing: 0.0125em;
      }

      .mat-toolbar-row,
      .mat-toolbar-single-row {
        height: 47px;
      }

      .toolbar-style-cycling {
        background-color: rgba(144, 238, 144, 0.3);
      }

      .toolbar-style-hiking {
        background-color: rgba(252, 185, 128, 0.3);
      }

      .toolbar-style-horse-riding {
        background-color: rgba(250, 97, 41, 0.3);
      }

      .toolbar-style-motorboat {
        background-color: rgba(135, 181, 250, 0.3);
      }

      .toolbar-style-canoe {
        background-color: rgba(20, 178, 252, 0.3);
      }

      .toolbar-style-inline-skating {
        background-color: rgba(255, 182, 193, 0.3);
      }
    `,
  ],
  standalone: true,
  imports: [
    AsyncPipe,
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    RouterLink,
    RouterOutlet,
    SpinnerComponent,
  ],
})
export class ToolbarComponent {
  toolbarBackgroundColor$: Observable<string>;

  constructor(private pageService: PageService) {
    this.toolbarBackgroundColor$ = pageService.toolbarBackgroundColor$;
  }

  toggleSidebarOpen() {
    this.pageService.toggleSidebarOpen();
  }
}
