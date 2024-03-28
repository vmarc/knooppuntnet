import { PlanRoute } from '@api/common/planner';
import { List } from 'immutable';
import { PlanUtil } from '../domain/plan/plan-util';
import { PlannerTranslations } from './planner-translations';

export class PlanRouteColourUtil {
  static hasColour(planRoute: PlanRoute): boolean {
    return planRoute.segments.filter((segment) => !!segment.colour).length > 0;
  }

  static colours(planRoute: PlanRoute): string {
    const colourValues = planRoute.segments
      .filter((segment) => !!segment.colour)
      .map((segment) => segment.colour);
    const distinctColours = PlanUtil.distinctColours(List(colourValues));
    const colourGroups = distinctColours.map((colour) => PlannerTranslations.colour(colour));
    return colourGroups.join(' > ');
  }
}
