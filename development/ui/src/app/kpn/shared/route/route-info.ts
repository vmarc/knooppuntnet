// this class is generated, please do not modify

import {List} from 'immutable';
import {Fact} from '../fact';
import {RouteInfoAnalysis} from './route-info-analysis';
import {RouteSummary} from '../route-summary';
import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class RouteInfo {
  readonly summary: RouteSummary;
  readonly active: boolean;
  readonly display: boolean;
  readonly ignored: boolean;
  readonly orphan: boolean;
  readonly version: number;
  readonly changeSetId: number;
  readonly lastUpdated: Timestamp;
  readonly tags: Tags;
  readonly facts: List<Fact>;
  readonly analysis: RouteInfoAnalysis;

  constructor(summary: RouteSummary,
              active: boolean,
              display: boolean,
              ignored: boolean,
              orphan: boolean,
              version: number,
              changeSetId: number,
              lastUpdated: Timestamp,
              tags: Tags,
              facts: List<Fact>,
              analysis: RouteInfoAnalysis) {
    this.summary = summary;
    this.active = active;
    this.display = display;
    this.ignored = ignored;
    this.orphan = orphan;
    this.version = version;
    this.changeSetId = changeSetId;
    this.lastUpdated = lastUpdated;
    this.tags = tags;
    this.facts = facts;
    this.analysis = analysis;
  }

  public static fromJSON(jsonObject): RouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    return new RouteInfo(
      RouteSummary.fromJSON(jsonObject.summary),
      jsonObject.active,
      jsonObject.display,
      jsonObject.ignored,
      jsonObject.orphan,
      jsonObject.version,
      jsonObject.changeSetId,
      Timestamp.fromJSON(jsonObject.lastUpdated),
      Tags.fromJSON(jsonObject.tags),
      jsonObject.facts ? List(jsonObject.facts.map(json => Fact.fromJSON(json))) : List(),
      RouteInfoAnalysis.fromJSON(jsonObject.analysis)
    );
  }
}
