import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {NewsComponent} from './components/news/news.component';
import {UserComponent} from './components/user/user.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import {Globals} from './global/globals';
import {TopTenEventsComponent} from './components/events/top-ten-events/top-ten-events.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  MatButtonModule,
  MatCheckboxModule,
  MatInputModule,
  MatSelectModule,
  MatSnackBarModule,
  MatRadioModule,
  MatAutocompleteModule, MatIconModule
} from '@angular/material';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import {SearchPageComponent} from './components/events/search page/search-page.component';
import {ArtistResultsComponent} from './components/search-results/artists/artist-results.component';
import {ShowResultsComponent} from './components/search-results/shows/show-results.component';
import {EventsComponent} from './components/events/start page/events.component';
import {LocationResultsComponent} from './components/search-results/locations/location-results.component';
import {Ng5SliderModule} from 'ng5-slider';
import {EventResultsComponent} from './components/search-results/events/event-results.component';
import {FloorplanControlComponent} from './components/floorplan/floorplan-control/floorplan-control.component';
import {FloorplanSvgComponent} from './components/floorplan/floorplan-svg/floorplan-svg.component';
import {CustomerComponent} from './components/customer/customer.component';
import {TicketComponent} from './components/ticket/ticket.component';
import {CustomerDialogComponent} from './components/customer-dialog/customer-dialog.component';
import {TicketCheckReservationComponent} from './components/ticket/ticket-check/ticket-check.component';
import {FloorplanTicketComponent} from './components/floorplan/floorplan-ticket/floorplan-ticket.component';
import {BlockedUsersComponent} from './components/blocked-users/blocked-users.component';
import {MinDirective} from './directives/min.directive';
import {ContentManagerComponent} from './components/content-manager/content-manager.component';
import {SelectDropDownModule} from 'ngx-select-dropdown';
import {ChangePasswordDialogComponent } from './components/change-password-dialog/change-password-dialog.component';
import {ReservationAndCancellationComponent} from './components/reservation-and-cancellation/reservation-and-cancellation.component';
import {ShowDialogComponent} from './components/show-dialog/show-dialog.component';
import {ErrorSnackBarComponent} from './components/error-snack-bar/error-snack-bar.component';
import {EventDialogComponent} from './components/event-dialog/event-dialog.component';
import {LocationDialogComponent} from './components/location-dialog/location-dialog.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    NewsComponent,
    UserComponent,
    TopTenEventsComponent,
    SearchPageComponent,
    ArtistResultsComponent,
    ShowResultsComponent,
    EventsComponent,
    LocationResultsComponent,
    EventResultsComponent,
    FloorplanControlComponent,
    FloorplanSvgComponent,
    CustomerComponent,
    TicketComponent,
    CustomerDialogComponent,
    FloorplanTicketComponent,
    MinDirective,
    BlockedUsersComponent,
    TicketCheckReservationComponent,
    ContentManagerComponent,
    BlockedUsersComponent,
    ChangePasswordDialogComponent,
    ContentManagerComponent,
    ShowDialogComponent,
    ContentManagerComponent,
    EventDialogComponent,
    ErrorSnackBarComponent,
    LocationDialogComponent,
    ContentManagerComponent,
    LocationDialogComponent,
    ReservationAndCancellationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    MatButtonModule,
    MatCheckboxModule,
    MatInputModule,
    MatSelectModule,
    MatSnackBarModule,
    NgxChartsModule,
    Ng5SliderModule,
    MatRadioModule,
    MatAutocompleteModule,
    SelectDropDownModule,
    MatIconModule
  ],
  providers: [httpInterceptorProviders, Globals],
  bootstrap: [AppComponent],
  entryComponents: [ErrorSnackBarComponent]
})
export class AppModule {
}
