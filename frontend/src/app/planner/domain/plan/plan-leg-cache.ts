import { Map } from 'immutable';
import { PlanLeg } from './plan-leg';

export class PlanLegCache {
  private legs: Map<string, PlanLeg> = Map();
}
