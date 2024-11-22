import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { SpinnerComponent } from '@app/spinner';
import { StateService } from '@app/state';
import { ToolbarPanelToggleComponent } from './toolbar-panel-toggle.component';
import { ToolbarRouteTypeMenuComponent } from './toolbar-route-type-menu.component';
import { ToolbarTitleComponent } from './toolbar-title.component';

@Component({
  selector: 'kpn-toolbar',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-toolbar>
      <kpn-toolbar-route-type-menu />
      <kpn-toolbar-title />
      <kpn-spinner />
      <span class="toolbar-spacer"></span>
      @if (small()) {
        <kpn-toolbar-panel-toggle />
      }
    </mat-toolbar>
  `,
  styles: `
    mat-toolbar {
      padding: 16px 16px 16px 6px;
    }

    .toolbar-spacer {
      flex: 1 1 auto;
    }
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatIconModule,
    MatToolbarModule,
    SpinnerComponent,
    ToolbarPanelToggleComponent,
    ToolbarRouteTypeMenuComponent,
    ToolbarTitleComponent,
  ],
})
export class ToolbarComponent {
  private readonly state = inject(StateService);
  readonly small = this.state.page.small;
}
