import {saveAs} from 'file-saver';
import {List} from 'immutable';
import {LatLonImpl} from '@api/common/lat-lon-impl';
import {Plan} from '../../map/planner/plan/plan';
import {PlanNode} from '@api/common/planner/plan-node';
import {PlanUtil} from '../../map/planner/plan/plan-util';

export class GpxWriter {

  write(plan: Plan, name: string): void {
    const content = this.header().concat(this.body(plan, name)).concat(this.footer()).join('\n');
    const blob = new Blob([content], {type: 'application/gpx'});
    const filename = name.replace(/ /g, '_') + '.gpx';
    saveAs(blob, filename);
  }

  private header(): List<string> {
    return List([
      `<?xml version="1.0" encoding="UTF-8" standalone="no"?>`,
      `<gpx creator="knooppuntnet" version="1.0" xmlns="http://www.topografix.com/GPX/1/0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.topografix.com/GPX/1/0 http://www.topografix.com/GPX/1/0/gpx.xsd">`
    ]);
  }

  private footer(): List<string> {
    return List([
      `</gpx>`
    ]);
  }

  private body(plan: Plan, name: string): List<string> {
    return this.wayPoints(plan).concat(this.tracks(plan, name));
  }

  private wayPoints(plan: Plan): List<string> {
    const nodes = List([plan.sourceNode]).concat(plan.legs.flatMap(leg => leg.routes.map(r=> r.sinkNode)));
    return nodes.flatMap(node => this.wayPoint(node));
  }

  private wayPoint(node: PlanNode): List<string> {
    return List([
      `  <wpt lat="${node.latLon.latitude}" lon="${node.latLon.longitude}">`,
      `    <name>${node.nodeName}</name>`,
      `  </wpt>`
    ]);
  }

  private tracks(plan: Plan, name: string): List<string> {

    const header = List([
      `  <trk>`,
      `    <name><![CDATA[${name}]]></name>`,
      `    <trkseg>`
    ]);

    const footer = List([
      `    </trkseg>`,
      `  </trk>`
    ]);

    const latLons = List([plan.sourceNode.latLon])
      .concat(plan.legs.flatMap(leg => leg.routes.flatMap(route => PlanUtil.planRouteLatLons(route))));
    const body = latLons.flatMap(latLon => this.trackPoint(latLon));
    return header.concat(body).concat(footer);
  }

  private trackPoint(latLon: LatLonImpl): List<string> {
    return List([
      `      <trkpt lat="${latLon.latitude}" lon="${latLon.longitude}">`,
      `      </trkpt>`
    ]);
  }

}
