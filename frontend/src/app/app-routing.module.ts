import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard, AuthGuardUser} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {UserComponent} from './components/user/user.component';
import {TopTenEventsComponent} from './components/events/top-ten-events/top-ten-events.component';
import {CustomerAddComponent} from './components/customerAdd/customer-add.component';
import {CustomerComponent} from './components/customer/customer.component';
import {FloorplanComponent} from './components/floorplan/floorplan.component';
import {FloorplanControlComponent} from './components/floorplan-control/floorplan-control.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'customer-add', canActivate: [AuthGuard], component: CustomerAddComponent},
  {path: 'customers', canActivate: [AuthGuard], component: CustomerComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'events/topten', canActivate: [AuthGuard], component: TopTenEventsComponent},
  {path: 'floorplan', canActivate: [AuthGuard], component: FloorplanControlComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
