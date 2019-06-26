import {AfterViewInit, Component, DoCheck, Input, IterableDiffers, OnInit, Renderer2} from '@angular/core';
import {Seat} from '../../../dtos/seat';
import {Sector} from '../../../dtos/sector';
import {PriceCategory} from '../../../dtos/priceCategory';
import {TicketStatus} from '../../../datatype/ticket_status';
import {AuthService} from '../../../services/auth/auth.service';
import {TicketSessionService} from '../../../services/ticket-session/ticket-session.service';

@Component({
  selector: 'app-floorplan-svg',
  templateUrl: './floorplan-svg.component.html',
  styleUrls: ['./floorplan-svg.component.scss']
})
export class FloorplanSvgComponent implements OnInit, DoCheck, AfterViewInit {

  // seats and sectors are passed as input arguments. One of them should always be empty
  @Input() private seats?: Seat[];
  @Input() private sectors?: Sector[];
  // hallType as input to decide whether seats or sectors are drawn
  @Input() private hallType: string;
  @Input() private editingEnabled: boolean;
  @Input() private priceMapping: object;
  // properties necessary to adjust svg viebox on zoom/mousedrag
  private viewboxPosX: number = 0;
  private viewboxPosY: number = 0;
  private viewboxWidth: number = 500;
  // used to represent original width of viewbox which is necessary to correctly draw sectors
  private originalWidth: number = this.viewboxWidth;
  // initial svg viewbox. Used for property binding
  private viewbox: string = '0 0 500 500';
  // list of priceCategories to loop through in select fields
  // noinspection JSMismatchedCollectionQueryUpdate
  private priceCategories: string[] = Object.keys(PriceCategory);
  // currently selected element
  private selectedElement: Seat | Sector;
  // HTML/SVG representation of currently selected element
  private activeElement: HTMLElement;
  // used for template update form
  private updateElementModel: Seat | Sector = this.hallType === 'seats' ? new Seat(null, null, null, null, null, null)
    : new Sector(null, null, null, null, null, null, null);
  // updateSubmissionError is displayed if seat/sector with given number/row already exists in the hall
  private updateSubmissionError: boolean = false;
  private updateSubmissionMessage: string = '';
  // html elements often used within this component. Initialized on viewinit
  private svgElement: HTMLElement;
  private updateForm: HTMLElement;
  private contextmenu: HTMLElement;
  // needed to disable event listeners
  private disableListenerUpdate: Function;
  private disableListenerContext: Function;
  private disableMousemoveListener: Function;
  private disableMouseupListener: Function;
  // detects changes for seat/sector arrays
  private iterableDiffer;
  private oldHallType = this.hallType;

  constructor(private _iterableDiffers: IterableDiffers,
              private renderer: Renderer2,
              private authService: AuthService,
              private ticketSession: TicketSessionService) {
    this.iterableDiffer = _iterableDiffers.find([]).create(null);
  }

  /**
   * initializes class variable html elements
   */
  ngOnInit() {
    this.svgElement = document.getElementById('floorplan');
    this.updateForm = document.getElementById('updateForm');
    this.contextmenu = document.getElementById('contextmenu');
  }

  ngAfterViewInit(): void {
    // draw seats after view init otherwise they will sometimes not show up
    if (this.hallType === 'seats') {
      this.seats.forEach(seat => this.drawSeatPath(seat));
    } else if (this.hallType === 'sectors') {
      this.sectors.forEach(sector => this.drawSectorPath(sector));
    }
  }

  /**
   * detects changes to seat or sector arrays and draws or removes changed elements accordingly
   */
  ngDoCheck(): void {
    if (this.svgElement && this.hallType !== this.oldHallType) {
      this.oldHallType = this.hallType;
      while (this.svgElement.firstChild) {
        this.svgElement.removeChild(this.svgElement.firstChild);
      }
    }
    if (this.hallType === 'seats') {
      const changes = this.iterableDiffer.diff(this.seats);
      if (changes) {
        changes.forEachAddedItem(record => this.drawSeatPath(record.item));
        changes.forEachRemovedItem(record => this.removeSeatPath(record.item));
      }
    } else if (this.hallType === 'sectors') {
      const changes = this.iterableDiffer.diff(this.sectors);
      if (changes) {
        changes.forEachAddedItem(record => this.drawSectorPath(record.item));
        changes.forEachRemovedItem(record => this.removeSectorPath(record.item));
      }
    }
  }

  /**
   * checks if user currently logged in is admin
   */
  private isAdmin() {
    return this.authService.getUserRole() === 'ADMIN';
  }

  /**
   * draws representation of a seat as svg path element and appends it to floorplan svg
   * sets up event listeners for the element
   * @param seat to be drawn
   */
  private drawSeatPath(seat: Seat): HTMLElement {
    const seatElement = this.renderer.createElement('path', 'svg');
    this.renderer.setAttribute(seatElement, 'id', 'seat' + seat.seatRow + seat.seatNumber);
    const xPos = (seat.seatNumber) * 1.2 * 10 + Math.floor(seat.seatNumber / 15) * 10;
    const yPos = (seat.seatRow) * 1.2 * 10 + Math.floor(seat.seatRow / 10) * 10;
    let path: string = 'M ' + xPos + ' ' + yPos + ' h 10 v 10 h -10 Z';
    this.renderer.setAttribute(seatElement, 'fill', this.getColor(seat));
    if (this.allowEditing()) {
      this.renderer.listen(seatElement, 'click', (event) => this.displayUpdateForm(seat, event.target));
    } else {
      this.renderer.listen(seatElement, 'click', (event) => this.displayContext(seat, event));
    }
    this.renderer.listen(seatElement, 'contextmenu', (event) => this.displayContext(seat, event));
    this.renderer.appendChild(this.svgElement, seatElement);
    // display ticket differently if status is set
    if (seat.ticketStatus && seat.ticketStatus !== TicketStatus.EXPIRED) {
      this.renderer.addClass(seatElement, 'ticket');
      path += seat.ticketStatus === TicketStatus.SOLD ? ' l 10 10 m -10 0 l 10 -10' : '';
    }
    this.renderer.setAttribute(seatElement, 'd', path);
    return seatElement;
  }

  /**
   * draws representation of a sector as svg path element and appends it to the floorplan svg
   * sets up event listeners for the element
   * @param sector to be drawn
   */
  private drawSectorPath(sector: Sector): HTMLElement {
    const sectorElement = this.renderer.createElement('path', 'svg');
    this.renderer.setAttribute(sectorElement, 'id', 'sector' + sector.sectorNumber);
    const gap = this.originalWidth / 32;
    const width = this.originalWidth / 3.2;
    const xPos = ((sector.sectorNumber - 1) % 3) * (width + gap);
    const yPos = Math.floor((sector.sectorNumber - 1) / 3) * (50 + gap);
    let path: string = 'M ' + xPos + ' ' + yPos + ' h ' + width + ' v 50 h ' + (-width) + ' Z';
    this.renderer.setAttribute(sectorElement, 'fill', this.getColor(sector));
    if (this.allowEditing()) {
      this.renderer.listen(sectorElement, 'click', (event) => this.displayUpdateForm(sector, event.target));
    } else {
      this.renderer.listen(sectorElement, 'click', (event => this.displayContext(sector, event)));
    }
    this.renderer.listen(sectorElement, 'contextmenu', (event => this.displayContext(sector, event)));
    // display ticket differently if status is set
    if (sector.ticketsSold && sector.ticketsSold > 0) {
      this.renderer.addClass(sectorElement, 'ticket');
      path += sector.ticketsSold === sector.maxCapacity ? ' l ' + width + ' 50 m 0 -50 l ' + (-width) + ' 50' : '';
    }
    this.renderer.setAttribute(sectorElement, 'd', path);
    this.renderer.appendChild(this.svgElement, sectorElement);
    return sectorElement;
  }

  /**
   * removes svg path used to represent a seat
   * @param seat associated with the path element to be removed
   */
  private removeSeatPath(seat: Seat): void {
    const id: string = 'seat' + seat.seatRow + seat.seatNumber;
    if (id) {
      const path = document.getElementById(id);
      // NOTE: using renderer to remove elements here for some reason did not always work
      if (path) {
        this.svgElement.removeChild(path);
      }
    }
  }

  /**
   * removes svg path used to represent a sector
   * @param sector assoicated with the path element to be removed
   */
  private removeSectorPath(sector: Sector): void {
    const id: string = 'sector' + sector.sectorNumber;
    if (id) {
      this.renderer.removeChild(this.svgElement, document.getElementById(id));
      const path = document.getElementById(id);
      if (path) {
        this.svgElement.removeChild(path);
      }
    }
  }

  /**
   * displays update form near selected element and sets up updateElementModel with parameters to update selected element
   * @param element for which update form should be displayed
   * @param eventTarget target of click event used to get position of the element's path representation
   */
  displayUpdateForm(element: Seat | Sector, eventTarget: HTMLElement): boolean | void {
    if (this.isAdmin() && this.editingEnabled) {
      this.closeContext();
      this.renderer.setStyle(this.updateForm, 'display', 'inline-block');
      const rectForm = this.updateForm.getBoundingClientRect();
      const rectEvent = eventTarget.getBoundingClientRect();
      this.renderer.setStyle(this.updateForm, 'left', window.scrollX + rectEvent.left - rectForm.width / 2 + rectEvent.width / 2 + 'px');
      this.renderer.setStyle(this.updateForm, 'top', window.scrollY + rectEvent.bottom + 20 + 'px');
      this.updateElementModel = {...element};
      if (this.disableListenerUpdate) {
        this.disableListenerUpdate();
      }
      this.disableListenerUpdate = this.renderer.listen('document', 'click', (evt) => this.onCloseMenusClick(evt));
    }
    this.setActiveElement(element, eventTarget);
  }

  /**
   * displays contextmenu near selected element
   * @param element for which contextmenu should be displayed
   * @param event click event used to get position of the element's path representation
   */
  private displayContext(element: Seat | Sector, event: MouseEvent): void {
    event.preventDefault();
    this.closeUpdateForm();
    this.renderer.setStyle(this.contextmenu, 'left', event.pageX + 'px');
    this.renderer.setStyle(this.contextmenu, 'top', event.pageY + 'px');
    this.renderer.setStyle(this.contextmenu, 'display', 'inline-block');
    this.setActiveElement(element, event.target as HTMLElement);
    if (this.disableListenerContext) {
      this.disableListenerContext();
    }
    this.disableListenerContext = this.renderer.listen('document', 'click', (evt) => this.onCloseMenusClick(evt));
  }

  /**
   * sets a specific element as active
   * by setting selectedElement and its SVG/HTML representation activeElement
   * also adds css class active to activeElement
   * @param elem to be set to selectedElement
   * @param elemHTML to be set to activeElement
   */
  private setActiveElement(elem: Seat | Sector, elemHTML: HTMLElement) {
    if (this.activeElement) {
      this.renderer.removeClass(this.activeElement, 'active');
    }
    this.selectedElement = elem;
    this.activeElement = elemHTML;
    if (this.activeElement) {
      this.renderer.addClass(this.activeElement, 'active');
    }
  }

  /**
   * handles click event to close update and context menus
   * closes those menus on click if target is anything but those menus themselves
   * @param event click to handle and check target
   */
  private onCloseMenusClick(event: Event): void {
    const target = event.target as HTMLElement;
    if (!(target.tagName === 'path' || target.closest('#updateForm') || target.closest('#contextmenu'))) {
      this.closeUpdateForm();
      this.closeContext();
      this.setActiveElement(null, null);
    }
  }

  /**
   * hides/closes update form
   */
  private closeUpdateForm(): void {
    this.renderer.setStyle(this.updateForm, 'display', 'none');
    if (this.disableListenerUpdate) {
      this.disableListenerUpdate();
    }
  }

  /**
   * hides/closes contextmenu
   */
  private closeContext(): void {
    this.renderer.setStyle(this.contextmenu, 'display', 'none');
    if (this.disableListenerContext) {
      this.disableListenerContext();
    }
  }

  /**
   * updates seat or sector element with parameters found in updateElementModel
   * displays update form wherever selectedElement with updated parameters reappears
   */
  private updateSelectedElement(): void {
    if (this.isAdmin() && this.editingEnabled) {
      // check if selected element and updateElementModel have a value other than null/undefiend
      if (this.selectedElement && this.updateElementModel) {
        // check if currently displaying seat array in which case selectedElement must also be seat
        if (this.seats && this.seats.length) {
          const updateElement = this.updateElementModel as Seat;
          const selectedElement = this.selectedElement as Seat;
          // if seat with updatedElement's number and row already exists display error message
          if (this.seats.some(seat => seat !== selectedElement &&
            seat.seatNumber === updateElement.seatNumber &&
            seat.seatRow === updateElement.seatRow)) {
            this.updateSubmissionError = true;
            this.updateSubmissionMessage = 'A Seat with row ' + updateElement.seatRow +
              ' and number ' + updateElement.seatNumber + ' already exists!';
          } else {
            // remove old svg path of selected element, update selected element, draw new path and display updateForm again
            this.removeSeatPath(selectedElement);
            Object.assign(selectedElement, updateElement);
            this.displayUpdateForm(selectedElement, this.drawSeatPath(selectedElement));
            this.updateSubmissionError = false;
          }
          // check if currently displayingg sector array in which case selectedElement must also be sector
        } else if (this.sectors && this.sectors.length) {
          const updateElement = this.updateElementModel as Sector;
          const selectedElement = this.selectedElement as Sector;
          // if sector with updatedElement's sector number already exists display error message
          if (this.sectors.some(sector => sector !== selectedElement &&
            sector.sectorNumber === updateElement.sectorNumber)) {
            this.updateSubmissionError = true;
            this.updateSubmissionMessage = 'A Sector with number ' + updateElement.sectorNumber + ' already exists!';
          } else {
            // remove old svg path of selected element, update selected element, draw new path and display updateForm again
            this.removeSectorPath(selectedElement);
            Object.assign(selectedElement, updateElement);
            this.displayUpdateForm(selectedElement, this.drawSectorPath(selectedElement));
            this.updateSubmissionError = false;
          }
        }
      }
    }
  }

  /**
   * deletes selected element from seat/sector array
   */
  private deleteSelectedElement(): void {
    if (this.isAdmin() && this.editingEnabled) {
      if (this.selectedElement) {
        if (this.seats && this.seats.length) {
          this.seats.splice(this.seats.indexOf(this.selectedElement as Seat), 1);
        } else if (this.sectors && this.sectors.length) {
          this.sectors.splice(this.sectors.indexOf(this.selectedElement as Sector), 1);
        }
      }
    }
    this.closeContext();
  }

  /**
   * saves selected element in ticketSessionService as either seat or sector ticket
   */
  private addAsTicket(): void {
    // tickets can only be added and priceMapping exists i.e. if a show is selected
    if (!this.editingEnabled && this.priceMapping && this.selectedElement) {
      if (this.hallType === 'seats') {
        const seat = this.selectedElement as Seat;
        this.ticketSession.saveSeatTicket(seat);
        // set ticket status and redraw element
        seat.ticketStatus = TicketStatus.SELECTED;
        this.removeSeatPath(seat);
        this.drawSeatPath(seat);
      } else if (this.hallType === 'sectors') {
        const sector = this.selectedElement as Sector;
        this.ticketSession.saveSectorTicket(sector);
        // set ticket status and redraw element
        sector.ticketsSold = sector.ticketsSold ? sector.ticketsSold + 1 : 1;
        sector.ticketStatus = TicketStatus.SELECTED;
        this.removeSectorPath(sector);
        this.drawSectorPath(sector);
      }
    }
    this.closeContext();
  }

  /**
   * removes ticket from an element and redraws it
   */
  private removeTicket(): void {
    if (!this.editingEnabled && this.priceMapping && this.selectedElement) {
      this.ticketSession.deleteTicket(this.selectedElement);
      this.selectedElement.ticketStatus = null;
      if (this.hallType === 'seats') {
        const seat = this.selectedElement as Seat;
        this.removeSeatPath(seat);
        this.drawSeatPath(seat);
      } else if (this.hallType === 'sectors') {
        const sector = this.selectedElement as Sector;
        sector.ticketsSold = sector.ticketsSold ? sector.ticketsSold - 1 : null;
        sector.ticketStatus = null;
        this.removeSectorPath(sector);
        this.drawSectorPath(sector);
      }
    }
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
   * allows users to zoom in by adjusting svg viewbox
   * also closes updateForm and contextmenu because their positions wouldn't make sense otherwise
   * @param event mousewheel event used to zoom
   */
  private zoom(event) {
    event.preventDefault();
    this.viewboxWidth = event.deltaY > 0 ? this.viewboxWidth * 1.05 : this.viewboxWidth / 1.05;
    this.viewboxWidth = this.viewboxWidth < 300 ? 300 : (this.viewboxWidth > 1310 ? 1310 : this.viewboxWidth);
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
    this.closeUpdateForm();
    this.closeContext();
  }

  /**
   * called on mousemove event after mousedown in svg
   * allows users to change viewbox by dragging it
   * also closes updateForm and contextmenu because their positions wouldn't make sense otherwise
   * @param event mousemove event used to drag viewbox
   */
  private svgDrag(event: MouseEvent): void {
    this.viewboxPosX -= event.movementX / 2;
    this.viewboxPosY -= event.movementY / 2;
    this.viewboxPosX = this.viewboxPosX < 0 ? 0 : this.viewboxPosX;
    this.viewboxPosY = this.viewboxPosY < 0 ? 0 : this.viewboxPosY;
    this.viewbox = this.viewboxPosX + ' ' + this.viewboxPosY + ' ' + this.viewboxWidth + ' ' + this.viewboxWidth;
    this.closeUpdateForm();
    this.closeContext();
  }

  /**
   * called on mousedown event  in svg
   * initializes viewbox drag on mousemove by adding eventlisteners to mousemove event and mouseup event to end mousedrag
   * also adds class 'grabbing' to svg to display cursor: grabbing
   */
  private svgDragInit(): void {
    this.disableMousemoveListener = this.renderer.listen(this.svgElement, 'mousemove', (event) => this.svgDrag(event));
    this.disableMouseupListener = this.renderer.listen('document', 'mouseup', () => this.svgDragExit());
    this.renderer.addClass(this.svgElement, 'grabbing');
  }

  /**
   * called on mouseup event in svg
   * ends viewbox dragging by removing mousemove and mouseup event listeners
   * also removes class 'grabbing' from svg to display cursor: grabbing
   */
  private svgDragExit(): void {
    if (this.disableMousemoveListener) {
      this.disableMousemoveListener();
    }
    if (this.disableMouseupListener) {
      this.disableMouseupListener();
    }
    this.renderer.removeClass(this.svgElement, 'grabbing');
  }

  /**
   * function returning boolean which determines if user is currently allowed to add a ticket to the selected seat or sector
   */
  private allowToAddTicket(): boolean {
    if (!this.selectedElement || this.editingEnabled || !this.priceMapping) {
      return false;
    }
    if (this.hallType === 'seats') {
      return !this.selectedElement.ticketStatus || this.selectedElement.ticketStatus === TicketStatus.EXPIRED;
    } else {
      const sector = this.selectedElement as Sector;
      return sector.ticketsSold ? sector.ticketsSold < sector.maxCapacity : true;
    }
  }

  /**
   * function returning boolean which determines if user is currently allowed to remove a ticket from the selected seat or sector
   */
  private allowDeleteTicket(): boolean {
    if (!this.selectedElement || this.editingEnabled || !this.priceMapping) {
      return false;
    }
    return this.selectedElement.ticketStatus === 'SELECTED';
  }

  /**
   * function checks if editing is currently enabled on the selected hall and given the current user
   */
  private allowEditing(): boolean {
    return this.editingEnabled && this.isAdmin();
  }

  /**
   * display function
   * returns string to display tickets sold/max capacity for sector
   */
  private getTicketsSoldAndCapacity(): string {
    const sector = this.selectedElement as Sector;
    // tslint:disable-next-line:max-line-length
    return sector && sector.ticketsSold ? sector.ticketsSold + '/' + sector.maxCapacity + ' sold' : (sector ? 'Capacity: ' + sector.maxCapacity : '');
  }

  /**
   * maps price category to number or just returns price category name if no price mapping is set
   */
  private getPrice(price: string): string {
    return this.priceMapping ? this.priceMapping[price] + ' €' : price;
  }
}
