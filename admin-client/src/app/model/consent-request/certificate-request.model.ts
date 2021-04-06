export interface CertificateRequest{
    commonName: string,
    surname: string,
    givenName: string,
    organization: string,
    organizationUnit: string,
    country: string,
    userId: number,
    email: string
}

export interface PersonModel{
    email: string,
    name: string,
    surname: string
}