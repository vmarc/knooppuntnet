import { Tag } from '@api/custom/tag';
import { Tags } from '@api/custom/tags';
import { List } from 'immutable';

export class InterpretedTags {
  static nodeTags(tags: Tags): InterpretedTags {
    const standardTagKeys = [
      'rwn_ref',
      'rcn_ref',
      'expected_rwn_route_relations',
      'expected_rcn_route_relations',
      'expected_rhn_route_relations',
      'expected_rmn_route_relations',
      'expected_rpn_route_relations',
      'expected_rin_route_relations',
      'network:type',
    ];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static routeTags(tags: Tags): InterpretedTags {
    const standardTagKeys = [
      'ref',
      'note',
      'network',
      'type',
      'route',
      'network:type',
    ];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static networkTags(tags: Tags): InterpretedTags {
    const standardTagKeys = ['network', 'type', 'name', 'network:type'];
    return new InterpretedTags(standardTagKeys, tags);
  }

  static all(tags: Tags): InterpretedTags {
    const standardTagKeys = [];
    return new InterpretedTags(standardTagKeys, tags);
  }

  private constructor(private standardTagKeys: string[], private tags: Tags) {}

  isEmpty(): boolean {
    return this.tags.tags.length === 0;
  }

  standardTags(): Tag[] {
    const tagArray: Array<Tag> = [];
    this.standardTagKeys.forEach((key) => {
      this.tags.tags
        .filter((t) => t.key === key)
        .forEach((x) => tagArray.push(x));
    });
    return tagArray;
  }

  extraTags(): Tag[] {
    const tags = this.tags.tags.filter(
      (tag) => !this.standardTagKeys.includes(tag.key)
    );
    return List(tags)
      .sortBy((tag) => tag.key)
      .toArray();
  }

  hasStandardTags(): boolean {
    return (
      this.tags.tags.find((tag) => this.standardTagKeys.includes(tag.key)) !==
      undefined
    );
  }

  hasExtraTags(): boolean {
    return (
      this.tags.tags.find((tag) => !this.standardTagKeys.includes(tag.key)) !==
      undefined
    );
  }
}
