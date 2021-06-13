package bsep.hospital.repository;

import bsep.hospital.logging.LogModel;
import bsep.hospital.logging.LogType;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

public interface LogRepository extends MongoRepository<LogModel, ObjectId> {

    long countByLogTimeBetween(LocalDateTime date1, LocalDateTime date2);
    int countByLevelAndLogTimeBetween(LogType logType, LocalDateTime date1, LocalDateTime date2);
}
