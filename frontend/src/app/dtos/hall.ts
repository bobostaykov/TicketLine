import {Show} from './show';

export class Hall {
  constructor(
    public id: number,
    public name: string,
    public shows: Show[]
  ) {}
}
