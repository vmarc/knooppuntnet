import {saveAs} from "file-saver";
import {List} from "immutable";
import {LatLonImpl} from "../../kpn/api/common/lat-lon-impl";
import {Plan} from "../../map/planner/plan/plan";
import {PlanNode} from "../../map/planner/plan/plan-node";

export class GpxWriter {

  write(plan: Plan): void {
    const content = this.header().concat(this.body(plan)).concat(this.footer()).join("\n");
    const blob = new Blob([content], {type: "application/gpx"});
    const filename = "knooppuntnet.gpx";
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

  private body(plan: Plan): List<string> {
    return this.wayPoints(plan).concat(this.tracks(plan));
  }

  private wayPoints(plan: Plan): List<string> {
    const nodes = List([plan.source]).concat(plan.legs.map(leg => leg.sink));
    return nodes.flatMap(node => this.wayPoint(node));
  }

  private wayPoint(node: PlanNode): List<string> {
    return List([
      `  <wpt lat="${node.latLon.latitude}" lon="${node.latLon.longitude}">`,
      `    <name>${node.nodeName}</name>`,
      `  </wpt>`
    ]);
  }

  private tracks(plan: Plan): List<string> {

    const header = List([
      `  <trk>`,
      `    <trkseg>`
    ]);

    const footer = List([
      `    </trkseg>`,
      `  </trk>`
    ]);

    const latLons = List([plan.source.latLon]).concat(plan.legs.flatMap(leg => leg.latLons()));
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
