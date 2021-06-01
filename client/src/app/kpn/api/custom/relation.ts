import { RawRelation } from '../common/data/raw/raw-relation';

export interface Relation {
  readonly raw: RawRelation;
  readonly members: any[];
}
