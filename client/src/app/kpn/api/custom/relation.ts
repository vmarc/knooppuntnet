import { RawRelation } from '../common/data/raw';

export interface Relation {
  readonly raw: RawRelation;
  readonly members: any[];
}
