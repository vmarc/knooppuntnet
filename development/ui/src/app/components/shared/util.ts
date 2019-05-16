import {Params} from "@angular/router";
import {Coordinate} from "ol/coordinate";
import {toLonLat, fromLonLat} from "ol/proj";
import {NetworkTypes} from "../../kpn/common/network-types";
import {Country} from "../../kpn/shared/country";
import {LatLonImpl} from "../../kpn/shared/lat-lon-impl";
import {Subset} from "../../kpn/shared/subset";

interface IPropertyGetter<T> {
  (): T;
}

export class Util {

  public static subsetInRoute(params: Params): Subset {
    const country = params["country"];
    const networkType = params["networkType"];
    return new Subset(new Country(country), NetworkTypes.withName(networkType));
  }

  public static replicationName(replicationNumber: number): string {
    const level1 = this.format(replicationNumber / 1000000);
    const remainder = replicationNumber % 1000000;
    const level2 = this.format(remainder / 1000);
    const level3 = this.format(remainder % 1000);
    return level1 + "/" + level2 + "/" + level3;
  }

  private static format(level: number): string {
    const integer = Math.floor(level);
    return (integer + 1000).toString().substr(1, 3);
  }

  public static safeGet<T>(getter: IPropertyGetter<T>, defaultValue?: T): T {
    try {
      let result: T = getter.apply(this);
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
    const latNumber = parseFloat(latLon.latitude);
    const lonNumber = parseFloat(latLon.longitude);
    return fromLonLat([lonNumber, latNumber]);
  }

}
