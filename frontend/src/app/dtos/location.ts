export class Location {
  constructor (
    public locationName: string,
    public id: number,
    public country: string,
    public city: string,
    public postalCode: string,
    public street: string,
    public description: string
  ) {}
}
