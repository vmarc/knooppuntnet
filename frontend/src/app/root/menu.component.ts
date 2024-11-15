import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
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

    <div class="kpn-small-spacer-above kpn-spacer-below">
      <mat-divider />
    </div>

    <p class="links-title">Temporary test links:</p>

    <ul>
      <li>
        <a routerLink="analysis/route/6376622">Route 01-02</a>
      </li>
      <li>
        <a routerLink="analysis/route/7973533">LAW9 Pierpad</a>
      </li>
      <li>
        <a routerLink="analysis/hiking/be/België:Oost-Vlaanderen:Aalst/details">Location Aalst</a>
      </li>
      <li>
        <a routerLink="analysis/hiking/be/networks">Subset Belgium</a>
      </li>
    </ul>
    <div class="kpn-spacer-above kpn-spacer-below">
      <mat-divider />
    </div>
  `,
  styles: [
    `
      .links-title {
        margin-left: 1.5em;
      }

      li {
        margin-left: 1.5em;
        margin-top: 1em;
        margin-bottom: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [MatIcon, MatIconButton, MatNavList, MatListItem, RouterLink, MatDivider],
})
export class MenuComponent {}
