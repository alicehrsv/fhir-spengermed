export interface Bundle {
  resourceType: string;
  type: string;
  total: number;
  link: BundleLink[];
  entry: BundleEntry[];
}

export interface BundleLink {
  relation: string;
  url: string;
}

export interface BundleEntry {
  fullUrl: string;
  resource: any;
}
