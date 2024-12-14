import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatChipOption } from '@angular/material/chips';
import { MatChipListbox } from '@angular/material/chips';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelDescription } from '@angular/material/expansion';
import { MatExpansionPanelTitle } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatLabel } from '@angular/material/select';

@Component({
  selector: 'kpn-tryout-filter-alternative-2',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel togglePosition="before">
      <mat-expansion-panel-header>
        <mat-panel-title>Filter</mat-panel-title>
        <mat-panel-description>123 of 321 routes</mat-panel-description>
      </mat-expansion-panel-header>

      <mat-chip-listbox aria-label="Facts" [multiple]="true">
        <mat-chip-option>RouteOverlappingWays (12)</mat-chip-option>
        <mat-chip-option selected>RouteRedundantNodes (3)</mat-chip-option>
        <mat-chip-option>RouteSuspiciousWays (11)</mat-chip-option>
        <mat-chip-option>RouteTagInvalid (1)</mat-chip-option>
      </mat-chip-listbox>
      <mat-chip-listbox aria-label="Survey">
        <mat-chip-option color="accent">Surveyed (12)</mat-chip-option>
        <mat-chip-option color="accent">Not surveyed (32)</mat-chip-option>
      </mat-chip-listbox>
    </mat-expansion-panel>

    <div class="kpn-small-spacer-above kpn-small-spacer-below">
      <mat-label>query params:</mat-label>
      {{ queryParams }}
    </div>
  `,
  styles: `
    mat-chip {
      margin-right: 0.5em;
    }
  `,
  imports: [
    MatExpansionPanel,
    MatExpansionPanelTitle,
    MatExpansionPanelHeader,
    MatExpansionPanelDescription,
    MatLabel,
    MatChipListbox,
    MatChipOption,
  ],
})
export class TryoutFilterAlternative2Component {
  queryParams = 'facts=RouteOverlappingWays,RouteRedundantNodes&survey=no';
}
