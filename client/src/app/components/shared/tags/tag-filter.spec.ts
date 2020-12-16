import {List} from 'immutable';
import {Tags} from '@api/custom/tags';
import {InterpretedTags} from './interpreted-tags';
import {Tag} from '@api/custom/tag';

describe('TagFilter', () => {

  it('empty', () => {
    const filter = InterpretedTags.networkTags(new Tags(List()));
    expect(filter.isEmpty()).toEqual(true);
    expect(filter.hasStandardTags()).toEqual(false);
    expect(filter.hasExtraTags()).toEqual(false);
    expect(filter.standardTags()).toEqual(List());
    expect(filter.extraTags()).toEqual(List());
  });

  it('tags are filtered out and sorted', () => {

    const unfiltered = Tags.fromArray(
      [
        ['c', '6'],
        ['name', '3'],
        ['b', '5'],
        ['type', '2'],
        ['a', '4'],
        ['network', '1']
      ]
    );

    const expectedStandardTags = [
      ['network', '1'],
      ['type', '2'],
      ['name', '3']
    ];

    const expectedExtraTags = [
      ['a', '4'],
      ['b', '5'],
      ['c', '6']
    ];

    const filter = InterpretedTags.networkTags(unfiltered);

    expect(filter.isEmpty()).toEqual(false);
    expect(filter.hasStandardTags()).toEqual(true);
    expect(filter.hasExtraTags()).toEqual(true);
    expect(toArray(filter.standardTags())).toEqual(expectedStandardTags);
    expect(toArray(filter.extraTags())).toEqual(expectedExtraTags);
  });

  function toArray(tags: List<Tag>): Array<Array<string>> {
    return tags.map(tag => [tag.key, tag.value]).toArray();
  }

});
