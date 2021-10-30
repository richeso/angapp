import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICodeValues, getCodeValuesIdentifier } from '../code-values.model';

export type EntityResponseType = HttpResponse<ICodeValues>;
export type EntityArrayResponseType = HttpResponse<ICodeValues[]>;

@Injectable({ providedIn: 'root' })
export class CodeValuesService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/code-values');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(codeValues: ICodeValues): Observable<EntityResponseType> {
    return this.http.post<ICodeValues>(this.resourceUrl, codeValues, { observe: 'response' });
  }

  update(codeValues: ICodeValues): Observable<EntityResponseType> {
    return this.http.put<ICodeValues>(`${this.resourceUrl}/${getCodeValuesIdentifier(codeValues) as number}`, codeValues, {
      observe: 'response',
    });
  }

  partialUpdate(codeValues: ICodeValues): Observable<EntityResponseType> {
    return this.http.patch<ICodeValues>(`${this.resourceUrl}/${getCodeValuesIdentifier(codeValues) as number}`, codeValues, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ICodeValues>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICodeValues[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCodeValuesToCollectionIfMissing(
    codeValuesCollection: ICodeValues[],
    ...codeValuesToCheck: (ICodeValues | null | undefined)[]
  ): ICodeValues[] {
    const codeValues: ICodeValues[] = codeValuesToCheck.filter(isPresent);
    if (codeValues.length > 0) {
      const codeValuesCollectionIdentifiers = codeValuesCollection.map(codeValuesItem => getCodeValuesIdentifier(codeValuesItem)!);
      const codeValuesToAdd = codeValues.filter(codeValuesItem => {
        const codeValuesIdentifier = getCodeValuesIdentifier(codeValuesItem);
        if (codeValuesIdentifier == null || codeValuesCollectionIdentifiers.includes(codeValuesIdentifier)) {
          return false;
        }
        codeValuesCollectionIdentifiers.push(codeValuesIdentifier);
        return true;
      });
      return [...codeValuesToAdd, ...codeValuesCollection];
    }
    return codeValuesCollection;
  }
}
