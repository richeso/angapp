import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IMaprRequests, MaprRequests } from '../mapr-requests.model';

import { MaprRequestsService } from './mapr-requests.service';

describe('MaprRequests Service', () => {
  let service: MaprRequestsService;
  let httpMock: HttpTestingController;
  let elemDefault: IMaprRequests;
  let expectedResult: IMaprRequests | IMaprRequests[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MaprRequestsService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      type: 'AAAAAAA',
      action: 'AAAAAAA',
      name: 'AAAAAAA',
      path: 'AAAAAAA',
      requestUser: 'AAAAAAA',
      requestDate: currentDate,
      status: 'AAAAAAA',
      statusDate: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          requestDate: currentDate.format(DATE_TIME_FORMAT),
          statusDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a MaprRequests', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          requestDate: currentDate.format(DATE_TIME_FORMAT),
          statusDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          requestDate: currentDate,
          statusDate: currentDate,
        },
        returnedFromService
      );

      service.create(new MaprRequests()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MaprRequests', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
          action: 'BBBBBB',
          name: 'BBBBBB',
          path: 'BBBBBB',
          requestUser: 'BBBBBB',
          requestDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          statusDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          requestDate: currentDate,
          statusDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MaprRequests', () => {
      const patchObject = Object.assign(
        {
          name: 'BBBBBB',
          requestUser: 'BBBBBB',
          requestDate: currentDate.format(DATE_TIME_FORMAT),
          statusDate: currentDate.format(DATE_TIME_FORMAT),
        },
        new MaprRequests()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          requestDate: currentDate,
          statusDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MaprRequests', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          type: 'BBBBBB',
          action: 'BBBBBB',
          name: 'BBBBBB',
          path: 'BBBBBB',
          requestUser: 'BBBBBB',
          requestDate: currentDate.format(DATE_TIME_FORMAT),
          status: 'BBBBBB',
          statusDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          requestDate: currentDate,
          statusDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a MaprRequests', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addMaprRequestsToCollectionIfMissing', () => {
      it('should add a MaprRequests to an empty array', () => {
        const maprRequests: IMaprRequests = { id: 123 };
        expectedResult = service.addMaprRequestsToCollectionIfMissing([], maprRequests);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maprRequests);
      });

      it('should not add a MaprRequests to an array that contains it', () => {
        const maprRequests: IMaprRequests = { id: 123 };
        const maprRequestsCollection: IMaprRequests[] = [
          {
            ...maprRequests,
          },
          { id: 456 },
        ];
        expectedResult = service.addMaprRequestsToCollectionIfMissing(maprRequestsCollection, maprRequests);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MaprRequests to an array that doesn't contain it", () => {
        const maprRequests: IMaprRequests = { id: 123 };
        const maprRequestsCollection: IMaprRequests[] = [{ id: 456 }];
        expectedResult = service.addMaprRequestsToCollectionIfMissing(maprRequestsCollection, maprRequests);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maprRequests);
      });

      it('should add only unique MaprRequests to an array', () => {
        const maprRequestsArray: IMaprRequests[] = [{ id: 123 }, { id: 456 }, { id: 56896 }];
        const maprRequestsCollection: IMaprRequests[] = [{ id: 123 }];
        expectedResult = service.addMaprRequestsToCollectionIfMissing(maprRequestsCollection, ...maprRequestsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const maprRequests: IMaprRequests = { id: 123 };
        const maprRequests2: IMaprRequests = { id: 456 };
        expectedResult = service.addMaprRequestsToCollectionIfMissing([], maprRequests, maprRequests2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(maprRequests);
        expect(expectedResult).toContain(maprRequests2);
      });

      it('should accept null and undefined values', () => {
        const maprRequests: IMaprRequests = { id: 123 };
        expectedResult = service.addMaprRequestsToCollectionIfMissing([], null, maprRequests, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(maprRequests);
      });

      it('should return initial array if no MaprRequests is added', () => {
        const maprRequestsCollection: IMaprRequests[] = [{ id: 123 }];
        expectedResult = service.addMaprRequestsToCollectionIfMissing(maprRequestsCollection, undefined, null);
        expect(expectedResult).toEqual(maprRequestsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
