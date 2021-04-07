import { MapFeature } from './map-feature';
import { PlanFlagType } from '../plan/plan-flag-type';

export class FlagFeature extends MapFeature {
  constructor(readonly flagType: PlanFlagType, readonly id: string) {
    super();
  }

  static start(id: string): FlagFeature {
    return new FlagFeature(PlanFlagType.start, id);
  }

  static via(id: string): FlagFeature {
    return new FlagFeature(PlanFlagType.via, id);
  }

  static end(id: string): FlagFeature {
    return new FlagFeature(PlanFlagType.end, id);
  }
}
