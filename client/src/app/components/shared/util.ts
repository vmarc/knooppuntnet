import { Params, Route } from '@angular/router';
import { ActivatedRouteSnapshot } from '@angular/router';
import { List } from 'immutable';
import { Map } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import { boundingExtent } from 'ol/extent';
import { Extent } from 'ol/extent';
import { fromLonLat, toLonLat } from 'ol/proj';
import { Bounds } from '@api/common/bounds';
import { ChangesParameters } from '@api/common/changes/filter/changes-parameters';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { Country } from '@api/custom/country';
import { NetworkType } from '@api/custom/network-type';
import { Subset } from '@api/custom/subset';
import { TagDiffs } from '@api/common/diff/tag-diffs';

type IPropertyGetter<T> = () => T;

export class Util {
  public static routePath(
    path: string,
    component: any,
    sidebarComponent: any
  ): Route {
    return {
      path,
      children: [
        {
          path: '',
          component,
        },
        {
          path: '',
          component: sidebarComponent,
          outlet: 'sidebar',
        },
      ],
    };
  }

  public static routePathWithToolbar(
    path: string,
    component: any,
    sidebarComponent: any,
    toolbarComponent: any
  ): Route {
    return {
      path,
      children: [
        {
          path: '',
          component,
        },
        {
          path: '',
          component: sidebarComponent,
          outlet: 'sidebar',
        },
        {
          path: '',
          component: toolbarComponent,
          outlet: 'toolbar',
        },
      ],
    };
  }

  public static subsetInRoute(params: Params): Subset {
    const country = params['country'];
    const networkType = params['networkType'];
    return new Subset(new Country(country), NetworkType.withName(networkType));
  }

  public static replicationName(replicationNumber: number): string {
    const level1 = this.format(replicationNumber / 1000000);
    const remainder = replicationNumber % 1000000;
    const level2 = this.format(remainder / 1000);
    const level3 = this.format(remainder % 1000);
    return level1 + '/' + level2 + '/' + level3;
  }

  public static safeGet<T>(getter: IPropertyGetter<T>, defaultValue?: T): T {
    try {
      const result: T = getter.apply(this);
      return result == null ? defaultValue : result;
    } catch (ex) {
      if (ex instanceof TypeError) {
        return defaultValue;
      }
      throw ex;
    }
  }

  public static latLonFromCoordinate(coordinate: Coordinate): LatLonImpl {
    const lonLat = toLonLat(coordinate);
    return new LatLonImpl('' + lonLat[1], '' + lonLat[0]);
  }

  public static coordinateToString(coordinate: Coordinate): string {
    if (coordinate) {
      const lonLat = toLonLat(coordinate);
      return '[' + lonLat[1] + ', ' + lonLat[0] + ']';
    }
    return '[]';
  }

  public static latLonToCoordinate(latLon: LatLonImpl): Coordinate {
    return this.toCoordinate(latLon.latitude, latLon.longitude);
  }

  public static toCoordinate(latitude: string, longitude: string): Coordinate {
    const latNumber = parseFloat(latitude);
    const lonNumber = parseFloat(longitude);
    return fromLonLat([lonNumber, latNumber]);
  }

  public static sum(list: List<number>): number {
    if (list.isEmpty()) {
      return 0;
    }
    return list.reduce((prev, current) => prev + current);
  }

  public static defaultChangesParameters(): ChangesParameters {
    return new ChangesParameters(
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      null,
      0,
      0,
      false
    );
  }

  public static toExtent(bounds: Bounds, delta: number): Extent {
    const latDelta = (bounds.maxLat - bounds.minLat) * delta;
    const lonDelta = (bounds.maxLon - bounds.minLon) * delta;
    const southWest = fromLonLat([
      bounds.minLon - lonDelta,
      bounds.minLat - latDelta,
    ]);
    const northEast = fromLonLat([
      bounds.maxLon + lonDelta,
      bounds.maxLat + latDelta,
    ]);
    return boundingExtent([southWest, northEast]);
  }

  public static today(): string {
    const today = new Date();
    const yyyy = today.getFullYear();
    const mm = Util.twoDigits(today.getMonth() + 1);
    const dd = Util.twoDigits(today.getDate());
    return yyyy + '-' + mm + '-' + dd;
  }

  private static format(level: number): string {
    const integer = Math.floor(level);
    return (integer + 1000).toString().substr(1, 3);
  }

  static hasTagDiffs(tagDiffs: TagDiffs): boolean {
    return (
      tagDiffs && !(tagDiffs.mainTags.isEmpty() && tagDiffs.extraTags.isEmpty())
    );
  }

  static twoDigits(value: number): string {
    return (value < 10 ? '0' : '') + value;
  }

  static paramsIn(routeSnapshot: ActivatedRouteSnapshot): Map<string, string> {
    let route = routeSnapshot;
    let params = Map(
      Object.keys(route.params).map((key) => [key, route.params[key]])
    );
    while (route.firstChild) {
      route = route.firstChild;
      Object.keys(route.params).forEach(
        (key) => (params = params.set(key, route.params[key]))
      );
    }
    return params;
  }

  static json(object: any): string {
    return JSON.stringify(object, null, 2);
  }
}
