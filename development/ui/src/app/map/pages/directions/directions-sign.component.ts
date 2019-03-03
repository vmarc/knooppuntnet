import {Component, Input} from '@angular/core';

@Component({
  selector: 'kpn-directions-sign',
  template: `
    <mat-icon *ngIf="sign == -7" svgIcon="keep-left" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == -3" svgIcon="turn-sharp-left" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == -2" svgIcon="turn-left" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == -1" svgIcon="turn-slight-left" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 0" svgIcon="continue" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 1" svgIcon="turn-slight-right" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 2" svgIcon="turn-right" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 3" svgIcon="turn-sharp-right" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 4" svgIcon="finish" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 5" svgIcon="via" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 6" svgIcon="roundabout" class="sign-icon"></mat-icon>
    <mat-icon *ngIf="sign == 7" svgIcon="keep-right" class="sign-icon"></mat-icon>
  `,
  styles: [`
    
    mat-icon {
      width: 40px;
      height: 40px;
    }

    /deep/ .sign-icon > svg {
      fill: #666666;
      stroke: #666666;
    }
    
  `]
})
export class DirectionsSignComponent {
  @Input() sign: number;
}
