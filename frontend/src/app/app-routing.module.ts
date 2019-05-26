import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {NewsComponent} from './components/news/news.component';
import {UserComponent} from './components/user/user.component';
import {TopTenEventsComponent} from './components/events/top-ten-events/top-ten-events.component';
import {CustomerAddComponent} from './components/customerAdd/customer-add.component';
import {SearchEventsComponent} from './components/events/search-events/search-events.component';
import {ShowComponent} from './components/search-results/shows/show.component';
import {ArtistResultsComponent} from './components/search-results/artists/artist-results.component';
import {EventsComponent} from './components/events/start page/events.component';
import {LocationsComponent} from './components/search-results/locations/locations.component';
import {EventResultsComponent} from './components/search-results/events/event-results.component';
import {FloorplanControlComponent} from './components/floorplan-control/floorplan-control.component';

// TODO Refactor Names: ShowComponent -> ShowResultsComponent; the same for LocationComponent; in Service as well
const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'news', canActivate: [AuthGuard], component: NewsComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'customer-add', canActivate: [AuthGuard], component: CustomerAddComponent},
  {path: 'events', canActivate: [AuthGuard], component: EventsComponent},
  {path: 'events/topten', canActivate: [AuthGuard], component: TopTenEventsComponent},
  {path: 'events/search', canActivate: [AuthGuard], component: SearchEventsComponent},
  {path: 'events/search/results/artists', canActivate: [AuthGuard], component: ArtistResultsComponent},
  {path: 'events/search/results/shows', canActivate: [AuthGuard], component: ShowComponent},
  {path: 'events/search/results/locations', canActivate: [AuthGuard], component: LocationsComponent},
  {path: 'events/search/results/events', canActivate: [AuthGuard], component: EventResultsComponent}
  {path: 'floorplan', canActivate: [AuthGuard], component: FloorplanControlComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
