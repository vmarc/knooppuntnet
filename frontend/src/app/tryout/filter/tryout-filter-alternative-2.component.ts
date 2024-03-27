import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatOption } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
import { MatChipOption } from '@angular/material/chips';
import { MatChipListbox } from '@angular/material/chips';
import { MatChipRemove } from '@angular/material/chips';
import { MatChip } from '@angular/material/chips';
import { MatExpansionPanelHeader } from '@angular/material/expansion';
import { MatExpansionPanelDescription } from '@angular/material/expansion';
import { MatExpansionPanelTitle } from '@angular/material/expansion';
import { MatExpansionPanel } from '@angular/material/expansion';
import { MatIcon } from '@angular/material/icon';
import { MatMenuTrigger } from '@angular/material/menu';
import { MatMenuItem } from '@angular/material/menu';
import { MatMenu } from '@angular/material/menu';
import { MatLabel } from '@angular/material/select';
import { MatSelect } from '@angular/material/select';
import { MatTabLabel } from '@angular/material/tabs';
import { MatTabGroup } from '@angular/material/tabs';
import { MatTab } from '@angular/material/tabs';

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
        <mat-chip-option>RouteReversed (7)</mat-chip-option>
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

    .menu-count {
      padding-left: 1em;
      color: darkgray;
    }
  `,
  standalone: true,
  imports: [
    MatSelect,
    MatOption,
    MatTabGroup,
    MatTab,
    MatTabLabel,
    MatButton,
    MatMenuTrigger,
    MatMenu,
    MatMenuItem,
    MatIcon,
    MatChip,
    MatChipRemove,
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
