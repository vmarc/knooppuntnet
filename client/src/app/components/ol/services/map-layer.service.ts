import {Injectable} from "@angular/core";
import {List} from "immutable";
import VectorTileLayer from "ol/layer/VectorTile";
import {I18nService} from "../../../i18n/i18n.service";
import {RawNode} from "../../../kpn/api/common/data/raw/raw-node";
import {NodeMoved} from "../../../kpn/api/common/diff/node/node-moved";
import {NetworkAttributes} from "../../../kpn/api/common/network/network-attributes";
import {NodeMapInfo} from "../../../kpn/api/common/node-map-info";
import {GeometryDiff} from "../../../kpn/api/common/route/geometry-diff";
import {RouteMap} from "../../../kpn/api/common/route/route-map";
import {NetworkType} from "../../../kpn/api/custom/network-type";
import {LocationBoundaryLayer} from "../layers/location-boundary-layer";
import {MapLayer} from "../layers/map-layer";
import {NetworkMarkerLayer} from "../layers/network-marker-layer";
import {NetworkVectorTileLayer} from "../layers/network-vector-tile-layer";
import {NodeMarkerLayer} from "../layers/node-marker-layer";
import {NodeMovedLayer} from "../layers/node-moved-layer";
import {OsmLayer} from "../layers/osm-layer";
import {PoiTileLayer} from "../layers/poi-tile-layer";
import {RouteChangeLayers} from "../layers/route-change-layers";
import {RouteLayers} from "../layers/route-layers";
import {RouteNodesLayer} from "../layers/route-nodes-layer";

@Injectable()
export class MapLayerService {

  constructor(private i18nService: I18nService) {
  }

  osmLayer(): MapLayer {
    return new OsmLayer(this.i18nService).build();
  }

  locationBoundaryLayer(geoJson: string): MapLayer {
    return new LocationBoundaryLayer(this.i18nService).build(geoJson);
  }

  nodeMarkerLayer(nodeMapInfo: NodeMapInfo): MapLayer {
    return new NodeMarkerLayer(this.i18nService).build(nodeMapInfo);
  }

  nodeMovedLayer(nodeMoved: NodeMoved): MapLayer {
    return NodeMovedLayer.build(nodeMoved);
  }

  networkLayers(networkTypes: List<NetworkType>): List<MapLayer> {
    return networkTypes.map(networkType => this.networkLayer(networkType));
  }

  networkLayer(networkType: NetworkType): MapLayer {
    const layer = NetworkVectorTileLayer.build(networkType);
    const layerName = this.i18nService.translation("@@map.layer." + networkType.name);
    layer.layer.set("name", layerName);
    return layer;
  }

  routeNodeLayer(nodes: List<RawNode>): MapLayer {
    return new RouteNodesLayer(this.i18nService).build(nodes);
  }

  routeChangeLayers(geometryDiff: GeometryDiff): List<MapLayer> {
    return new RouteChangeLayers(this.i18nService).build(geometryDiff);
  }

  routeLayers(routeMap: RouteMap): List<MapLayer> {
    return new RouteLayers(this.i18nService, routeMap).build();
  }

  networkMarkerLayer(networks: List<NetworkAttributes>): MapLayer {
    return new NetworkMarkerLayer(this.i18nService).build(networks);
  }

  poiTileLayer(): VectorTileLayer {
    return new PoiTileLayer().build();
  }

}
