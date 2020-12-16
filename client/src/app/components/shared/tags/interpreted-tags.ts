import {List} from 'immutable';
import {Tags} from '@api/custom/tags';
import {Tag} from '@api/custom/tag';

export class InterpretedTags {

  static nodeTags(tags: Tags): InterpretedTags {
    const standardTagKeys = List([
      'rwn_ref',
      'rcn_ref',
      'expected_rwn_route_relations',
      'expected_rcn_route_relations',
      'expected_rhn_route_relations',
      'expected_rmn_route_relations',
      'expected_rpn_route_relations',
      'expected_rin_route_relations',
      'network:type'
    ]);
    return new InterpretedTags(standardTagKeys, tags);
  }

  static routeTags(tags: Tags): InterpretedTags {
    const standardTagKeys = List([
      'ref',
      'note',
      'network',
      'type',
      'route',
      'network:type'
    ]);
    return new InterpretedTags(standardTagKeys, tags);
  }

  static networkTags(tags: Tags): InterpretedTags {
    const standardTagKeys = List([
      'network',
      'type',
      'name',
      'network:type'
    ]);
    return new InterpretedTags(standardTagKeys, tags);
  }

  static all(tags: Tags): InterpretedTags {
    const standardTagKeys = List([]);
    return new InterpretedTags(standardTagKeys, tags);
  }

  private constructor(private standardTagKeys: List<string>, private tags: Tags) {
  }

  isEmpty(): boolean {
    return this.tags.isEmpty();
  }

  standardTags(): List<Tag> {
    return this.standardTagKeys.flatMap(key => this.tags.tagsWithKey(key));
  }

  extraTags(): List<Tag> {
    const tags = this.tags.tags.filter(tag => !this.standardTagKeys.contains(tag.key));
    return tags.sortBy((tag) => tag.key);
  }

  hasStandardTags(): boolean {
    return this.tags.tags.find(tag => this.standardTagKeys.contains(tag.key)) !== undefined;
  }

  hasExtraTags(): boolean {
    return this.tags.tags.find(tag => !this.standardTagKeys.contains(tag.key)) !== undefined;
  }

}
