import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {PriceCategory} from '../../../dtos/priceCategory';

@Component({
  selector: 'app-floorplan-svg',
  templateUrl: './floorplan-svg.component.html',
  styleUrls: ['./floorplan-svg.component.scss']
})
export class FloorplanSvgComponent implements OnInit {
  @Input() private seats?: Seat[];
  @Input() private sectors?: Sector[];
  @Output() private addTicket: EventEmitter<Seat | Sector> = new EventEmitter<Seat | Sector>();
  private viewboxPosX: number = 0;
  private viewboxPosY: number = 0;
  private viewboxWidth: number = 300;
  private viewbox: string = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  // list of priceCategories to loop through in select fields
  // noinspection JSMismatchedCollectionQueryUpdate
  private priceCategories: string[] = Object.keys(PriceCategory);
  private selectedElement: Seat | Sector;
  private updateElementModel: Seat | Sector = this.seats ? new Seat(null, null, null, null) : new Sector(null, null, null);
  private updateSubmissionError: boolean = false;
  private updateSubmissionMessage: string = '';
  private _svgDrag = this.svgDrag.bind(this);
  private _svgDragExit = this.svgDragExit.bind(this);

  constructor() {
  }

  /**
   * gets string for d attribute of svg path element depicting a seat
   * @param seat for which to return path attribute
   */
  private getSeatPath(seat: Seat): string {
    return 'M' + this.getSeatXPos(seat) + ' ' + this.getSeatYPos(seat) +
      'h ' + this.getSeatWidth() + ' v ' + this.getSeatWidth() + ' h ' + -this.getSeatWidth() + ' Z';
  }

  /**
   * gets string for d attribut of svg path element depicting a sector
   * @param sector for which to return path attribute
   */
  private getSectorPath(sector: Sector): string {
    return 'M' + this.getSectorXPos(sector) + ' ' + this.getSectorYPos(sector) + 'h ' +
      this.getSectorWidth() + ' v ' + this.getSectorHeight() + ' h ' + (-this.getSectorWidth()) + ' Z';
  }

  /**
   * get color of seat or sector assed as parameter
   * color depends on the priceCategory of the seat
   * @param elem for which to return color
   */
  private getColor(elem: Seat | Sector): string {
    return elem.priceCategory === PriceCategory.Cheap ? '#2fb207' : (elem.priceCategory === PriceCategory.Average ? '#129ded' : '#db0611');
  }

  private displayUpdateForm(element: Seat | Sector): void {
    const svg = document.getElementsByTagName('svg')[0];
    if (svg instanceof SVGSVGElement) {
      this.closeContext();
      const updateForm = document.getElementById('updateForm');
      updateForm.style.display = 'inline-block';
      const rectForm = updateForm.getBoundingClientRect();
      const rectSvg = svg.getBoundingClientRect();
      const svgpx = svg.getCTM().a;
      const elementHeight = element instanceof Seat ? this.getSeatWidth() * svgpx : this.getSectorHeight() * svgpx;
      const elementWidth = element instanceof Seat ? this.getSeatWidth() * svgpx : this.getSectorWidth() * svgpx;
      const xPos = element instanceof Seat ? this.getSeatXPos(element) : this.getSectorXPos(element);
      const yPos = element instanceof Seat ? this.getSeatYPos(element) : this.getSectorYPos(element);
      updateForm.style.left = rectSvg.left + svgpx * (xPos - this.viewboxPosX) - rectForm.width / 2 + elementWidth / 2 + 'px';
      updateForm.style.top = rectSvg.top + svgpx * (yPos - this.viewboxPosY) + elementHeight + 20 + window.scrollY + 'px';
      this.selectedElement = element;
      this.updateElementModel = {...this.selectedElement};
    }
  }

  private closeUpdateForm(): void {
    document.getElementById('updateForm').style.display = 'none';
  }

  private getSeatXPos(seat: Seat): number {
    return (seat.seatNumber - 1) * 1.2 * 10 + Math.floor(seat.seatNumber / 15) * 10;
  }

  private getSeatYPos(seat: Seat): number {
    return (seat.seatRow - 1) * 1.2 * 10 + Math.floor(seat.seatRow / 10) * 10;
  }

  private getSectorXPos(sector: Sector): number {
    return ((sector.sectorNumber - 1) % 3) * (this.getSectorWidth() + this.getSectorGap());
  }

  private getSectorYPos(sector: Sector): number {
    return Math.floor((sector.sectorNumber - 1) / 3) * (this.getSectorHeight() + this.getSectorGap());
  }

  private getSeatWidth(): number {
    return 10;
  }

  private getSectorHeight(): number {
    return 50;
  }

  private getSectorWidth(): number {
    return this.viewboxWidth / 3.2;
  }

  private getSectorGap(): number {
    return this.viewboxWidth / 32;
  }

  ngOnInit() {
  }

  private displayContext(element: Seat | Sector, event: MouseEvent): void {
    event.preventDefault();
    if (event.target instanceof Element) {
      this.closeUpdateForm();
      const rectEvent = event.target.getBoundingClientRect();
      const context = document.getElementById('contextmenu');
      context.style.left = rectEvent.left + 'px';
      context.style.top = rectEvent.bottom + 'px';
      context.style.display = 'inline-block';
      this.selectedElement = element;
    }
  }

  private closeContext(): void {
    document.getElementById('contextmenu').style.display = 'none';
  }

  private onSvgClick(event: Event): void {
    if ((event.target as HTMLElement).tagName !== 'path') {
      this.closeUpdateForm();
      this.closeContext();
    }
  }

  private deleteSelectedElement(): void {
    if (this.selectedElement instanceof Seat) {
      this.seats.splice(this.seats.indexOf(this.selectedElement), 1);
    } else {
      this.sectors.splice(this.sectors.indexOf(this.selectedElement), 1);
    }
    this.closeContext();
  }

  private addToTicket(): void {
    this.addTicket.emit(this.selectedElement);
  }

  private updateSelectedElement(): void {
    if (this.selectedElement instanceof Seat) {
      if (this.seats.some(seat => seat !== this.selectedElement &&
        seat.seatNumber === (this.updateElementModel as Seat).seatNumber &&
        seat.seatRow === (this.updateElementModel as Seat).seatRow)) {
        this.updateSubmissionError = true;
        this.updateSubmissionMessage = 'A Seat with row ' + (this.updateElementModel as Seat).seatRow +
          ' and number ' + (this.updateElementModel as Seat).seatNumber + ' already exists!';
      } else {
        Object.assign(this.selectedElement, this.updateElementModel as Seat);
        this.updateSubmissionError = false;
        this.displayUpdateForm(this.selectedElement);
      }
    } else {
      if (this.sectors.some(sector => sector !== this.selectedElement &&
        sector.sectorNumber === (this.updateElementModel as Sector).sectorNumber)) {
        this.updateSubmissionError = true;
        this.updateSubmissionMessage = 'A Sector with number ' + (this.updateElementModel as Sector).sectorNumber +
          ' already exists!';
      } else {
        Object.assign(this.selectedElement, this.updateElementModel as Sector);
        this.updateSubmissionError = false;
        this.displayUpdateForm(this.selectedElement);
      }
    }

  }

  /**
   * allows users to zoom in by adjusting svg viewbox
   * @param event mousewheel event used to zoom
   */
  private zoom(event) {
    event.preventDefault();
    this.viewboxWidth = event.deltaY > 0 ? this.viewboxWidth * 1.05 : this.viewboxWidth / 1.05;
    this.viewboxWidth = this.viewboxWidth < 300 ? 300 : (this.viewboxWidth > 1310 ? 1310 : this.viewboxWidth);
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  }

  private svgDrag(event: MouseEvent): void {
    this.viewboxPosX -= event.movementX / 2;
    this.viewboxPosY -= event.movementY / 2;
    this.viewboxPosX = this.viewboxPosX < 0 ? 0 : this.viewboxPosX;
    this.viewboxPosY = this.viewboxPosY < 0 ? 0 : this.viewboxPosY;
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  }

  private svgDragInit(): void {
    const svg = document.getElementsByTagName('svg')[0];
    svg.addEventListener('mousemove', this._svgDrag);
    document.addEventListener('mouseup', this._svgDragExit);
    svg.classList.add('grabbing');
  }

  private svgDragExit(): void {
    const svg = document.getElementsByTagName('svg')[0];
    svg.removeEventListener('mousemove', this._svgDrag);
    svg.classList.remove('grabbing');
  }
}
