import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatOption } from '@angular/material/autocomplete';
import { MatSelect } from '@angular/material/select';

@Component({
  selector: 'kpn-tryout-tabs-drop-down',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div style="width: 10em" class="kpn-spacer-above kpn-small-spacer-below">
      <mat-select value="1">
        <mat-option value="1">
          <span class="menu-link">Nodes</span>&nbsp;<span class="menu-count">(123)</span>
        </mat-option>
        <mat-option value="2">
          <span class="menu-link">Routes</span>&nbsp;<span class="menu-count">(123)</span>
        </mat-option>
        <mat-option value="3">
          <span class="menu-link">Facts</span>&nbsp;<span class="menu-count">(123)</span>
        </mat-option>
        <mat-option value="4">
          <span class="menu-link">Map</span>
        </mat-option>
        <mat-option value="5">
          <span class="menu-link">Changes</span>
        </mat-option>
      </mat-select>
    </div>
  `,
  styles: `
    .menu-link {
      color: blue;
      font-size: 16px;
      font-family: Roboto, Helvetica, Arial, sans-serif;
    }

    .menu-count {
      color: rgb(128, 128, 128);
    }
  `,
  standalone: true,
  imports: [MatSelect, MatOption],
})
export class TryoutTabsDropDownComponent {}
