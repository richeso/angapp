import * as dayjs from 'dayjs';

export interface IMaprRequests {
  id?: number;
  type?: string;
  action?: string;
  name?: string;
  path?: string;
  requestUser?: string;
  requestDate?: dayjs.Dayjs;
  status?: string;
  statusDate?: dayjs.Dayjs;
}

export class MaprRequests implements IMaprRequests {
  constructor(
    public id?: number,
    public type?: string,
    public action?: string,
    public name?: string,
    public path?: string,
    public requestUser?: string,
    public requestDate?: dayjs.Dayjs,
    public status?: string,
    public statusDate?: dayjs.Dayjs
  ) {}
}

export function getMaprRequestsIdentifier(maprRequests: IMaprRequests): number | undefined {
  return maprRequests.id;
}
