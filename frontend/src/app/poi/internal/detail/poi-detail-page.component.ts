import { inject } from '@angular/core';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { Tag } from '@api/custom/tag';
import { PoiDetailMapComponent } from '@app/ol/components/poi-detail-map.component';
import { DataComponent } from '@app/shared/components/data/data.component';
import { DividerComponent } from '@app/shared/components/divider.component';
import { PageComponent } from '@app/shared/components/page/page.component';
import { PoiAnalysisComponent } from '@app/shared/components/poi/poi-analysis.component';
import { InterpretedTags } from '@app/shared/components/tags/interpreted-tags';
import { TagTableComponent } from '@app/shared/components/tags/tag-table.component';
import { TimestampComponent } from '@app/shared/components/timestamp/timestamp.component';
import { ActionButtonNodeComponent } from '@app/analysis/components/action/action-button-node.component';
import { ActionButtonRelationComponent } from '@app/analysis/components/action/action-button-relation.component';
import { ActionButtonWayComponent } from '@app/analysis/components/action/action-button-way.component';
import { RouterService } from '@app/shared/services/router.service';
import { PoiDetailPageService } from './poi-detail-page.service';

@Component({
  selector: 'ui-poi-detail-page',
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <!-- work-in-progress -->
    <!-- eslint-disable @angular-eslint/template/i18n -->

    <ui-page>
      <!--    <ui-page-header>-->
      <!--      <span i18n="@@poi-areas.title">Poi</span>-->
      <!--    </ui-page-header>-->

      @if (service.response(); as response) {
        @if (response.result) {
          <ui-poi-analysis [poi]="response.result.poiAnalysis" />
          <ui-divider />
          <ui-poi-detail-map [poiDetail]="response.result" />
          <ui-divider />
          <ui-data title="Identification" i18n-title="@@poi-detail.id">
            <span class="kpn-line">
              <span>{{ response.result.poi._id }}</span>
              @if (response.result.poi.elementType === 'node') {
                <ui-action-button-node [nodeId]="response.result.poi.elementId" />
              }
              @if (response.result.poi.elementType === 'way') {
                <ui-action-button-way [wayId]="response.result.poi.elementId" />
              }
              @if (response.result.poi.elementType === 'relation') {
                <ui-action-button-relation [relationId]="response.result.poi.elementId" />
              }
            </span>
          </ui-data>
          <ui-data title="Layer(s)" i18n-title="@@poi-detail.layers">
            @for (layer of response.result.poi.layers; track layer) {
              <p>
                {{ layer }}
              </p>
            }
          </ui-data>
          <ui-data title="Tags" i18n-title="@@poi-detail.tags">
            <ui-tag-table [tags]="tags(response.result.poi.tags)" />
          </ui-data>
          <ui-data title="Location" i18n-title="@@poi-detail.location">
            @for (locationName of response.result.poi.location.names; track locationName) {
              <p>
                {{ locationName }}
              </p>
            }
          </ui-data>
          @if (response.result.poiState.imageLink) {
            <ui-data title="Image" i18n-title="@@poi-detail.image">
              <p>
                <a
                  class="external"
                  rel="nofollow noreferrer"
                  target="_blank"
                  [href]="response.result.poiState.imageLink"
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
                  <ui-timestamp [timestamp]="response.result.poiState.imageFirstSeen" />
                </p>
              }
              @if (response.result.poiState.imageLastSeen) {
                <p>
                  imageLastSeen=
                  <ui-timestamp [timestamp]="response.result.poiState.imageLastSeen" />
                </p>
              }
            </ui-data>
          }
          <p></p>
          <ui-data title="Tiles" i18n-title="@@poi-detail.tiles">
            @for (tile of response.result.poi.tiles; track tile) {
              <p>
                {{ tile }}
              </p>
            }
          </ui-data>
        }
      }
    </ui-page>
  `,
  providers: [PoiDetailPageService, RouterService],
  imports: [
    ActionButtonNodeComponent,
    ActionButtonRelationComponent,
    ActionButtonWayComponent,
    DataComponent,
    DividerComponent,
    PageComponent,
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
