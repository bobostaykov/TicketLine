import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {NewsComponent} from './components/news/news.component';
import {UserComponent} from './components/user/user.component';
import {TopTenEventsComponent} from './components/events/top-ten-events/top-ten-events.component';
import {CustomerAddComponent} from './components/customerAdd/customer-add.component';
import {TicketComponent} from './components/ticket/ticket.component';
import {ShowComponent} from './components/show/show.component';
import {ArtistResultsComponent} from './components/search-results/artist/artist-results.component';
import {EventsComponent} from './components/events/start page/events.component';
import {SearchEventsComponent} from './components/events/search-events/search-events.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'news', canActivate: [AuthGuard], component: NewsComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'events/topten', canActivate: [AuthGuard], component: TopTenEventsComponent},
  {path: 'customer-add', canActivate: [AuthGuard], component: CustomerAddComponent},
  {path: 'events/search', canActivate: [AuthGuard], component: SearchEventsComponent},
  {path: 'events/search/results/artist', canActivate: [AuthGuard], component: ArtistResultsComponent},
  {path: 'shows', canActivate: [AuthGuard], component: ShowComponent},
  {path: 'events', canActivate: [AuthGuard], component: EventsComponent}
  {path: 'ticket', canActivate: [AuthGuard], component: TicketComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
