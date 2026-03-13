package com.vasilevyuv.springcore1.repository;

import com.vasilevyuv.springcore1.model.Task;
import com.vasilevyuv.springcore1.model.TaskPriority;
import com.vasilevyuv.springcore1.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByStatusAndPriority(TaskStatus status, TaskPriority priority);
    List<Task> findAllByOrderByPriorityDesc();

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.status = :status WHERE t.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") TaskStatus status);

    @Query("SELECT t.status, COUNT(t) FROM Task t GROUP BY t.status")
    List<Object[]> countTasksByStatus();

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.title = :#{#task.title}, " +
            "t.description = :#{#task.description}, " +
            "t.priority = :#{#task.priority}, " +
            "t.status = :#{#task.status} " +
            "WHERE t.id = :id")
    int updateTaskById(@Param("id") Long id, @Param("task") Task task);
}