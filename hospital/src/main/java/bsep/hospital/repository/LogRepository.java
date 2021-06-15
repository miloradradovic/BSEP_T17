package bsep.hospital.repository;

import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends MongoRepository<LogModel, ObjectId> {

    long countByLogTimeBetween(LocalDateTime date1, LocalDateTime date2);
    int countByLevelAndLogTimeBetween(LogType logType, LocalDateTime date1, LocalDateTime date2);
    List<LogModel> findByLogTimeBetween(LocalDateTime date1, LocalDateTime date2);

    List<LogModel> findAllByAlarm(boolean alarm);

    List<LogModel> findByLogTimeBefore(LocalDateTime to);
    List<LogModel> findByLogTimeAfter(LocalDateTime from);
}
