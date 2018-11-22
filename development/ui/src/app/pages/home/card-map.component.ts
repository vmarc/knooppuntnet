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
          <a routerLink="{{'/analysis/map/rcn'}}">
            <mat-icon>directions_bike</mat-icon>
            <span>Cycling</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/analysis/map/rwn'}}">
            <mat-icon>directions_walk</mat-icon>
            <span>Hiking</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/analysis/map/rhn'}}">
            <mat-icon>directions_bike</mat-icon>
            <span>Horse</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/analysis/map/rmn'}}">
            <mat-icon>directions_bike</mat-icon>
            <span>Motorboat</span>
          </a>
        </div>
        <div>
          <a routerLink="{{'/analysis/map/rpn'}}">
            <mat-icon>directions_bike</mat-icon>
            <span>Canoe</span>
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
