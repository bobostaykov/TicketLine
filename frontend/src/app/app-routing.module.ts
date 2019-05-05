import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard, AuthGuardUser} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {UserComponent} from './components/user/user.component';
import {CustomerAddComponent} from "./components/customerAdd/customer-add.component";

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'user', canActivate: [AuthGuard], component: UserComponent},
  {path: 'customer-add', canActivate: [AuthGuard], component: CustomerAddComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
