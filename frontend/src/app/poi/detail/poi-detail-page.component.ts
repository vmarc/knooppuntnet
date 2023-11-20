import { AsyncPipe, NgFor, NgIf } from '@angular/common';
import { ChangeDetectionStrategy } from '@angular/core';
import { Component } from '@angular/core';
import { OnInit } from '@angular/core';
import { MatDividerModule } from '@angular/material/divider';
import { ActivatedRoute } from '@angular/router';
import { PoiDetail } from '@api/common';
import { ApiResponse } from '@api/custom';
import { Tags } from '@api/custom';
import { BaseSidebarComponent } from '@app/shared/base';
import { PoiDetailMapComponent } from '@app/ol/components';
import { PoiAnalysisComponent } from '@app/components/poi';
import { DataComponent } from '@app/components/shared/data';
import { JosmNodeComponent } from '@app/components/shared/link';
import { JosmRelationComponent } from '@app/components/shared/link';
import { JosmWayComponent } from '@app/components/shared/link';
import { OsmLinkNodeComponent } from '@app/components/shared/link';
import { OsmLinkRelationComponent } from '@app/components/shared/link';
import { OsmLinkWayComponent } from '@app/components/shared/link';
import { PageComponent } from '@app/components/shared/page';
import { InterpretedTags } from '@app/components/shared/tags';
import { TagsTableComponent } from '@app/components/shared/tags';
import { TimestampComponent } from '@app/components/shared/timestamp';
import { ApiService } from '@app/services';
import { Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

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

      <div *ngIf="response$ | async as response">
        <div *ngIf="response.result">
          <kpn-poi-analysis [poi]="response.result.poiAnalysis" />

          <mat-divider class="map-divider" />
          <kpn-poi-detail-map [poiDetail]="response.result" />
          <mat-divider class="map-divider" />

          <kpn-data title="Identification" i18n-title="@@poi-detail.id">
            <span class="kpn-line">
              <span>{{ response.result.poi._id }}</span>

              <kpn-osm-link-node
                *ngIf="response.result.poi.elementType === 'node'"
                [nodeId]="response.result.poi.elementId"
              />
              <kpn-osm-link-way
                *ngIf="response.result.poi.elementType === 'way'"
                [wayId]="response.result.poi.elementId"
              />
              <kpn-osm-link-relation
                *ngIf="response.result.poi.elementType === 'relation'"
                [relationId]="response.result.poi.elementId"
              />

              <kpn-josm-node
                *ngIf="response.result.poi.elementType === 'node'"
                [nodeId]="response.result.poi.elementId"
              />
              <kpn-josm-way
                *ngIf="response.result.poi.elementType === 'way'"
                [wayId]="response.result.poi.elementId"
              />
              <kpn-josm-relation
                *ngIf="response.result.poi.elementType === 'relation'"
                [relationId]="response.result.poi.elementId"
              />
            </span>
          </kpn-data>

          <kpn-data title="Layer(s)" i18n-title="@@poi-detail.layers">
            <p *ngFor="let layer of response.result.poi.layers">
              {{ layer }}
            </p>
          </kpn-data>

          <kpn-data title="Tags" i18n-title="@@poi-detail.tags">
            <kpn-tags-table [tags]="tags(response.result.poi.tags)" />
          </kpn-data>

          <kpn-data title="Location" i18n-title="@@poi-detail.location">
            <p *ngFor="let locationName of response.result.poi.location.names">
              {{ locationName }}
            </p>
          </kpn-data>

          <kpn-data
            *ngIf="response.result.poiState.imageLink"
            title="Image"
            i18n-title="@@poi-detail.image"
          >
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
              />
            </p>
            <p *ngIf="response.result.poiState.imageLastSeen">
              imageLastSeen=
              <kpn-timestamp
                [timestamp]="response.result.poiState.imageLastSeen"
              />
            </p>
          </kpn-data>

          <p></p>

          <kpn-data title="Tiles" i18n-title="@@poi-detail.tiles">
            <p *ngFor="let tile of response.result.poi.tiles">
              {{ tile }}
            </p>
          </kpn-data>
        </div>
      </div>
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
    JosmNodeComponent,
    JosmRelationComponent,
    JosmWayComponent,
    MatDividerModule,
    NgFor,
    NgIf,
    OsmLinkNodeComponent,
    OsmLinkRelationComponent,
    OsmLinkWayComponent,
    PageComponent,
    PoiAnalysisComponent,
    PoiDetailMapComponent,
    TagsTableComponent,
    TimestampComponent,
  ],
})
export class PoiDetailPageComponent implements OnInit {
  response$: Observable<ApiResponse<PoiDetail>>;

  constructor(
    private activatedRoute: ActivatedRoute,
    private apiService: ApiService
  ) {}

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
