import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggle } from '@angular/material/button-toggle';
import { MatButtonToggleGroup } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';
import { SpinnerComponent } from '@app/spinner';
import { RootService } from './root.service';

@Component({
  selector: 'kpn-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-toolbar>
      <button mat-button routerLink="/" class="toolbar-app-name">
        <div i18n="@@toolbar.title">knooppuntnet</div>
      </button>
      <kpn-spinner />
      <span class="toolbar-spacer"></span>
      @if (rootService.small()) {
        <mat-button-toggle-group
          #group="matButtonToggleGroup"
          [hideSingleSelectionIndicator]="true"
          [value]="rootService.activePanel()"
          (change)="rootService.setShowTextPanel($event.value)"
        >
          <mat-button-toggle value="text" aria-label="Text">
            <mat-icon>format_align_left</mat-icon>
          </mat-button-toggle>
          <mat-button-toggle value="map" aria-label="Map">
            <mat-icon>map</mat-icon>
          </mat-button-toggle>
        </mat-button-toggle-group>
      }
    </mat-toolbar>
  `,
  styles: `
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
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    RouterLink,
    SpinnerComponent,
    MatButtonToggleGroup,
    MatButtonToggle,
  ],
})
export class ToolbarComponent {
  readonly rootService = inject(RootService);
}
