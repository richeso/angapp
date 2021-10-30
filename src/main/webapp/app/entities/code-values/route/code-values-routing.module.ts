import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CodeValuesComponent } from '../list/code-values.component';
import { CodeValuesDetailComponent } from '../detail/code-values-detail.component';
import { CodeValuesUpdateComponent } from '../update/code-values-update.component';
import { CodeValuesRoutingResolveService } from './code-values-routing-resolve.service';

const codeValuesRoute: Routes = [
  {
    path: '',
    component: CodeValuesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CodeValuesDetailComponent,
    resolve: {
      codeValues: CodeValuesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CodeValuesUpdateComponent,
    resolve: {
      codeValues: CodeValuesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CodeValuesUpdateComponent,
    resolve: {
      codeValues: CodeValuesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(codeValuesRoute)],
  exports: [RouterModule],
})
export class CodeValuesRoutingModule {}
