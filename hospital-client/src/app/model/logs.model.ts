export interface LogModel{
    level: String,
    message: String,
    logTime: String,
    logSource: String,
    alarmDescription: String,
    ip: String,
    alarm: boolean
}

export interface LogsFilterModel {
    logType: String,
    logSource: String,
    dateFrom: Date,
    dateTo: Date
}
