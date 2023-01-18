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
            ></kpn-timestamp>
          </p>
          <p *ngIf="response.result.poiState.imageLastSeen">
            imageLastSeen=
            <kpn-timestamp
              [timestamp]="response.result.poiState.imageLastSeen"
            ></kpn-timestamp>
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
  `,
  styles: [
    `
      .map-divider {
        margin-top: 1em;
        margin-bottom: 1em;
      }
    `,
  ],
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
