import { Route } from '@angular/router';
import { UrlSegmentGroup } from '@angular/router';
import { UrlSegment } from '@angular/router';
import { UrlMatchResult } from '@angular/router';

export class LocationUrlMatcher {
  public static match(
    segments: UrlSegment[],
    group: UrlSegmentGroup,
    route: Route
  ): UrlMatchResult {
    const networkType =
      /(cycling)|(hiking)|(horse-riding)|(motorboat)|(canoe)|(inline-skating)/;
    const country = /(be)|(nl)|(de)|(fr)|(at)|(es)/;
    const detail = /(nodes)|(routes)|(facts)|(map)|(changes)|(edit)/;

    if (
      segments.length === 2 &&
      networkType.test(segments[0].path) &&
      country.test(segments[1].path)
    ) {
      return {
        consumed: [],
        posParams: {},
      };
    }
    if (
      segments.length === 4 &&
      networkType.test(segments[0].path) &&
      country.test(segments[1].path) &&
      detail.test(segments[3].path)
    ) {
      return {
        consumed: [],
        posParams: {},
      };
    }
    return null;
  }

  public static subsetUrl(
    segments: UrlSegment[],
    group: UrlSegmentGroup,
    route: Route
  ): UrlMatchResult {
    const networkType =
      /(cycling)|(hiking)|(horse-riding)|(motorboat)|(canoe)|(inline-skating)/;
    const country = /(be)|(nl)|(de)|(fr)|(at)|(es)/;
    const target =
      /(networks)|(facts)|(orphan-nodes)|(orphan-routes)|(map)|(changes)/;

    if (
      segments.length === 3 &&
      networkType.test(segments[0].path) &&
      country.test(segments[1].path) &&
      target.test(segments[2].path)
    ) {
      return {
        consumed: [],
        posParams: {
          // username: new UrlSegment(url[0].path.substr(1), {})
        },
      };
    }
    if (
      (segments.length === 3 || segments.length === 4) &&
      networkType.test(segments[0].path) &&
      country.test(segments[1].path) &&
      'facts' === segments[2].path
    ) {
      return {
        consumed: [],
        posParams: {
          // username: new UrlSegment(url[0].path.substr(1), {})
        },
      };
    }
    return null;
  }
}
