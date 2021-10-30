import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CodeTablesComponent } from './list/code-tables.component';
import { CodeTablesDetailComponent } from './detail/code-tables-detail.component';
import { CodeTablesUpdateComponent } from './update/code-tables-update.component';
import { CodeTablesDeleteDialogComponent } from './delete/code-tables-delete-dialog.component';
import { CodeTablesRoutingModule } from './route/code-tables-routing.module';

@NgModule({
  imports: [SharedModule, CodeTablesRoutingModule],
  declarations: [CodeTablesComponent, CodeTablesDetailComponent, CodeTablesUpdateComponent, CodeTablesDeleteDialogComponent],
  entryComponents: [CodeTablesDeleteDialogComponent],
})
export class CodeTablesModule {}
