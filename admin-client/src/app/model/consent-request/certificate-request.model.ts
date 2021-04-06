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