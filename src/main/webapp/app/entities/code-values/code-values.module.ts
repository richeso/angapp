import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CodeValuesComponent } from './list/code-values.component';
import { CodeValuesDetailComponent } from './detail/code-values-detail.component';
import { CodeValuesUpdateComponent } from './update/code-values-update.component';
import { CodeValuesDeleteDialogComponent } from './delete/code-values-delete-dialog.component';
import { CodeValuesRoutingModule } from './route/code-values-routing.module';

@NgModule({
  imports: [SharedModule, CodeValuesRoutingModule],
  declarations: [CodeValuesComponent, CodeValuesDetailComponent, CodeValuesUpdateComponent, CodeValuesDeleteDialogComponent],
  entryComponents: [CodeValuesDeleteDialogComponent],
})
export class CodeValuesModule {}
