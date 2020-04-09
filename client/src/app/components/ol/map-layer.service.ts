import {Injectable} from "@angular/core";
import BaseLayer from "ol/layer/Base";
import {I18nService} from "../../i18n/i18n.service";
import {NodeMapInfo} from "../../kpn/api/common/node-map-info";
import {LocationBoundaryLayer} from "./layers/location-boundary-layer";
import {NodeMarkerLayer} from "./layers/node-marker-layer";
import {OsmLayer} from "./layers/osm-layer";

@Injectable()
export class MapLayerService {

  constructor(private i18nService: I18nService) {
  }

  osmLayer(): BaseLayer {
    return new OsmLayer(this.i18nService).build();
  }

  locationBoundaryLayer(geoJson: string): BaseLayer {
    return new LocationBoundaryLayer(this.i18nService).build(geoJson);
  }

  nodeMarkerLayer(nodeMapInfo: NodeMapInfo): BaseLayer {
    return new NodeMarkerLayer(this.i18nService).build(nodeMapInfo);
  }

}
