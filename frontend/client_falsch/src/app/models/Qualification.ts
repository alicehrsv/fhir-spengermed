import { Identifier } from './Identifier';
import { Coding } from './Coding';
import { Period } from './Period';

export class Qualification {
  constructor(
    public identifier: Identifier[] = [],
    public code?: Coding,
    public period?: Period
  ) {}
}