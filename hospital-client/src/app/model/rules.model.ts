export interface DoctorRule{
    ruleName: String,
    patient: String,
    bloodType: String,
    messageType: String,
    value: number,
    operation: String
}

export interface AdminRule{
    ruleName: String,
    messageInput: String,
    levelInput: String
}