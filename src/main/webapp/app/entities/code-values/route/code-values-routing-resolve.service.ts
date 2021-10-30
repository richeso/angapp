import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICodeValues, CodeValues } from '../code-values.model';
import { CodeValuesService } from '../service/code-values.service';

@Injectable({ providedIn: 'root' })
export class CodeValuesRoutingResolveService implements Resolve<ICodeValues> {
  constructor(protected service: CodeValuesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICodeValues> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((codeValues: HttpResponse<CodeValues>) => {
          if (codeValues.body) {
            return of(codeValues.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CodeValues());
  }
}
