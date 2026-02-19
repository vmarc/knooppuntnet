import { LatLonImpl } from '@api/common/lat-lon-impl';
import { PlanNode } from '@api/common/planner/plan-node';
import { saveAs } from 'file-saver-es';
import { Plan } from '../../domain/plan/plan';
import { PlanUtil } from '../../domain/plan/plan-util';

export class GpxWriter {
  write(plan: Plan, name: string): void {
    const content = this.header().concat(this.body(plan, name)).concat(this.footer()).join('\n');
    const blob = new Blob([content], { type: 'application/gpx' });
    const filename = name.replace(/ /g, '_') + '.gpx';
    saveAs(blob, filename);
  }

  private header(): ReadonlyArray<string> {
    return [
      `<?xml version="1.0" encoding="UTF-8" standalone="no"?>`,
      `<gpx creator="knooppuntnet" version="1.0" xmlns="http://www.topografix.com/GPX/1/0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"`,
      `  xsi:schemaLocation="http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd">`,
    ];
  }

  private footer(): ReadonlyArray<string> {
    return [`</gpx>`];
  }

  private body(plan: Plan, name: string): ReadonlyArray<string> {
    return this.wayPoints(plan).concat(this.tracks(plan, name));
  }

  private wayPoints(plan: Plan): ReadonlyArray<string> {
    const nodes = [plan.sourceNode].concat(
      plan.legs.flatMap((leg) => leg.routes.map((r) => r.sinkNode))
    );
    return nodes.flatMap((node) => this.wayPoint(node));
  }

  private wayPoint(node: PlanNode): ReadonlyArray<string> {
    return [
      `  <wpt lat="${node.latLon.latitude}" lon="${node.latLon.longitude}">`,
      `    <name>${node.nodeName}</name>`,
      `  </wpt>`,
    ];
  }

  private tracks(plan: Plan, name: string): ReadonlyArray<string> {
    const header = [`  <trk>`, `    <name><![CDATA[${name}]]></name>`, `    <trkseg>`];

    const footer = [`    </trkseg>`, `  </trk>`];

    const latLons = [plan.sourceNode.latLon].concat(
      plan.legs.flatMap((leg) => leg.routes.flatMap((route) => PlanUtil.planRouteLatLons(route)))
    );
    const body = latLons.flatMap((latLon) => this.trackPoint(latLon));
    return header.concat(body).concat(footer);
  }

  private trackPoint(latLon: LatLonImpl): ReadonlyArray<string> {
    return [`      <trkpt lat="${latLon.latitude}" lon="${latLon.longitude}">`, `      </trkpt>`];
  }
}
