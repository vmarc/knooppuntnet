import {Component} from '@angular/core';

@Component({
  selector: 'kpn-card-map',
  template: `
    <mat-card>
      <mat-card-header>
        <mat-card-title>Maps</mat-card-title>
        <mat-card-subtitle>Plan a trip, or just look at the map.</mat-card-subtitle>
      </mat-card-header>
      <mat-divider></mat-divider>
      <mat-card-content>
        <div>
          <a routerLink="{{'/planner/map/rcn'}}">
            <mat-icon svgIcon="rcn"></mat-icon>
            <span>Cycling</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/planner/map/rwn'}}">
            <mat-icon svgIcon="rwn"></mat-icon>
            <span>Hiking</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/planner/map/rhn'}}">
            <mat-icon svgIcon="rhn"></mat-icon>
            <span>Horse</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/planner/map/rmn'}}">
            <mat-icon svgIcon="rmn"></mat-icon>
            <span>Motorboat</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/planner/map/rpn'}}">
            <mat-icon svgIcon="rcn"></mat-icon>
            <span>Canoe</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/planner/map/rpn'}}">
            <mat-icon svgIcon="rin"></mat-icon>
            <span>Inline skates</span>
          </a>
        </div>
      </mat-card-content>
    </mat-card>
  `,
  styles: [`
    mat-card-content {
      padding-left: 15px;
      padding-top: 15px;
    }

    a {
      display: inline-flex;
      line-height: 20px;
      flex-direction: row;
      align-items: center;
      margin-top: 5px;
      margin-bottom: 5px;
      font-size: 16px;
    }

    mat-icon {
      margin-right: 10px;
      color: black;
    }
  `]
})
export class CardMapComponent {
}
