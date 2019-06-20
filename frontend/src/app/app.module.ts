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
import { TopTenEventsComponent } from './components/events/top-ten-events/top-ten-events.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatButtonModule, MatCheckboxModule, MatInputModule, MatSelectModule, MatSnackBarModule} from '@angular/material';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import { SearchPageComponent } from './components/events/search page/search-page.component';
import { ArtistResultsComponent } from './components/search-results/artists/artist-results.component';
import { ShowResultsComponent } from './components/search-results/shows/show-results.component';
import { EventsComponent } from './components/events/start page/events.component';
import { LocationResultsComponent } from './components/search-results/locations/location-results.component';
import { Ng5SliderModule } from 'ng5-slider';
import { EventResultsComponent } from './components/search-results/events/event-results.component';
import {FloorplanControlComponent} from './components/floorplan-control/floorplan-control.component';
import {FloorplanComponent} from './components/floorplan/floorplan.component';
import {CustomerComponent} from './components/customer/customer.component';
import {CustomerDialogComponent} from './components/customer-dialog/customer-dialog.component';
import { BlockedUsersComponent } from './components/blocked-users/blocked-users.component';
import { ContentManagerComponent } from './components/content-manager/content-manager.component';
import { SelectDropDownModule } from 'ngx-select-dropdown'

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
    FloorplanComponent,
    CustomerComponent,
    CustomerDialogComponent,
    BlockedUsersComponent,
    ContentManagerComponent
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
    SelectDropDownModule
  ],
  entryComponents: [
    CustomerDialogComponent
  ],
  providers: [httpInterceptorProviders, Globals],
  bootstrap: [AppComponent]
})
export class AppModule {
}
