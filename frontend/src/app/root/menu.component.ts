import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { MatButton } from '@angular/material/button';
import { MatDivider } from '@angular/material/divider';
import { MatNavList } from '@angular/material/list';
import { RouterLink } from '@angular/router';
import { MapService } from '../map/map.service';
import { MenuItemComponent } from './menu-item.component';

@Component({
  selector: 'kpn-menu',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <mat-nav-list>
      <kpn-menu-item label="Search" icon="search" link="search" />
      <kpn-menu-item label="Plan a route" icon="explore" link="planner" />
      <kpn-menu-item label="Map configuration" icon="layers" link="configuration" />
      <kpn-menu-item label="Settings" icon="settings" link="configuration" />
      <kpn-menu-item label="Analysis" icon="stethoscope" link="analysis" />
      <kpn-menu-item label="Monitor" icon="cardiology" link="monitor" />
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
        <a routerLink="analysis/route/16068584">Wandelpad Calmeyn</a>
      </li>
      <li>
        <a routerLink="analysis/hiking/be/België:Oost-Vlaanderen:Aalst/details">Location Aalst</a>
      </li>
      <li>
        <a routerLink="analysis/hiking/be/networks">Subset Belgium</a>
      </li>
    </ul>

    <div class="kpn-small-spacer-above kpn-spacer-below">
      <mat-divider />
    </div>

    <div class="action">
      <button mat-stroked-button (click)="selectRoute(8464197)">LAW11</button>
    </div>
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(16068584)">Wandelpad Calmeyn</button>
    </div>
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(16075296)">Het Stroperspad</button>
    </div>
    <div class="action">
      <button mat-stroked-button (click)="selectRoute(null)">None</button>
    </div>

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

      .action {
        margin: 1em;
      }
    `,
  ],
  standalone: true,
  imports: [MatNavList, RouterLink, MatDivider, MenuItemComponent, MatButton],
})
export class MenuComponent {
  private readonly mapService = inject(MapService);

  selectRoute(routeId: number) {
    console.log('selectedRoute: ' + routeId);
    this.mapService.selectRoute(routeId);
  }
}
