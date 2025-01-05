import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Tag } from '@api/custom';
import { PoiAnalysisComponent } from '@app/components/poi';
import { DividerComponent } from '@app/components/shared';
import { DataComponent } from '@app/components/shared/data';
import { OldPageComponent } from '@app/components/shared/page';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { PoiDetailMapComponent } from '@app/ol/components';
import { BaseSidebarComponent } from '@app/shared/base';
import { ActionButtonNodeComponent } from '../../analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '../../analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '../../analysis/components/action/action-button-way.component';
import { RouterService } from '../../shared/services/router.service';
import { PoiDetailPageService } from './poi-detail-page.service';

@Component({
  selector: 'kpn-poi-detail-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <kpn-old-page>
      <!--    <kpn-page-header>-->
      <!--      <span i18n="@@poi-areas.title">Poi</span>-->
      <!--    </kpn-page-header>-->

      @if (service.response(); as response) {
        @if (response.result) {
          <kpn-poi-analysis [poi]="response.result.poiAnalysis" />
          <kpn-divider />
          <kpn-poi-detail-map [poiDetail]="response.result" />
          <kpn-divider />
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
            <kpn-tag-table [tags]="tags(response.result.poi.tags)" />
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
    </kpn-old-page>
  `,
  providers: [PoiDetailPageService, RouterService],
  imports: [
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
    ActionButtonWayComponent,
    BaseSidebarComponent,
    DataComponent,
    DividerComponent,
    OldPageComponent,
    PoiAnalysisComponent,
    PoiDetailMapComponent,
    TagTableComponent,
    TimestampComponent,
  ],
})
export class PoiDetailPageComponent implements OnInit {
  readonly service = inject(PoiDetailPageService);

  ngOnInit(): void {
    this.service.onInit();
  }

  tags(tags: Tag[]): InterpretedTags {
    return InterpretedTags.all(tags);
  }
}
