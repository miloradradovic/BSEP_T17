export interface CertificateRequest{
    id: number,
    commonName: string,
    surname: string,
    givenName: string,
    organization: string,
    organizationUnit: string,
    country: string,
    userId: number,
    email: string
}

export interface Certificate {
    commonName: string,
    fullName: string,
    email: string,
    revoked: boolean,
    revocationReason: string,
    ca: boolean,
    alias: string,
    children: Certificate[]
}

export interface PersonModel{
    email: string,
    name: string,
    surname: string
}

export interface CertificateCreation{
    subjectID: number,
    keyUsageDTO: KeyUsages,
    extendedKeyUsageDTO: ExtendedKeyUsages
} 

export interface KeyUsages {
    cRLSign: boolean,
    dataEncipherment: boolean,
    decipherOnly: boolean,
    digitalSignature: boolean,
    encipherOnly: boolean,
    keyAgreement: boolean,
    keyCertSign: boolean,
    keyEncipherment: boolean,
    nonRepudiation: boolean,
}

export interface ExtendedKeyUsages {
    serverAuth: boolean,
    clientAuth: boolean,
    codeSigning: boolean,
    emailProtection: boolean,
    timeStamping: boolean,
    OCSPSigning: boolean,

}

export enum RevocationReason {

    UNSPECIFIED,
    KEY_COMPROMISE,
    CA_COMPROMISE,
    AFFILIATION_CHANGED,
    SUPERSEDED,
    CESSATION_OF_OPERATION,
    CERTIFICATE_HOLD,
    REMOVE_FROM_CRL,
    PRIVILEGE_WITHDRAWN,
    AA_COMPROMISE

}