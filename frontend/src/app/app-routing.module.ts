import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {NewsComponent} from './components/news/news.component';
import {UserComponent} from './components/user/user.component';
import {TopTenEventsComponent} from './components/events/top-ten-events/top-ten-events.component';
import {SearchPageComponent} from './components/events/search page/search-page.component';
import {ShowResultsComponent} from './components/search-results/shows/show-results.component';
import {ArtistResultsComponent} from './components/search-results/artists/artist-results.component';
import {EventsComponent} from './components/events/start page/events.component';
import {LocationResultsComponent} from './components/search-results/locations/location-results.component';
import {EventResultsComponent} from './components/search-results/events/event-results.component';
import {FloorplanControlComponent} from './components/floorplan/floorplan-control/floorplan-control.component';
import {CustomerComponent} from './components/customer/customer.component';
import {TicketComponent} from './components/ticket/ticket.component';
import {TicketCheckReservationComponent} from './components/ticket/ticket-check/ticket-check.component';
import {BlockedUsersComponent} from './components/blocked-users/blocked-users.component';
import {ContentManagerComponent} from './components/content-manager/content-manager.component';
import {ReservationAndCancellationComponent} from './components/reservation-and-cancellation/reservation-and-cancellation.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'news', canActivate: [AuthGuard], component: NewsComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'customers', canActivate: [AuthGuard], component: CustomerComponent},
  {path: 'events', canActivate: [AuthGuard], component: SearchPageComponent                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   },
  {path: 'events/topten', canActivate: [AuthGuard], component: TopTenEventsComponent},
  {path: 'events/search', canActivate: [AuthGuard], component: SearchPageComponent},
  {path: 'events/search/results/artists', canActivate: [AuthGuard], component: ArtistResultsComponent},
  {path: 'events/search/results/shows', canActivate: [AuthGuard], component: ShowResultsComponent},
  {path: 'events/search/results/locations', canActivate: [AuthGuard], component: LocationResultsComponent},
  {path: 'events/search/results/events', canActivate: [AuthGuard], component: EventResultsComponent},
  {path: 'ticket', canActivate: [AuthGuard], component: TicketComponent},
  {path: 'ticket-check', canActivate: [AuthGuard], component: TicketCheckReservationComponent},
  {path: 'floorplan', canActivate: [AuthGuard], component: FloorplanControlComponent},
  {path: 'floorplan/:show_id', canActivate: [AuthGuard], component: FloorplanControlComponent},
  {path: 'ticket', canActivate: [AuthGuard], component: TicketComponent},
  {path: 'manager', canActivate: [AuthGuard], component: ContentManagerComponent},
  {path: 'ticket-check', canActivate: [AuthGuard], component: TicketCheckReservationComponent},
  {path: 'blocked', canActivate: [AuthGuard], component: BlockedUsersComponent},
  {path: 'ticketsearch', canActivate: [AuthGuard], component: ReservationAndCancellationComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
