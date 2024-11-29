import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-toolbar-title',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <button mat-button routerLink="/" class="toolbar-app-name">
      <div i18n="@@toolbar.title">routes</div>
    </button>
  `,
  styles: `
    .toolbar-app-name {
      font-size: 20px;
      font-weight: 400;
      letter-spacing: 0.0125em;
    }
  `,
  imports: [MatButtonModule, MatIconModule, MatToolbarModule, RouterLink],
})
export class ToolbarTitleComponent {}
