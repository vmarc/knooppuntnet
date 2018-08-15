// this class is generated, please do not modify

import {Fact} from '../fact';
import {RouteInfoAnalysis} from './route-info-analysis';
import {RouteSummary} from '../route-summary';
import {Tags} from '../data/tags';
import {Timestamp} from '../timestamp';

export class RouteInfo {

  constructor(public summary?: RouteSummary,
              public active?: boolean,
              public display?: boolean,
              public ignored?: boolean,
              public orphan?: boolean,
              public version?: number,
              public changeSetId?: number,
              public lastUpdated?: Timestamp,
              public tags?: Tags,
              public facts?: Array<Fact>,
              public analysis?: RouteInfoAnalysis) {
  }

  public static fromJSON(jsonObject): RouteInfo {
    if (!jsonObject) {
      return undefined;
    }
    const instance = new RouteInfo();
    instance.summary = RouteSummary.fromJSON(jsonObject.summary);
    instance.active = jsonObject.active;
    instance.display = jsonObject.display;
    instance.ignored = jsonObject.ignored;
    instance.orphan = jsonObject.orphan;
    instance.version = jsonObject.version;
    instance.changeSetId = jsonObject.changeSetId;
    instance.lastUpdated = Timestamp.fromJSON(jsonObject.lastUpdated);
    instance.tags = Tags.fromJSON(jsonObject.tags);
    instance.facts = jsonObject.facts ? jsonObject.facts.map(json => Fact.fromJSON(json)) : [];
    instance.analysis = RouteInfoAnalysis.fromJSON(jsonObject.analysis);
    return instance;
  }
}

