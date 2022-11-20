package com.piotrdomagalski.planning.logs;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
public class LogsController {

    private final LogsService logsService;

    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }

    @GetMapping("/{identifier}")
    List<LogEntity> getAllLogsByUniqueIdentifier(@PathVariable String identifier) {
        return logsService.getAllLogsByUniqueIdentifier(identifier);
    }
}
