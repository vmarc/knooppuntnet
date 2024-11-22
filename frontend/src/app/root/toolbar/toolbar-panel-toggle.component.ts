import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatButtonToggle } from '@angular/material/button-toggle';
import { MatButtonToggleGroup } from '@angular/material/button-toggle';
import { MatIconModule } from '@angular/material/icon';
import { MatToolbarModule } from '@angular/material/toolbar';
import { StateService } from '@app/state';

@Component({
  selector: 'kpn-toolbar-panel-toggle',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-button-toggle-group
      #group="matButtonToggleGroup"
      [hideSingleSelectionIndicator]="true"
      [value]="activePanel()"
      (change)="updateActivePanel($event.value)"
    >
      <mat-button-toggle value="text" aria-label="Text">
        <mat-icon>format_align_left</mat-icon>
      </mat-button-toggle>
      <mat-button-toggle value="map" aria-label="Map">
        <mat-icon>map</mat-icon>
      </mat-button-toggle>
    </mat-button-toggle-group>
  `,
  standalone: true,
  imports: [
    MatButtonModule,
    MatButtonToggle,
    MatButtonToggleGroup,
    MatIconModule,
    MatToolbarModule,
  ],
})
export class ToolbarPanelToggleComponent {
  private readonly state = inject(StateService);
  readonly activePanel = this.state.page.activePanel;

  updateActivePanel(value: string): void {
    this.state.page.updateActivePanel(value);
  }
}
