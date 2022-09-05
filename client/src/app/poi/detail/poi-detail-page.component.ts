import { ChangeDetectionStrategy } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PoiDetail } from '@api/common/poi-detail';
import { ApiResponse } from '@api/custom/api-response';
import { Tags } from '@api/custom/tags';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { AppService } from '../../app.service';
import { InterpretedTags } from '../../components/shared/tags/interpreted-tags';

@Component({
  selector: 'kpn-poi-detail-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <!--    <kpn-page-header>-->
    <!--      <span i18n="@@poi-areas.title">Poi</span>-->
    <!--    </kpn-page-header>-->

    <div *ngIf="response$ | async as response">
      <div *ngIf="response.result">
        <kpn-poi-analysis
          [poi]="response.result.poiAnalysis"
        ></kpn-poi-analysis>

        <mat-divider></mat-divider>

        <p>
          {{ response.result.poi.elementType }}
          {{ response.result.poi.elementId }}
        </p>
        <p>
          latitude={{ response.result.poi.latitude }} longitude={{
            response.result.poi.longitude
          }}
        </p>

        <p *ngFor="let layer of response.result.poi.layers">
          layers={{ layer }}
        </p>

        <p *ngIf="response.result.poiState.imageLink">
          imageLink=<a
            class="external"
            rel="nofollow noreferrer"
            target="_blank"
            href="{{ response.result.poiState.imageLink }}"
            >{{ response.result.poiState.imageLink }}</a
          >
        </p>
        <p *ngIf="response.result.poiState.imageStatus">
          imageStatus={{ response.result.poiState.imageStatus }}
        </p>
        <p *ngIf="response.result.poiState.imageStatusDetail">
          imageStatusDetail={{ response.result.poiState.imageStatusDetail }}
        </p>
        <p *ngIf="response.result.poiState.imageFirstSeen">
          imageFirstSeen=
          <kpn-timestamp
            [timestamp]="response.result.poiState.imageFirstSeen"
          ></kpn-timestamp>
        </p>
        <p *ngIf="response.result.poiState.imageLastSeen">
          imageLastSeen=
          <kpn-timestamp
            [timestamp]="response.result.poiState.imageLastSeen"
          ></kpn-timestamp>
        </p>

        <p>tiles={{ response.result.poi.tiles }}</p>

        <div class="kpn-detail">
          <kpn-tags-table
            [tags]="tags(response.result.poi.tags)"
          ></kpn-tags-table>
        </div>
      </div>
    </div>
  `,
})
export class PoiDetailPageComponent implements OnInit {
  response$: Observable<ApiResponse<PoiDetail>>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private appService: AppService
  ) {}

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      mergeMap((params) => {
        const elementType = params['elementType'];
        const elementId = +params['elementId'];
        return this.appService.poiDetail(elementType, elementId);
      })
    );
  }

  tags(tags: Tags): InterpretedTags {
    return InterpretedTags.all(tags);
  }
}
