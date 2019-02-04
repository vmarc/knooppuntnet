import {Component} from '@angular/core';

@Component({
  selector: 'kpn-planner-page',
  template: `
    <h1>
      Planner
    </h1>

    <div class="kpn-line">
      <mat-icon svgIcon="rcn"></mat-icon>
      <a routerLink="/planner/map/rcn">
        <span>Cycling</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon svgIcon="rwn"></mat-icon>
      <a routerLink="/planner/map/rwn">
        <span>Hiking</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon svgIcon="rhn"></mat-icon>
      <a routerLink="/planner/map/rhn">
        <span>Horse</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon svgIcon="rmn"></mat-icon>
      <a routerLink="/planner/map/rmn">
        <span>Motorboat</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon svgIcon="rpn"></mat-icon>
      <a routerLink="/planner/map/rpn">
        <span>Canoe</span>
      </a>
    </div>
    <div class="kpn-line">
      <mat-icon svgIcon="rin"></mat-icon>
      <a routerLink="/planner/map/rin">
        <span>Inline skating</span>
      </a>
    </div>
  `
})
export class PlannerPageComponent {

}
