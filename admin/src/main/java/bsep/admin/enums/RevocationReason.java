package bsep.admin.enums;

//https://docs.oracle.com/javase/7/docs/api/java/security/cert/CRLReason.html
public enum RevocationReason {

    AA_COMPROMISE,
    AFFILIATION_CHANGED,
    CA_COMPROMISE,
    CERTIFICATE_HOLD,
    CESSATION_OF_OPERATION,
    KEY_COMPROMISE,
    PRIVILEGE_WITHDRAWN,
    REMOVE_FROM_CRL,
    SUPERSEDED,
    UNSPECIFIED,
    UNUSED
}
