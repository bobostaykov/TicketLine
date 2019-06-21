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
  // component can be used to display either seats or sectors which are passed as input element
  @Input() private seats?: Seat[];
  @Input() private sectors?: Sector[];
  // event emitter to add ticket for selected seat or sector
  @Output() private addTicket: EventEmitter<Seat | Sector> = new EventEmitter<Seat | Sector>();
  // properties necessary to adjust svg viebox on zoom/mousedrag
  private viewboxPosX: number = 0;
  private viewboxPosY: number = 0;
  private viewboxWidth: number = 300;
  // initial svg viewbox
  private viewbox: string = '0 0 300 300';
  // list of priceCategories to loop through in select fields
  // noinspection JSMismatchedCollectionQueryUpdate
  private priceCategories: string[] = Object.keys(PriceCategory);
  // on update operation updateElementModel is changed and then assigned to selected element
  private selectedElement: Seat | Sector;
  private updateElementModel: Seat | Sector = this.seats ? new Seat(null, null, null, null, null) : new Sector(null, null, null, null);
  // updateSubmissionError is displayed if seat/sector with given number/row already exists in the hall
  private updateSubmissionError: boolean = false;
  private updateSubmissionMessage: string = '';
  // event handlers for mousedrag as well as closing update/context menu
  private _svgDrag = this.svgDrag.bind(this);
  private _svgDragExit = this.svgDragExit.bind(this);
  private _closeMenusClick = this.onCloseMenusClick.bind(this);

  constructor() {
  }

  ngOnInit() {
  }

  /**
   * gets string for d attribute of svg path element depicting a seat
   * @param seat for which to return path attribute
   */
  private getSeatPath(seat: Seat): string {
    return 'M' + this.getSeatXPos(seat) + ' ' + this.getSeatYPos(seat) +
      'h ' + this.getSeatWidthAndHeight() + ' v ' + this.getSeatWidthAndHeight() + ' h ' + -this.getSeatWidthAndHeight() + ' Z';
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
   * get color of seat or sector passed as parameter
   * color depends on the priceCategory of the seat
   * @param elem for which to return color
   */
  private getColor(elem: Seat | Sector): string {
    return elem.priceCategory === PriceCategory.Cheap ? '#2fb207' : (elem.priceCategory === PriceCategory.Average ? '#129ded' : '#db0611');
  }

  /**
   * initializes operation to display update form for selected seat
   * @param seat for which to display update form
   */
  private displaySeatUpdateForm(seat: Seat): void {
    this.selectedElement = seat;
    const xPos = this.getSeatXPos(seat);
    const yPos = this.getSeatYPos(seat);
    const seatWidthAndHeight = this.getSeatWidthAndHeight();
    this.displayUpdateForm(xPos, yPos, seatWidthAndHeight, seatWidthAndHeight);
  }

  /**
   * initializes operation to display update form for selected sector
   * @param sector for which to display update form
   */
  private displaySectorUpdateForm(sector: Sector): void {
    this.selectedElement = sector;
    const xPos = this.getSectorXPos(sector);
    const yPos = this.getSectorYPos(sector);
    const sectorWidth = this.getSectorWidth();
    const sectorHeight = this.getSectorHeight();
    this.displayUpdateForm(xPos, yPos, sectorWidth, sectorHeight);
  }

  /**
   * calculates position of selected seat or sector and displays update form near it
   * NOTE: This calculation is necessary because we want to display update form correctly even if no click event is available
   * to get the correct position
   * @param xPos svg coordinate of seat or sector displayed as path element
   * @param yPos svg coordinate of seat or sector displayed as path element
   * @param elementWidth of seat or sector path element in svg coordinates
   * @param elementHeight of seat or sector path element in svg coordinates
   */
  private displayUpdateForm(xPos: number, yPos: number, elementWidth: number, elementHeight: number): void {
    const svg = document.getElementById('floorplan');
    if (svg instanceof SVGSVGElement) {
      // close contextmenu so it is not active at the same time
      this.closeContext();
      const updateForm = document.getElementById('updateForm');
      updateForm.style.display = 'inline-block';
      const rectForm = updateForm.getBoundingClientRect();
      const rectSvg = svg.getBoundingClientRect();
      const svgpx = svg.getCTM().a;
      updateForm.style.left = rectSvg.left + svgpx * (xPos - this.viewboxPosX) - rectForm.width / 2 + elementWidth * svgpx / 2 + 'px';
      updateForm.style.top = rectSvg.top + svgpx * (yPos - this.viewboxPosY) + elementHeight * svgpx + 20 + window.scrollY + 'px';
      this.updateElementModel = {...this.selectedElement};
      document.addEventListener('click', this._closeMenusClick);
    }
  }

  /**
   * displays contextmenu for selected seat or sector element near mouse event
   * @param element seat or sector for which contextmenu is displayed
   * @param event mouse event containing position of selected seat or sector
   */
  private displayContext(element: Seat | Sector, event: MouseEvent): void {
    event.preventDefault();
    // close update form so it is not displayed at the same time
    this.closeUpdateForm();
    const rectEvent = (event.target as HTMLElement).getBoundingClientRect();
    const context = document.getElementById('contextmenu');
    context.style.left = rectEvent.left + 'px';
    context.style.top = rectEvent.bottom + 'px';
    context.style.display = 'inline-block';
    this.selectedElement = element;
    document.addEventListener('click', this._closeMenusClick);
  }

  /**
   * event listener function which closes update form or contextmenu on click anywhere but the form itself or a seat/sector path element
   * @param event mouse click event
   */
  private onCloseMenusClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!(target.tagName === 'path' || target.closest('#updateForm') || target.closest('#contextmenu'))) {
      this.closeUpdateForm();
      this.closeContext();
      document.removeEventListener('click', this._closeMenusClick);
    }
  }

  /**
   * hides/closes update form
   */
  private closeUpdateForm(): void {
    const updateForm = document.getElementById('updateForm');
    if (updateForm) {
      document.getElementById('updateForm').style.display = 'none';
    }
  }

  /**
   * hides/closes context menu
   */
  private closeContext(): void {
    const contextmenu = document.getElementById('contextmenu');
    if (contextmenu) {
      document.getElementById('contextmenu').style.display = 'none';
    }
  }

  /**
   * deletes selected element from seat/sector array
   */
  private deleteSelectedElement(): void {
    if (this.selectedElement) {
      if (this.seats && this.seats.length) {
        this.seats.splice(this.seats.indexOf(this.selectedElement as Seat), 1);
      } else if (this.sectors && this.sectors.length) {
        this.sectors.splice(this.sectors.indexOf(this.selectedElement as Sector), 1);
      }
      this.closeContext();
    }
  }

  /**
   * updates seat or sector element with parameters found in updateElementModel
   * displays update form wherever selectedElement with updated parameters reappears
   */
  private updateSelectedElement(): void {
    if (this.selectedElement && this.updateElementModel) {
      if (this.seats && this.seats.length) {
        const updateElement = this.updateElementModel as Seat;
        if (this.seats.some(seat => seat !== this.selectedElement &&
          seat.seatNumber === updateElement.seatNumber &&
          seat.seatRow === updateElement.seatRow)) {
          this.updateSubmissionError = true;
          this.updateSubmissionMessage = 'A Seat with row ' + updateElement.seatRow +
            ' and number ' + updateElement.seatNumber + ' already exists!';
        } else {
          Object.assign(this.selectedElement, updateElement);
          this.updateSubmissionError = false;
          this.displaySeatUpdateForm(this.selectedElement as Seat);
        }
      } else if (this.sectors && this.sectors.length) {
        const updateElement = this.updateElementModel as Sector;
        if (this.sectors.some(sector => sector !== this.selectedElement &&
          sector.sectorNumber === updateElement.sectorNumber)) {
          this.updateSubmissionError = true;
          this.updateSubmissionMessage = 'A Sector with number ' + updateElement.sectorNumber + ' already exists!';
        } else {
          Object.assign(this.selectedElement, updateElement);
          this.updateSubmissionError = false;
          this.displaySectorUpdateForm(this.selectedElement as Sector);
        }
      }
    }
  }

  /**
   * emits event for selectedElement to parent component so it can add selectedElement to tickets array
   */
  private addToTicket(): void {
    if (this.selectedElement) {
      this.addTicket.emit(this.selectedElement);
      this.closeContext();
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

  /**
   * called on mousemove event after mousedown in svg
   * allows users to change viewbox by dragging it
   * @param event mousemove event used to drag viewbox
   */
  private svgDrag(event: MouseEvent): void {
    this.viewboxPosX -= event.movementX / 2;
    this.viewboxPosY -= event.movementY / 2;
    this.viewboxPosX = this.viewboxPosX < 0 ? 0 : this.viewboxPosX;
    this.viewboxPosY = this.viewboxPosY < 0 ? 0 : this.viewboxPosY;
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
  }

  /**
   * called on mousedown event  in svg
   * initializes viewbox drag on mousemove by adding eventlisteners to mousemove event and mouseup event to end mousedrag
   */
  private svgDragInit(): void {
    const svg = document.getElementById('floorplan');
    if (svg) {
      svg.addEventListener('mousemove', this._svgDrag);
      document.addEventListener('mouseup', this._svgDragExit);
      svg.classList.add('grabbing');
    }
  }

  /**
   * called on mouseup event in svg
   * ends viewbox dragging by removing mousemove event listener
   */
  private svgDragExit(): void {
    const svg = document.getElementById('floorplan');
    if (svg) {
      svg.removeEventListener('mousemove', this._svgDrag);
      svg.classList.remove('grabbing');
    }
  }

  /**
   * getter function
   * calculates the x position of a given seat element from its number
   * @param seat for which x position is to be calculated
   */
  private getSeatXPos(seat: Seat): number {
    return (seat.seatNumber - 1) * 1.2 * 10 + Math.floor(seat.seatNumber / 15) * 10;
  }

  /**
   * getter function
   * calculates the y position of a given seat element from its number
   * @param seat for which y position is to be calculated
   */
  private getSeatYPos(seat: Seat): number {
    return (seat.seatRow - 1) * 1.2 * 10 + Math.floor(seat.seatRow / 10) * 10;
  }

  /**
   * getter function
   * calculates the x position of a given sector element from its number
   * @param sector for which x position is to be calculated
   */
  private getSectorXPos(sector: Sector): number {
    return ((sector.sectorNumber - 1) % 3) * (this.getSectorWidth() + this.getSectorGap());
  }

  /**
   * getter function
   * calculates the y position of a given sector element from its number
   * @param sector for which y position is to be calculated
   */
  private getSectorYPos(sector: Sector): number {
    return Math.floor((sector.sectorNumber - 1) / 3) * (this.getSectorHeight() + this.getSectorGap());
  }

  /**
   * gets number which is used both for width and height of a path element depicting a seat
   */
  private getSeatWidthAndHeight(): number {
    return 10;
  }

  /**
   * gets number which is used for height of a path element depicting a sector
   */
  private getSectorHeight(): number {
    return 50;
  }

  /**
   * gets number which is used for width of a path element depicting a sector
   */
  private getSectorWidth(): number {
    return this.viewboxWidth / 3.2;
  }

  /**
   * calculates gap found along the x-axis of the svg between different sectors
   */
  private getSectorGap(): number {
    return this.viewboxWidth / 32;
  }
}
