import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ApiResponse } from '@api/custom/api-response';
import { Observable } from 'rxjs';
import { AppService } from '@app/app.service';

@Component({
  selector: 'kpn-poi-areas-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <kpn-page-header>
      <span i18n="@@poi-areas.title">Point of interest areas</span>
    </kpn-page-header>
    <p i18n="@@poi-areas.comment">
      This map shows the bounding boxes that are used to determine where to
      collect the point of interest information.
    </p>

    <div *ngIf="response$ | async as response">
      <kpn-poi-map [geoJson]="response.result" />
    </div>
  `,
})
export class PoiAreasPageComponent implements OnInit {
  response$: Observable<ApiResponse<string>>;

  constructor(private appService: AppService) {}

  ngOnInit(): void {
    this.response$ = this.appService.poiAreas();
  }
}
