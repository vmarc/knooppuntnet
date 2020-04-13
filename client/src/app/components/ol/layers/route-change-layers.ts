import {List} from "immutable";
import {Color} from "ol/color";
import Feature from "ol/Feature";
import LineString from "ol/geom/LineString";
import BaseLayer from "ol/layer/Base";
import VectorLayer from "ol/layer/Vector";
import VectorSource from "ol/source/Vector";
import {Stroke} from "ol/style";
import {Style} from "ol/style";
import {I18nService} from "../../../i18n/i18n.service";
import {GeometryDiff} from "../../../kpn/api/common/route/geometry-diff";
import {PointSegment} from "../../../kpn/api/common/route/point-segment";
import {Util} from "../../shared/util";

export class RouteChangeLayers {

  constructor(private i18nService: I18nService) {
  }

  build(geometryDiff: GeometryDiff): List<BaseLayer> {

    const unchanged = this.segmentLayer("@@map.layer.unchanged", geometryDiff.common, 5, [0, 0, 255]);
    const added = this.segmentLayer("@@map.layer.added", geometryDiff.after, 12, [0, 255, 0]);
    const deleted = this.segmentLayer("@@map.layer.deleted", geometryDiff.before, 3, [255, 0, 0]);

    return List([
      unchanged,
      added,
      deleted
    ]).filter(layer => layer !== null);
  }

  private segmentLayer(name: string, segments: List<PointSegment>, width: number, color: Color): BaseLayer {
    if (segments.isEmpty()) {
      return null;
    }

    const style = new Style({
      stroke: new Stroke({
        color: color,
        width: width
      })
    });

    const source = new VectorSource();
    segments.forEach(segment => {
      const p1 = Util.latLonToCoordinate(segment.p1);
      const p2 = Util.latLonToCoordinate(segment.p2);
      const feature = new Feature(new LineString([p1, p2]));
      feature.setStyle(style);
      source.addFeature(feature);
    });

    const layer = new VectorLayer({
      source: source
    });
    layer.set("name", this.i18nService.translation(name));
    return layer;
  }

}
