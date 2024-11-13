import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatIcon } from '@angular/material/icon';
import { MatListItem } from '@angular/material/list';
import { MatNavList } from '@angular/material/list';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'kpn-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-nav-list>
      <mat-list-item routerLink="search">
        <button mat-icon-button>
          <mat-icon>search</mat-icon>
        </button>
        <a>Search</a>
      </mat-list-item>

      <mat-list-item routerLink="planner">
        <button mat-icon-button>
          <mat-icon svgIcon="location" />
        </button>
        <a>Plan a route</a>
      </mat-list-item>

      <mat-list-item routerLink="configuration">
        <button mat-icon-button>
          <mat-icon svgIcon="layers" />
        </button>
        <a>Map configuration</a>
      </mat-list-item>

      <mat-list-item routerLink="analysis">
        <button mat-icon-button>
          <mat-icon svgIcon="analysis" />
        </button>
        <a>Analysis</a>
      </mat-list-item>

      <mat-list-item routerLink="monitor">
        <button mat-icon-button>
          <mat-icon svgIcon="output" />
        </button>
        <a>Monitor</a>
      </mat-list-item>
    </mat-nav-list>
  `,
  standalone: true,
  imports: [MatIcon, MatIconButton, MatNavList, MatListItem, RouterLink],
})
export class MenuComponent {}
