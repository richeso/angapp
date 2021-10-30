import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodeTables, CodeTables } from '../code-tables.model';
import { CodeTablesService } from '../service/code-tables.service';

@Injectable({ providedIn: 'root' })
export class CodeTablesRoutingResolveService implements Resolve<ICodeTables> {
  constructor(protected service: CodeTablesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICodeTables> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((codeTables: HttpResponse<CodeTables>) => {
          if (codeTables.body) {
            return of(codeTables.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CodeTables());
  }
}
