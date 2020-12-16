import {Injectable} from '@angular/core';
import {SubsetInfo} from '@api/common/subset/subset-info';
import {Map} from 'immutable';

@Injectable({
  providedIn: 'root'
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
