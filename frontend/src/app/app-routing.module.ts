import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard, AuthGuardUser} from './guards/auth.guard';
import {NewsComponent} from './components/news/news.component';
import {UserComponent} from './components/user/user.component';
import {TopTenEventsComponent} from './components/events/top-ten-events/top-ten-events.component';
import {CustomerAddComponent} from './components/customerAdd/customer-add.component';
import {CustomerComponent} from './components/customer/customer.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'news', canActivate: [AuthGuard], component: NewsComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'customer-add', canActivate: [AuthGuard], component: CustomerAddComponent},
  {path: 'customers', canActivate: [AuthGuard], component: CustomerComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'events/topten', canActivate: [AuthGuard], component: TopTenEventsComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
