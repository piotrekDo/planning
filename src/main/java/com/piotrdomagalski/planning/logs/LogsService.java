package com.piotrdomagalski.planning.logs;

import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;

@Service
public class LogsService {
    private final LogsRepository logsRepository;

    public LogsService(LogsRepository logsRepository) {
        this.logsRepository = logsRepository;
    }

    public List<LogEntity> getAllLogsByUniqueIdentifier(String uniqueIdentifier) {
        return logsRepository.findAllByUniqueIdentifierIgnoreCase(uniqueIdentifier, Sort.by(Sort.Direction.DESC, "time"));
    }

    public LogEntity createNewEntityLog(String uniqueIdentifier) {
        String currentUserName = getUserName();
        LogEntity logEntity = new NewEntityLogFactory().makeLog(uniqueIdentifier, currentUserName);
        return logsRepository.save(logEntity);
    }

    public LogEntity createEditEntityLog(String uniqueIdentifier, String previousUniqueIdentifier) {
        if (previousUniqueIdentifier != null)
            updateLogsUniqueIdentifier(previousUniqueIdentifier, uniqueIdentifier);
        String currentUserName = getUserName();
        LogEntity logEntity = new EditEntityLogFactory().makeLog(uniqueIdentifier, currentUserName);
        return logsRepository.save(logEntity);
    }

    public LogEntity createDeleteEntityLog(String uniqueIdentifier) {
        String currentUserName = getUserName();
        LogEntity logEntity = new DeleteEntityFactory().makeLog(uniqueIdentifier, currentUserName);
        return logsRepository.save(logEntity);
    }

    public LogEntity createCoupleLog(String uniqueIdentifier, String relatedTo) {
        String currentUserName = getUserName();
        LogEntity logEntity = new CoupleLogFactory().makeLog(uniqueIdentifier, currentUserName, relatedTo);
        return logsRepository.save(logEntity);
    }

    public LogEntity createUnCoupleLog(String uniqueIdentifier, String relatedTo) {
        String currentUserName = getUserName();
        LogEntity logEntity = new UncoupleLogFactory().makeLog(uniqueIdentifier, currentUserName, relatedTo);
        return logsRepository.save(logEntity);
    }

    private void updateLogsUniqueIdentifier(String previousUniqueIdentifier, String newUniqueIdentifier) {
        List<LogEntity> logs = logsRepository.findAllByUniqueIdentifierIgnoreCase(previousUniqueIdentifier, Sort.by(Sort.Direction.DESC, "time"));
        logs.forEach(log -> log.setUniqueIdentifier(newUniqueIdentifier));
        logsRepository.saveAll(logs);
    }

    private String getUserName() {
//        @AuthenticationPrincipal UserPrincipal principal
        String currentUserName;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken))
            currentUserName = authentication.getName().toUpperCase(Locale.ROOT);
        else
            currentUserName = "SYSTEM";
        return currentUserName;
    }


}