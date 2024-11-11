import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';

@Component({
  selector: 'kpn-tryout-panels-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div>
      <div [class]="menuItemClasses('analysis')">
        <button mat-icon-button (click)="select('analysis')">
          <mat-icon svgIcon="analysis" />
        </button>
      </div>
      <div [class]="menuItemClasses('layers')">
        <button mat-icon-button (click)="select('layers')">
          <mat-icon svgIcon="layers" />
        </button>
      </div>
      <div [class]="menuItemClasses('link')">
        <button mat-icon-button (click)="select('link')">
          <mat-icon svgIcon="link" />
        </button>
      </div>
      <div [class]="menuItemClasses('changes')">
        <button mat-icon-button (click)="select('changes')">
          <mat-icon svgIcon="changes" />
        </button>
      </div>
      <div [class]="menuItemClasses('external-link')">
        <button mat-icon-button (click)="select('external-link')">
          <mat-icon svgIcon="external-link" />
        </button>
      </div>
      <div [class]="menuItemClasses('output')">
        <button mat-icon-button (click)="select('output')">
          <mat-icon svgIcon="output" />
        </button>
      </div>
      <div [class]="menuItemClasses('overview')">
        <button mat-icon-button (click)="select('overview')">
          <mat-icon svgIcon="overview" />
        </button>
      </div>
    </div>
  `,
  styles: `
    .icon-menu-item {
      display: block;
    }

    .icon-menu-item-selected {
      border-top: 1px solid lightgray;
      border-bottom: 1px solid lightgray;
      background-color: rgb(250, 250, 250);
    }

    .icon-menu-item-unselected {
      border-right: 1px solid lightgray;
    }
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton],
})
export class TryoutPanelsMenuComponent {
  selected = 'analysis';

  menuItemClasses(name: string): string {
    if (this.selected === name) {
      return 'icon-menu-item icon-menu-item-selected';
    }
    return 'icon-menu-item icon-menu-item-unselected';
  }

  select(name: string): void {
    this.selected = name;
  }
}
