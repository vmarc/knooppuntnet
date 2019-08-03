import {Component} from "@angular/core";
import {AppService} from "../../app.service";
import {MapService, PoiId} from "../../components/ol/map.service";
import {Tags} from "../../kpn/shared/data/tags";
import {PoiPage} from "../../kpn/shared/poi-page";
import {PoiService} from "../../services/poi.service";
import {Subscriptions} from "../../util/Subscriptions";
import {InterpretedTags} from "../../components/shared/tags/interpreted-tags";
import {filter, flatMap, tap} from "rxjs/operators";

@Component({
  selector: "kpn-poi-detail",
  template: `

    <div *ngIf="poiId == null">
      Click on point of interest icons in the map to see detail.
    </div>

    <kpn-poi-names></kpn-poi-names>

    <div *ngIf="poiPage != null">

      <h2 *ngIf="poiPage.name">{{poiPage.name}}</h2>
      <h2 *ngIf="!poiPage.name">{{layerName()}}</h2>

      <p *ngIf="poiPage.name">{{layerName()}}</p>

      <div *ngIf="poiPage.subject">{{poiPage.subject}}</div>

      <p *ngIf="poiPage.addressLine1 || poiPage.addressLine2">
        <span *ngIf="poiPage.addressLine1">{{poiPage.addressLine1}}</span><br/>
        <span *ngIf="poiPage.addressLine2">{{poiPage.addressLine2}}</span>
      </p>


      <div *ngIf="poiPage.description">{{poiPage.description}}</div>
      <div *ngIf="poiPage.website"><a [href]="poiPage.website" class="external" target="_blank">website</a></div>
      <div *ngIf="poiPage.image"><a [href]="poiPage.image" class="external" target="_blank">image</a></div>

      <kpn-osm-link kind="{{poiId.elementType}}" id="{{poiId.elementId}}" title="osm"></kpn-osm-link>
      <kpn-josm-link kind="{{poiId.elementType}}" id="{{poiId.elementId}}" title="edit"></kpn-josm-link>

      <div *ngIf="poiPage.mainTags && !poiPage.mainTags.tags.isEmpty()">
        <kpn-tags-table [tags]="mainTags()"></kpn-tags-table>
      </div>

      <div *ngIf="poiPage.extraTags && !poiPage.extraTags.tags.isEmpty()">
        <kpn-tags-table [tags]="extraTags()"></kpn-tags-table>
      </div>

      <br/>
      <br/>
      <br/>
      latitude {{latitude}}
      <br/>
      longitude {{longitude}}
      <br/>

    </div>
  `,
  styles: [`
    :host {
      display: block;
      padding: 20px;
    }
  `]
})
export class PoiDetailComponent {

  private readonly subscriptions = new Subscriptions();

  poiId: PoiId;
  poiPage: PoiPage;
  tags: Tags;
  latitude: string;
  longitude: string;

  constructor(private mapService: MapService,
              private appService: AppService,
              private poiService: PoiService) {

    this.subscriptions.add(
      mapService.poiClickedObserver.pipe(
        tap(poiId => this.poiId = poiId),
        filter(poiId => poiId !== null),
        flatMap(poiId => this.appService.poi(poiId.elementType, poiId.elementId))
      ).subscribe(response => {
        this.poiPage = response.result;
        this.latitude = response.result.latitude;
        this.longitude = response.result.longitude;
        this.tags = response.result.mainTags;
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

}
