import { ChangeDetectionStrategy } from '@angular/core';
import { Input } from '@angular/core';
import { Component } from '@angular/core';
import { ApiResponse } from '@api/custom/api-response';

@Component({
  selector: 'kpn-location-response',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div *ngIf="response">
      <kpn-situation-on
        *ngIf="situationOnEnabled"
        [timestamp]="response.situationOn"
      ></kpn-situation-on>
      <div *ngIf="!response.result">
        <p i18n="@@location.location-not-found">Location not found</p>
      </div>
      <div *ngIf="response.result">
        <ng-content></ng-content>
      </div>
    </div>
  `,
})
export class LocationResponseComponent {
  @Input() situationOnEnabled = true;
  @Input() response: ApiResponse<any>;
}
