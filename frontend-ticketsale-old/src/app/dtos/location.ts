export class Location {
  constructor (
    public id: number,
    public country: string,
    public city: string,
    public postalCode: string,
    public street: string,
    public description: string
  ) {}
}
