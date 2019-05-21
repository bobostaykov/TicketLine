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
import {MatButtonModule, MatCheckboxModule, MatInputModule, MatSelectModule} from '@angular/material';
import {NgxChartsModule} from '@swimlane/ngx-charts';
import { CustomerAddComponent } from './components/customerAdd/customer-add.component';
import { CustomerComponent } from './components/customer/customer.component';
import { CustomerDialogComponent } from './components/customer-dialog/customer-dialog.component';
import { BlockedUsersComponent } from './components/blocked-users/blocked-users.component';
import { SearchEventsComponent } from './components/events/search-events/search-events.component';
import { ArtistResultsComponent } from './components/search-results/artist/artist-results.component';
import { ShowComponent } from './components/show/show.component';
import { EventsComponent } from './components/events/start page/events.component';

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
    CustomerAddComponent,
    SearchEventsComponent,
    ArtistResultsComponent,
    ShowComponent,
    EventsComponent,
    CustomerComponent,
    CustomerDialogComponent,
    BlockedUsersComponent,
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
    NgxChartsModule,
  ],
  providers: [httpInterceptorProviders, Globals],
  bootstrap: [AppComponent]
})
export class AppModule {
}
