package com.sparklecow.book.services;

import java.time.LocalDateTime;

public interface AuditingService {
    LocalDateTime findCreationDate(Integer id);
    LocalDateTime findUpdateDate(Integer id);
}
