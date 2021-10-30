import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'code-tables',
        data: { pageTitle: 'mgrwebApp.codeTables.home.title' },
        loadChildren: () => import('./code-tables/code-tables.module').then(m => m.CodeTablesModule),
      },
      {
        path: 'code-values',
        data: { pageTitle: 'mgrwebApp.codeValues.home.title' },
        loadChildren: () => import('./code-values/code-values.module').then(m => m.CodeValuesModule),
      },
      {
        path: 'mapr-requests',
        data: { pageTitle: 'mgrwebApp.maprRequests.home.title' },
        loadChildren: () => import('./mapr-requests/mapr-requests.module').then(m => m.MaprRequestsModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
