import { agent } from './agent';

import {entity} from './entity';


export class PLF5A22 {
  constructor(
    public id: string = '',
    public why: markdown[] = [],
    public agent: agent[] = [],
    public entity: entity[] = []
  ) {
  }
}
