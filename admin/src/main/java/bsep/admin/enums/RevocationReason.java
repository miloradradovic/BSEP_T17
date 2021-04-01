package bsep.admin.enums;

//https://docs.oracle.com/javase/7/docs/api/java/security/cert/CRLReason.html
public enum RevocationReason {

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
