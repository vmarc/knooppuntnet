import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { input } from '@angular/core';
import { ApiResponse } from '@api/custom';
import { SituationOnComponent } from '@app/components/shared/timestamp';

@Component({
  selector: 'kpn-location-response',
  changeDetection: ChangeDetectionStrategy.OnPush,

  template: `
    @if (response()) {
      @if (situationOnEnabled()) {
        <kpn-situation-on [timestamp]="response().situationOn" />
      }
      @if (!response().result) {
        <p i18n="@@location.location-not-found">Location not found</p>
      } @else {
        <ng-content />
      }
    }
  `,
  standalone: true,
  imports: [SituationOnComponent],
})
export class LocationResponseComponent {
  situationOnEnabled = input(true);
  response = input<ApiResponse<any> | undefined>();
}
