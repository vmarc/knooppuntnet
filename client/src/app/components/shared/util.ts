import {Params, Route} from "@angular/router";
import {List} from "immutable";
import {Coordinate} from "ol/coordinate";
import {boundingExtent} from "ol/extent";
import {Extent} from "ol/extent";
import {fromLonLat, toLonLat} from "ol/proj";
import {Bounds} from "../../kpn/api/common/bounds";
import {ChangesParameters} from "../../kpn/api/common/changes/filter/changes-parameters";
import {LatLonImpl} from "../../kpn/api/common/lat-lon-impl";
import {Country} from "../../kpn/api/custom/country";
import {NetworkType} from "../../kpn/api/custom/network-type";
import {Subset} from "../../kpn/api/custom/subset";

interface IPropertyGetter<T> {
  (): T;
}

export class Util {

  public static routePath(path: string, component: any, sidebarComponent: any): Route {
    return {
      path: path,
      children: [
        {
          path: "",
          component: component
        },
        {
          path: "",
          component: sidebarComponent,
          outlet: "sidebar"
        }
      ]
    };
  }

  public static routePathWithToolbar(path: string, component: any, sidebarComponent: any, toolbarComponent: any): Route {
    return {
      path: path,
      children: [
        {
          path: "",
          component: component
        },
        {
          path: "",
          component: sidebarComponent,
          outlet: "sidebar"
        },
        {
          path: "",
          component: toolbarComponent,
          outlet: "toolbar"
        }
      ]
    };
  }

  public static subsetInRoute(params: Params): Subset {
    const country = params["country"];
    const networkType = params["networkType"];
    return new Subset(new Country(country), NetworkType.withName(networkType));
  }

  public static replicationName(replicationNumber: number): string {
    const level1 = this.format(replicationNumber / 1000000);
    const remainder = replicationNumber % 1000000;
    const level2 = this.format(remainder / 1000);
    const level3 = this.format(remainder % 1000);
    return level1 + "/" + level2 + "/" + level3;
  }

  public static safeGet<T>(getter: IPropertyGetter<T>, defaultValue?: T): T {
    try {
      const result: T = getter.apply(this);
      return (result == null) ? defaultValue : result;
    } catch (ex) {
      if (ex instanceof TypeError) {
        return defaultValue;
      }
      throw ex;
    }
  }

  public static latLonFromCoordinate(coordinate: Coordinate): LatLonImpl {
    const lonLat = toLonLat(coordinate);
    return new LatLonImpl("" + lonLat[1], "" + lonLat[0]);
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
    return list.reduce((prev, current) => prev + current);
  }

  public static defaultChangesParameters(): ChangesParameters {
    return new ChangesParameters(null, null, null, null, null, null, null, null, 0, 0, false);
  }

  public static toExtent(bounds: Bounds, delta: number): Extent {
    const latDelta = (bounds.maxLat - bounds.minLat) * delta;
    const lonDelta = (bounds.maxLon - bounds.minLon) * delta;
    const southWest = fromLonLat([bounds.minLon - lonDelta, bounds.minLat - latDelta]);
    const northEast = fromLonLat([bounds.maxLon + lonDelta, bounds.maxLat + latDelta]);
    return boundingExtent([southWest, northEast]);
  }

  private static format(level: number): string {
    const integer = Math.floor(level);
    return (integer + 1000).toString().substr(1, 3);
  }
}
