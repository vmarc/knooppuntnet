import {Component} from '@angular/core';

@Component({
  selector: 'kpn-planner-page',
  template: `
    <h1>
      Planner
    </h1>

    <div class="kpn-line">
      <mat-icon>directions_bike</mat-icon>
      <a routerLink="/planner/map/rcn">
        <span>Cycling</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon>directions_walk</mat-icon>
      <a routerLink="/planner/map/rwn">
        <span>Hiking</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon>directions_bike</mat-icon>
      <a routerLink="/planner/map/rhn">
        <span>Horse</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon>directions_bike</mat-icon>
      <a routerLink="/planner/map/rmn">
        <span>Motorboat</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon>directions_bike</mat-icon>
      <a routerLink="/planner/map/rpn">
        <span>Canoe</span>
      </a>
    </div>
  `
})
export class PlannerPageComponent {

}
