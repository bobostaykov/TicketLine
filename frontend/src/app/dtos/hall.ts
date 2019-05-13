import {Show} from './show';
import {Location} from './location';

export class Hall {
  constructor(
    public id: number,
    public name: string,
    public shows: Show[],
    public location: Location
  ) {}
}
