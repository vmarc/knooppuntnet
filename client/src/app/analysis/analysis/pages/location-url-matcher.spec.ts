import {UrlMatchResult} from "@angular/router";
import {UrlSegment} from "@angular/router";
import {LocationUrlMatcher} from "./location-url-matcher";

xdescribe("location-url-matcher", () => {

  it("match", () => {

    const url = [
      new UrlSegment("cycling", {}),
      new UrlSegment("nl", {}),
      new UrlSegment("essen", {}),
      new UrlSegment("nodes", {}),
    ];

    const result: UrlMatchResult = LocationUrlMatcher.match(url, null, null);

    expect(result.consumed).toEqual(url);
  });

  it("no match", () => {

    const url = [
      new UrlSegment("XX", {}),
      new UrlSegment("nl", {}),
      new UrlSegment("essen", {}),
      new UrlSegment("nodes", {}),
    ];

    const result: UrlMatchResult = LocationUrlMatcher.match(url, null, null);

    expect(result.consumed).toEqual(null);
  });
});
