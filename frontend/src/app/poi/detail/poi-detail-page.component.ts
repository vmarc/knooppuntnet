import { AsyncPipe } from '@angular/common';
import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { ActivatedRoute } from '@angular/router';
import { PoiDetail } from '@api/common';
import { ApiResponse } from '@api/custom';
import { Tags } from '@api/custom';
import { PoiAnalysisComponent } from '@app/components/poi';
import { DataComponent } from '@app/components/shared/data';
import { PageComponent } from '@app/components/shared/page';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { PoiDetailMapComponent } from '@app/ol/components';
import { ApiService } from '@app/services';
import { BaseSidebarComponent } from '@app/shared/base';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { ActionButtonNodeComponent } from '../../analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../analysis/components/action/action-button-way.component';

@Component({
  selector: 'kpn-poi-detail-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <kpn-page>
      <!--    <kpn-page-header>-->
      <!--      <span i18n="@@poi-areas.title">Poi</span>-->
      <!--    </kpn-page-header>-->

      @if (response$ | async; as response) {
        @if (response.result) {
          <kpn-poi-analysis [poi]="response.result.poiAnalysis" />
          <mat-divider class="map-divider" />
          <kpn-poi-detail-map [poiDetail]="response.result" />
          <mat-divider class="map-divider" />
          <kpn-data title="Identification" i18n-title="@@poi-detail.id">
            <span class="kpn-line">
              <span>{{ response.result.poi._id }}</span>
              @if (response.result.poi.elementType === 'node') {
                <kpn-action-button-node [nodeId]="response.result.poi.elementId" />
              }
              @if (response.result.poi.elementType === 'way') {
                <kpn-action-button-way [wayId]="response.result.poi.elementId" />
              }
              @if (response.result.poi.elementType === 'relation') {
                <kpn-action-button-relation [relationId]="response.result.poi.elementId" />
              }
            </span>
          </kpn-data>
          <kpn-data title="Layer(s)" i18n-title="@@poi-detail.layers">
            @for (layer of response.result.poi.layers; track layer) {
              <p>
                {{ layer }}
              </p>
            }
          </kpn-data>
          <kpn-data title="Tags" i18n-title="@@poi-detail.tags">
            <kpn-tags-table [tags]="tags(response.result.poi.tags)" />
          </kpn-data>
          <kpn-data title="Location" i18n-title="@@poi-detail.location">
            @for (locationName of response.result.poi.location.names; track locationName) {
              <p>
                {{ locationName }}
              </p>
            }
          </kpn-data>
          @if (response.result.poiState.imageLink) {
            <kpn-data title="Image" i18n-title="@@poi-detail.image">
              <p>
                <a
                  class="external"
                  rel="nofollow noreferrer"
                  target="_blank"
                  href="{{ response.result.poiState.imageLink }}"
                >
                  {{ response.result.poiState.imageLink }}
                </a>
              </p>
              @if (response.result.poiState.imageStatus) {
                <p>imageStatus={{ response.result.poiState.imageStatus }}</p>
              }
              @if (response.result.poiState.imageStatusDetail) {
                <p>imageStatusDetail={{ response.result.poiState.imageStatusDetail }}</p>
              }
              @if (response.result.poiState.imageFirstSeen) {
                <p>
                  imageFirstSeen=
                  <kpn-timestamp [timestamp]="response.result.poiState.imageFirstSeen" />
                </p>
              }
              @if (response.result.poiState.imageLastSeen) {
                <p>
                  imageLastSeen=
                  <kpn-timestamp [timestamp]="response.result.poiState.imageLastSeen" />
                </p>
              }
            </kpn-data>
          }
          <p></p>
          <kpn-data title="Tiles" i18n-title="@@poi-detail.tiles">
            @for (tile of response.result.poi.tiles; track tile) {
              <p>
                {{ tile }}
              </p>
            }
          </kpn-data>
        }
      }
      <kpn-base-sidebar sidebar />
    </kpn-page>
  `,
  styles: `
    .map-divider {
      margin-top: 1em;
      margin-bottom: 1em;
    }
  `,
  standalone: true,
  imports: [
    AsyncPipe,
    BaseSidebarComponent,
    DataComponent,
    MatDividerModule,
    PageComponent,
    PoiAnalysisComponent,
    PoiDetailMapComponent,
    TagsTableComponent,
    TimestampComponent,
    ActionButtonWayComponent,
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
  ],
})
export class PoiDetailPageComponent implements OnInit {
  private readonly activatedRoute = inject(ActivatedRoute);
  private readonly apiService = inject(ApiService);

  protected response$: Observable<ApiResponse<PoiDetail>>;

  ngOnInit(): void {
    this.response$ = this.activatedRoute.params.pipe(
      mergeMap((params) => {
        const elementType = params['elementType'];
        const elementId = +params['elementId'];
        return this.apiService.poiDetail(elementType, elementId);
      })
    );
  }

  tags(tags: Tags): InterpretedTags {
    return InterpretedTags.all(tags);
  }
}
