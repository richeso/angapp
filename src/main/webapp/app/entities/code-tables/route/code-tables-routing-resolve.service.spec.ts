jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICodeTables, CodeTables } from '../code-tables.model';
import { CodeTablesService } from '../service/code-tables.service';

import { CodeTablesRoutingResolveService } from './code-tables-routing-resolve.service';

describe('CodeTables routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: CodeTablesRoutingResolveService;
  let service: CodeTablesService;
  let resultCodeTables: ICodeTables | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(CodeTablesRoutingResolveService);
    service = TestBed.inject(CodeTablesService);
    resultCodeTables = undefined;
  });

  describe('resolve', () => {
    it('should return ICodeTables returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodeTables = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCodeTables).toEqual({ id: 123 });
    });

    it('should return new ICodeTables if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodeTables = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultCodeTables).toEqual(new CodeTables());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as CodeTables })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultCodeTables = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultCodeTables).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
