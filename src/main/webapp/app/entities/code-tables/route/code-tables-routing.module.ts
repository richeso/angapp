import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CodeTablesComponent } from '../list/code-tables.component';
import { CodeTablesDetailComponent } from '../detail/code-tables-detail.component';
import { CodeTablesUpdateComponent } from '../update/code-tables-update.component';
import { CodeTablesRoutingResolveService } from './code-tables-routing-resolve.service';

const codeTablesRoute: Routes = [
  {
    path: '',
    component: CodeTablesComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CodeTablesDetailComponent,
    resolve: {
      codeTables: CodeTablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CodeTablesUpdateComponent,
    resolve: {
      codeTables: CodeTablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CodeTablesUpdateComponent,
    resolve: {
      codeTables: CodeTablesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(codeTablesRoute)],
  exports: [RouterModule],
})
export class CodeTablesRoutingModule {}
