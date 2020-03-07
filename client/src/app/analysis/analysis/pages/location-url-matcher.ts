import {Route} from "@angular/router";
import {UrlSegmentGroup} from "@angular/router";
import {UrlSegment} from "@angular/router";
import {UrlMatchResult} from "@angular/router";

export class LocationUrlMatcher {

  public static match(segments: UrlSegment[], group: UrlSegmentGroup, route: Route): UrlMatchResult {

    const p0 = /(cycling)|(hiking)|(canoe)/;
    const p1 = /(be)|(nl)|(de)|(fr)|(at)/;
    const p3 = /(nodes)|(routes)|(facts)|(map)|(changes)/;

    if (
      segments.length === 4 &&
      p0.test(segments[0].path) &&
      p1.test(segments[1].path) &&
      p3.test(segments[3].path)
    ) {
      return {
        consumed: [],
        posParams: {}
      };
    }
    return null;
  }


  public static subsetUrl(segments: UrlSegment[], group: UrlSegmentGroup, route: Route): UrlMatchResult {

    const networkType = /(cycling)|(hiking)|(horse-riding)|(motorboat)|(canoe)|(inline-skating)/;
    const country = /(be)|(nl)|(de)|(fr)|(at)/;
    const target = /(networks)|(facts)|(orphan-nodes)|(orphan-routes)|(map)|(changes)/;

    if (
      segments.length === 3 &&
      networkType.test(segments[0].path) &&
      country.test(segments[1].path) &&
      target.test(segments[2].path)
    ) {
      console.log("\n\n\nSUBSET url=" + segments.map(s => s.path) + " MATCH \n\n\n");
      return {
        consumed: [],
        posParams: {
          // username: new UrlSegment(url[0].path.substr(1), {})
        }
      };
    }
    return null;
  }
}
