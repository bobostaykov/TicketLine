import {PriceCategory} from './priceCategory';

export class Sector {
  constructor(
    public id: number,
    public sectorNumber: number,
    public price: PriceCategory
  ) {
  }
}
