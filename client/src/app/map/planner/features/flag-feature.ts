import { MapFeature } from './map-feature';
import { PlanFlagType } from '../plan/plan-flag-type';

export class FlagFeature extends MapFeature {
  constructor(readonly flagType: PlanFlagType, readonly id: string) {
    super();
  }

  static start(id: string): FlagFeature {
    return new FlagFeature(PlanFlagType.Start, id);
  }

  static via(id: string): FlagFeature {
    return new FlagFeature(PlanFlagType.Via, id);
  }

  static end(id: string): FlagFeature {
    return new FlagFeature(PlanFlagType.End, id);
  }
}
