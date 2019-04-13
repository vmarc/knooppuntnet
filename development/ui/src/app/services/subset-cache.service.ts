import {Injectable} from "@angular/core";
import {Map} from "immutable";
import {SubsetInfo} from "../kpn/shared/subset/subset-info";

@Injectable({
  providedIn: "root"
})
export class SubsetCacheService {

  private subsetInfos = Map<string, SubsetInfo>();

  getSubsetInfo(subsetKey: string): SubsetInfo {
    return this.subsetInfos.get(subsetKey);
  }

  setSubsetInfo(subsetKey: string, subsetInfo: SubsetInfo) {
    this.subsetInfos = this.subsetInfos.set(subsetKey, subsetInfo);
  }

}
