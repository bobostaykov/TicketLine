import {Component, Input, OnInit} from '@angular/core';
import {Sector} from '../../../dtos/sector';
import {Seat} from '../../../dtos/seat';
import {TicketSessionService} from '../../../services/ticket-session/ticket-session.service';

@Component({
  selector: 'app-floorplan-ticket',
  templateUrl: './floorplan-ticket.component.html',
  styleUrls: ['./floorplan-ticket.component.scss']
})
export class FloorplanTicketComponent implements OnInit {
  // stepProgress can be used to showcase how many steps along the way to selling tickets the user already is
  // when embedding this component in different parts of the app
  @Input() private stepProgress: number;
  @Input() private allowDeleteTickets: boolean;
  private selectedTicket: Seat | Sector;
  // event listener to close contextmenu on click anywhere in the document
  private _closeContext = this.closeContext.bind(this);

  constructor(private ticketsession: TicketSessionService) {
  }

  ngOnInit() {
  }

  /**
   * deletes selected ticket from the ticket arrays stored in ticketsession
   * called via contextmenu
   */
  private deleteSelectedTicket(): void {
    if (this.selectedTicket && this.allowDeleteTickets) {
      this.ticketsession.deleteTicket(this.selectedTicket);
    }
  }

  /**
   * displays contextmenu near selected ticket on click
   * @param ticket that was selected
   * @param event click event containing location which will be used to determine where the contextmenu is placed
   */
  private displayContextmenu(ticket: Seat | Sector, event: MouseEvent): void {
    if (this.allowDeleteTickets) {
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
  }

  /**
   * closes contextmenu on click anywhere but the contextmenu
   * @param event click event to check if click was inside or outside contextmenu
   */
  private closeContext(event: MouseEvent): void {
    if (!(event.target as HTMLElement).closest('.ticket')) {
      const context = document.getElementById('contextmenuTicket');
      if (context) {
        context.style.display = 'none';
        document.removeEventListener('click', this._closeContext);
      }
    }
  }
}
