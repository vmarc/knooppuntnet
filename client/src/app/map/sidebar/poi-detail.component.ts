import {ChangeDetectorRef, Component} from "@angular/core";
import {AppService} from "../../app.service";
import {MapService} from "../../components/ol/map.service";
import {Tags} from "../../kpn/api/custom/tags";
import {PoiPage} from "../../kpn/api/common/poi-page";
import {PoiService} from "../../services/poi.service";
import {Subscriptions} from "../../util/Subscriptions";
import {InterpretedTags} from "../../components/shared/tags/interpreted-tags";
import {filter, flatMap, tap} from "rxjs/operators";
import {PlannerService} from "../planner.service";
import {PoiClick} from "../../components/ol/domain/poi-click";

@Component({
  selector: "kpn-poi-detail",
  template: `

    <div *ngIf="poiPage != null">

      <h2 *ngIf="poiPage.name">{{poiPage.name}}</h2>
      <h2 *ngIf="!poiPage.name">{{layerName()}}</h2>

      <div *ngIf="poiPage.name" class="item">{{layerName()}}</div>

      <div *ngIf="poiPage.subject" class="item">{{poiPage.subject}}</div>

      <div *ngIf="poiPage.addressLine1 || poiPage.addressLine2" class="item">
        <span *ngIf="poiPage.addressLine1">{{poiPage.addressLine1}}</span><br/>
        <span *ngIf="poiPage.addressLine2">{{poiPage.addressLine2}}</span>
      </div>

      <div *ngIf="poiPage.phone" class="item"><span i18n="@@poi.detail.phone">Phone</span>: {{poiPage.phone}}</div>
      <div *ngIf="poiPage.email" class="item"><span i18n="@@poi.detail.email">E-mail</span>: <a [href]="emailLink()">{{poiPage.email}}</a></div>

      <div *ngIf="poiPage.description" class="item">{{poiPage.description}}</div>
      <div *ngIf="poiPage.wheelchair" class="item"><span i18n="@@poi.detail.wheelchair">Wheelchair</span>: {{poiPage.wheelchair}}</div>

      <div *ngIf="poiPage.website || poiPage.wikidata || poiPage.wikipedia" class="item">
        <a *ngIf="poiPage.website" [href]="poiPage.website" class="external" target="_blank">website</a>
        <a *ngIf="poiPage.wikidata" [href]="poiPage.wikidata" class="external" target="_blank">wikidata</a>
        <a *ngIf="poiPage.wikipedia" [href]="poiPage.wikipedia" class="external" target="_blank">wikipedia</a>
      </div>

      <div *ngIf="poiPage.mainTags && !poiPage.mainTags.tags.isEmpty()" class="item">
        <kpn-tags-table [tags]="mainTags()"></kpn-tags-table>
      </div>

      <div *ngIf="poiPage.extraTags && !poiPage.extraTags.tags.isEmpty()" class="item">
        <kpn-tags-table [tags]="extraTags()"></kpn-tags-table>
      </div>

      <div class="item">
        <kpn-osm-link kind="{{poiClick.poiId.elementType}}" id="{{poiClick.poiId.elementId}}" title="osm"></kpn-osm-link>
        <kpn-josm-link kind="{{poiClick.poiId.elementType}}" id="{{poiClick.poiId.elementId}}" title="edit"></kpn-josm-link>
      </div>

    </div>
  `,
  styles: [`
    .item {
      margin-top: 10px;
      margin-bottom: 10px;
    }
  `]
})
export class PoiDetailComponent {

  poiClick: PoiClick;
  poiPage: PoiPage;
  tags: Tags;
  latitude: string;
  longitude: string;
  private readonly subscriptions = new Subscriptions();

  constructor(private mapService: MapService,
              private appService: AppService,
              private poiService: PoiService,
              private plannerService: PlannerService,
              private cdr: ChangeDetectorRef) {

    this.subscriptions.add(
      mapService.poiClickedObserver.pipe(
        tap(poiClick => this.poiClick = poiClick),
        filter(poiClick => poiClick !== null),
        flatMap(poiClick => this.appService.poi(poiClick.poiId.elementType, poiClick.poiId.elementId))
      ).subscribe(response => {
        this.poiPage = response.result;
        this.latitude = response.result.latitude;
        this.longitude = response.result.longitude;
        this.tags = response.result.mainTags;
        this.cdr.detectChanges();
        this.plannerService.context.overlay.setPosition(this.poiClick.coordinate)
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  layerName(): string {
    const layer = this.poiPage.layers.get(0);
    return this.poiService.name(layer);
  }

  mainTags(): InterpretedTags {
    return InterpretedTags.all(this.poiPage.mainTags);
  }

  extraTags(): InterpretedTags {
    return InterpretedTags.all(this.poiPage.extraTags);
  }

  emailLink(): string {
    return "mailto:" + this.poiPage.email;
  }
}
