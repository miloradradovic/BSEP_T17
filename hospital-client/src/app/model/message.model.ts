export interface Message {
    id: number,
    patient: String,
    dateTime: Date,
    type: String,
    message: String,
    alarm: boolean
}