import {Component, Input, OnInit} from '@angular/core';
import {Sector} from '../../../dtos/sector';
import {Seat} from '../../../dtos/seat';

@Component({
  selector: 'app-floorplan-ticket',
  templateUrl: './floorplan-ticket.component.html',
  styleUrls: ['./floorplan-ticket.component.scss']
})
export class FloorplanTicketComponent implements OnInit {

  @Input() private tickets: Array<Seat | Sector>;
  private selectedTicket: Seat | Sector;
  private _closeContext = this.closeContext.bind(this);

  constructor() {
  }


  ngOnInit() {
  }

  private deleteSelectedTicket(): void {
    this.tickets.splice(this.tickets.indexOf(this.selectedTicket), 1);
  }

  private displayContextmenu(ticket: Seat | Sector, event: MouseEvent): void {
    event.preventDefault();
    if (event.target instanceof Element) {
      const context = document.getElementById('contextmenuTicket');
      context.style.left = event.pageX + 'px';
      context.style.top = event.pageY + 'px';
      context.style.display = 'inline-block';
      this.selectedTicket = ticket;
      document.addEventListener('click', this._closeContext);
    }
  }

  private closeContext(event: MouseEvent): void {
    if (! (event.target as HTMLElement).closest('.ticket')) {
      document.getElementById('contextmenuTicket').style.display = 'none';
      document.removeEventListener('click', this._closeContext);
    }
  }
}
