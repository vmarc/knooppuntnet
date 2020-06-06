import {ChangeDetectionStrategy} from "@angular/core";
import {OnInit} from "@angular/core";
import {Component} from "@angular/core";
import {Coordinate} from "ol/coordinate";
import {Observable} from "rxjs";
import {filter, flatMap, tap} from "rxjs/operators";
import {AppService} from "../../../../app.service";
import {PoiClick} from "../../../../components/ol/domain/poi-click";
import {MapService} from "../../../../components/ol/services/map.service";
import {InterpretedTags} from "../../../../components/shared/tags/interpreted-tags";
import {PoiAnalysis} from "../../../../kpn/api/common/poi-analysis";
import {PoiPage} from "../../../../kpn/api/common/poi-page";
import {ApiResponse} from "../../../../kpn/api/custom/api-response";
import {Tags} from "../../../../kpn/api/custom/tags";
import {PoiService} from "../../../../services/poi.service";
import {PlannerService} from "../../../planner.service";

@Component({
  selector: "kpn-map-popup-poi",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `

    <div *ngIf="response$ | async">

      <div *ngIf="poi == null" class="item" i18n="@@poi.detail.none">No details available</div>

      <div *ngIf="poi != null">

        <h2 *ngIf="poi.name">{{poi.name}}</h2>
        <h2 *ngIf="!poi.name">{{layerName()}}</h2>

        <div *ngIf="poi.name" class="item">{{layerName()}}</div>

        <div *ngIf="poi.subject" class="item">{{poi.subject}}</div>
        <div *ngIf="poi.denomination" class="item">{{poi.denomination}}</div>
        <div *ngIf="poi.cuisine" class="item">{{poi.cuisine}}</div>

        <div *ngIf="poi.addressLine1 || poi.addressLine2" class="item">
          <span *ngIf="poi.addressLine1">{{poi.addressLine1}}</span><br/>
          <span *ngIf="poi.addressLine2">{{poi.addressLine2}}</span>
        </div>

        <div *ngIf="poi.phone" class="item">
          <span class="kpn-label" i18n="@@poi.detail.phone">Phone</span>
          {{poi.phone}}
        </div>

        <div *ngIf="poi.fax" class="item">
          <span class="kpn-label" i18n="@@poi.detail.fax">Fax</span>
          {{poi.fax}}
        </div>

        <div *ngIf="poi.email" class="item">
          <span class="kpn-label" i18n="@@poi.detail.email">E-mail</span> <a [href]="emailLink()">{{poi.email}}</a>
        </div>
        <div *ngIf="poi.facebook" class="item">
          <a [href]="poi.facebook" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.facebook">Facebook</a>
        </div>

        <div *ngIf="poi.description" class="item">{{poi.description}}</div>
        <div *ngIf="poi.wheelchair" class="item">
          <span class="kpn-label" i18n="@@poi.detail.wheelchair">Wheelchair</span>
          {{poi.wheelchair}}
        </div>

        <div *ngIf="poi.openingHours" class="item">
          <span class="kpn-label" i18n="@@poi.detail.opengingHours">Opening hours</span>
          {{poi.openingHours}}
        </div>
        <div *ngIf="poi.serviceTimes" class="item">
          <span class="kpn-label" i18n="@@poi.detail.serviceHours">Service times</span>
          {{poi.serviceTimes}}
        </div>

        <div *ngIf="poi.image" class="item">
          <a [href]="poi.image" target="_blank" rel="nofollow noreferrer">
            <img [src]="thumbnailImage(poi.image)" width="inherit" height="100px" alt="image" class="image"/>
          </a>
        </div>

        <div *ngIf="poi.imageLink" class="item">
          <a [href]="poi.imageLink" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.image">Image</a>
        </div>

        <div *ngIf="poi.mapillary" class="item">
          <a [href]="poi.mapillary" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.mapillary">Mapillary</a>
        </div>

        <div *ngIf="poi.onroerendErfgoed" class="item">
          <a [href]="poi.onroerendErfgoed" i18n="@@poi.detail.onroerendErfgoed" class="external" target="_blank" rel="nofollow noreferrer">Onroerend Erfgoed</a>
        </div>

        <div *ngIf="poi.website || poi.wikidata || poi.wikipedia" class="item">
          <a *ngIf="poi.website" [href]="poi.website" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.website">Website</a>
          <a *ngIf="poi.wikidata" [href]="poi.wikidata" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.wikidata">Wikidata</a>
          <a *ngIf="poi.wikipedia" [href]="poi.wikipedia" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.wikipedia">Wikipedia</a>
        </div>

        <div *ngIf="poi.molenDatabase" class="item">
          <a [href]="poi.molenDatabase" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.molen-database">Molen database</a>
        </div>

        <div *ngIf="poi.hollandscheMolenDatabase" class="item">
          <a [href]="poi.hollandscheMolenDatabase" class="external" target="_blank" rel="nofollow noreferrer" i18n="@@poi.detail.hollandsche-molen-database">
            Hollandsche Molen database
          </a>
        </div>

        <div *ngIf="poi.mainTags && !poi.mainTags.tags.isEmpty()" class="item">
          <kpn-tags-table [tags]="mainTags()"></kpn-tags-table>
        </div>

        <div *ngIf="poi.extraTags && !poi.extraTags.tags.isEmpty()" class="item">
          <kpn-tags-table [tags]="extraTags()"></kpn-tags-table>
        </div>

        <div class="item">
          <kpn-osm-link [kind]="poiClick.poiId.elementType" [elementId]="poiClick.poiId.elementId.toString()" title="osm"></kpn-osm-link>
          <kpn-josm-link [kind]="poiClick.poiId.elementType" [elementId]="poiClick.poiId.elementId" title="edit"></kpn-josm-link>
        </div>

      </div>
    </div>
  `,
  styles: [`
    .item {
      margin-top: 10px;
      margin-bottom: 10px;
    }

    .image {
      border: 1px solid #cccccc;
    }
  `]
})
export class MapPopupPoiComponent implements OnInit {

  response$: Observable<ApiResponse<PoiPage>>;

  poiClick: PoiClick;
  poiPage: PoiPage;
  poi: PoiAnalysis;
  tags: Tags;

  constructor(private mapService: MapService,
              private appService: AppService,
              private poiService: PoiService,
              private plannerService: PlannerService) {
  }

  ngOnInit(): void {
    this.response$ = this.mapService.poiClicked$.pipe(
      filter(poiClick => poiClick !== null),
      tap(poiClick => this.poiClick = poiClick),
      flatMap(poiClick => this.appService.poi(poiClick.poiId.elementType, poiClick.poiId.elementId)),
      tap(response => {
        if (response.result) {
          this.poiPage = response.result;
          this.poi = response.result.analysis;
          this.tags = response.result.analysis.mainTags;
        } else {
          this.poiPage = null;
          this.poi = null;
          this.tags = null;
        }
        this.openPopup(this.poiClick.coordinate);
      })
    );
  }

  layerName(): string {
    const layer = this.poi.layers.get(0);
    return this.poiService.name(layer);
  }

  mainTags(): InterpretedTags {
    return InterpretedTags.all(this.poi.mainTags);
  }

  extraTags(): InterpretedTags {
    return InterpretedTags.all(this.poi.extraTags);
  }

  emailLink(): string {
    return "mailto:" + this.poi.email;
  }

  thumbnailImage(uncached: string): string {
    if (this.poi.imageThumbnail) {
      return /*window.location.origin +*/ this.poi.imageThumbnail;
    }
    return uncached;
  }

  private openPopup(coordinate: Coordinate): void {
    setTimeout(() => this.plannerService.context.overlay.setPosition(coordinate, -45), 0);
  }
}
