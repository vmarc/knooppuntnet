import { MatTableDataSource } from '@angular/material/table';
import { Route } from '@angular/router';
import { ActivatedRouteSnapshot } from '@angular/router';
import { Bounds } from '@api/common/bounds';
import { TagDiffs } from '@api/common/diff/tag-diffs';
import { LatLonImpl } from '@api/common/lat-lon-impl';
import { Tags } from '@api/custom/tags';
import { List } from 'immutable';
import { Map } from 'immutable';
import { Coordinate } from 'ol/coordinate';
import { boundingExtent } from 'ol/extent';
import { Extent } from 'ol/extent';
import { fromLonLat, toLonLat } from 'ol/proj';

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
    return {
      latitude: '' + lonLat[1],
      longitude: '' + lonLat[0],
    };
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

  static hasTagDiffs(tagDiffs: TagDiffs): boolean {
    return (
      tagDiffs &&
      (tagDiffs.mainTags.length > 0 || tagDiffs.extraTags.length > 0)
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

  static tagWithKey(tags: Tags, key: string): string {
    const values = tags.tags.filter((t) => t.key === key).map((x) => x.value);
    if (values.length > 0) {
      return values[0];
    }
    return null;
  }

  static normalize(value: string): string {
    return value
      .toLowerCase()
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '');
  }

  static currentPageItems<T>(dataSource: MatTableDataSource<T>): Array<T> {
    const pageIndex = dataSource.paginator.pageIndex;
    const pageSize = dataSource.paginator.pageSize;
    const start = pageIndex * pageSize;
    const end = start + pageSize;
    return dataSource.filteredData.slice(start, end);
  }

  static toInteger(value: string): number {
    const isInteger = /^\d*$/.test(value);
    if (isInteger) {
      return +value;
    }
    return 0;
  }

  private static format(level: number): string {
    const integer = Math.floor(level);
    return (integer + 1000).toString().substring(1, 4);
  }
}
