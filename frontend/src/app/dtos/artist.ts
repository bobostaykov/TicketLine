import {Event} from './event';

export class Artist {
  constructor(
    public id: number,
    public name: string,
    public eventParticipations: Event,
  ) {}
}
