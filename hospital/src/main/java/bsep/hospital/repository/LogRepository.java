package bsep.hospital.repository;

import bsep.hospital.logging.LogModel;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

public interface LogRepository extends MongoRepository<LogModel, ObjectId> {
}
