package com.vasilevyuv.springcore1.controller;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskStatus;
import com.vasilevyuv.springcore1.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
@Tag(name = "Task Manager", description = "API для управления задачами")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Получить все задачи",
            description = "Возвращает список всех задач с возможностью фильтрации по статусу и приоритету")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список задач успешно получен")
    })
    public List<Task> getAllTasks(
            @Parameter(description = "Фильтр по статусу (NEW, IN_PROGRESS, DONE)")
            @RequestParam(required = false) String status,

            @Parameter(description = "Фильтр по приоритету (LOW, MEDIUM, HIGH)")
            @RequestParam(required = false) String priority
    ) {
        return taskService.getAllTasks(status, priority);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID",
            description = "Возвращает задачу с указанным идентификатором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача найдена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новую задачу",
            description = "Создает задачу с указанными параметрами")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Задача успешно создана"),
            @ApiResponse(responseCode = "400", description = "Неверные данные задачи",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> createTask(
            @Parameter(description = "Данные новой задачи", required = true)
            @RequestBody Task task
    ) {
        Task createdTask = taskService.createTask(new Task (task.getTitle(),
                task.getDescription(),
                task.getPriority()));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Полностью обновить задачу",
            description = "Заменяет существующую задачу новыми данными")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Задача успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> updateTask(
            @Parameter(description = "ID задачи для обновления", required = true)
            @PathVariable Long id,

            @Parameter(description = "Новые данные задачи", required = true)
            @RequestBody Task task
    ) {
        task.setId(id);
        Task updatedTask = taskService.updateTask(id, task);
        return ResponseEntity.ok(updatedTask);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Изменить статус задачи",
            description = "Частичное обновление - меняет только статус задачи")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статус успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Неверный статус или отсутствует поле status",
                    content = @Content(schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Task> changeStatus(
            @Parameter(description = "ID задачи", required = true)
            @PathVariable Long id,

            @Parameter(description = "JSON с полем status (например: {\"status\": \"DONE\"})", required = true)
            @RequestBody Map<String, String> body
    ) {
        String statusStr = body.get("status");
        if (statusStr == null) {
            throw new IllegalArgumentException("Status field is required");
        }
        TaskStatus taskStatus = TaskStatus.valueOf(statusStr.toUpperCase());
        Task updatedTask = taskService.updateStatus(id, taskStatus);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу",
            description = "Удаляет задачу с указанным идентификатором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Задача успешно удалена (тело ответа пустое)"),
            @ApiResponse(responseCode = "404", description = "Задача не найдена",
                    content = @Content(schema = @Schema(implementation = Map.class)))
    })
    public ResponseEntity<Void> deleteTask(
            @Parameter(description = "ID задачи для удаления", required = true)
            @PathVariable Long id
    ) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    @Operation(summary = "Получить статистику по задачам",
            description = "Возвращает количество задач в каждом статусе (NEW, IN_PROGRESS, DONE)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Статистика успешно получена")
    })
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = taskService.getTaskStats();
        return ResponseEntity.ok(stats);
    }
}