import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatOption } from '@angular/material/autocomplete';
import { MatButton } from '@angular/material/button';
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
import { PageComponent } from '@app/components/shared/page';

@Component({
  selector: 'kpn-tryout-filter-alternative-1',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-expansion-panel togglePosition="before">
      <mat-expansion-panel-header>
        <mat-panel-title>Filter</mat-panel-title>
        <mat-panel-description>Alternative 1</mat-panel-description>
      </mat-expansion-panel-header>

      <div class="chips">
        <button mat-button [matMenuTriggerFor]="filterMenu">
          <div class="kpn-align-center">
            <span>Add</span>
            <mat-icon svgIcon="menu-down-arrow" />
          </div>
        </button>

        @for (filterName of filterNames; track filterName) {
          <mat-chip (removed)="remove(filterName)">
            {{ filterName }}
            <button matChipRemove [attr.aria-label]="'remove ' + filterName">
              <mat-icon>cancel</mat-icon>
            </button>
          </mat-chip>
        }
      </div>
      <mat-menu #filterMenu="matMenu">
        <button mat-menu-item [matMenuTriggerFor]="factMenu">Fact</button>
        <button mat-menu-item [matMenuTriggerFor]="surveyMenu">Survey</button>
      </mat-menu>

      <mat-menu #factMenu="matMenu">
        <button mat-menu-item>
          <span>RouteOverlappingWays</span>
          <span class="menu-count">(12)</span>
        </button>
        <button mat-menu-item>
          <span>RouteRedundantNodes</span>
          <span class="menu-count"> (3)</span>
        </button>
        <button mat-menu-item>
          <span>RouteReversed</span>
          <span class="menu-count"> (7)</span>
        </button>
        <button mat-menu-item>
          <span>RouteSuspiciousWays</span>
          <span class="menu-count"> (11)</span>
        </button>
        <button mat-menu-item>
          <span>RouteTagInvalid</span>
          <span class="menu-count"> (1)</span>
        </button>
      </mat-menu>

      <mat-menu #surveyMenu="matMenu">
        <button mat-menu-item>
          <span>Surveyed</span>
          <span class="menu-count"> (11)</span>
        </button>
        <button mat-menu-item>
          <span>Not surveyed</span>
          <span class="menu-count"> (31)</span>
        </button>
      </mat-menu>
    </mat-expansion-panel>

    <div class="kpn-small-spacer-above kpn-small-spacer-below">
      <mat-label>quiry params</mat-label>
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
  ],
})
export class TryoutFilterAlternative1Component {
  readonly filterNames = [
    'RouteOverlappingWays (12)',
    'RouteRedundantNodes (3)',
    'RouteReversed (7)',
    'RouteSuspiciousWays (11)',
    'RouteTagInvalid (1)',
  ];

  queryParams = 'facts=RouteOverlappingWays,RouteRedundantNodes&survey=no';

  remove(filterName: string) {
    console.log('remove filter');
  }
}
